package com.example.rabee.breath.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rabee.breath.Activities.CommentActivity;
import com.example.rabee.breath.Activities.OtherProfileActivity;
import com.example.rabee.breath.Activities.YoutubeDialogActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.AddCommentModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.Models.ResponseModels.PostResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 3/27/2018.
 */

public class DirectSignUpHomePostsAdapter extends RecyclerView.Adapter<DirectSignUpHomePostsAdapter.MyViewHolder> {
    Context context;
    EditText commentTextDialog;
    TextView cancelBtnDialog;
    ImageView sendBtnDialog;
    int addCommentDialogPostId;
    Dialog addCommentDialog;
    private List<PostCommentResponseModel> postResponseModelsList;

    public DirectSignUpHomePostsAdapter(Context context, List<PostCommentResponseModel> postResponseModelsList) {
        this.postResponseModelsList = postResponseModelsList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.direct_sign_up_post_list_view_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PostResponseModel postResponseModel = postResponseModelsList.get(position).getPost();
        holder.posterUserName.setText(postResponseModel.getUserId().getFirst_name() + " " + postResponseModel.getUserId().getLast_name());
        holder.postBodyText.setText(postResponseModel.getText());
        long now = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date convertedDate = dateFormat.parse(postResponseModelsList.get(position).getPost().getTimestamp());
            CharSequence relativeTIme = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.postTime.setText(relativeTIme);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String image;
        image = postResponseModel.getUserId().getImage();
        Gson gson = new Gson();
        String json = gson.toJson(postResponseModel);
        String imageUrl = GeneralInfo.SPRING_URL +"/"+ image;
        Picasso.with(this.context).load(imageUrl).into(holder.posterProfilePicture);
        if (postResponseModel.getImage() != null) {
            imageUrl = GeneralInfo.SPRING_URL + "/" + postResponseModel.getImage();
            Picasso.with(this.context).load(imageUrl).into(holder.postImage);

        }
        //// postStatusIcon

        holder.postStatusIcon.setImageResource(postResponseModelsList.get(position).getPost().is_public_comment() ? R.drawable.unlocked_icon : R.drawable.locked_icon);
        // holder.postTime.setText(postResponseModelsList.get(position).getPost());
        int commentSize = postResponseModelsList.get(position).getComments().size();
        if (commentSize > 0) {
            if (commentSize == 1) {
                holder.postCommentCount.setText(String.valueOf(commentSize) + " comment");
            } else {
                holder.postCommentCount.setText(String.valueOf(commentSize) + " comments");
            }
        } else {
            holder.postCommentCount.setVisibility(View.INVISIBLE);
        }
        holder.addComnent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCommentDialog = new Dialog(context);
                addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                addCommentDialog.setContentView(R.layout.activity_add_comment);
                addCommentDialog.show();
                commentTextDialog = (EditText) addCommentDialog.findViewById(R.id.commentText);
                cancelBtnDialog = (TextView) addCommentDialog.findViewById(R.id.cancelBtn);
                sendBtnDialog = (ImageView) addCommentDialog.findViewById(R.id.sendBtn);
                addCommentDialogPostId = postResponseModelsList.get(position).getPost().getPostId();
                sendBtnDialog.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (commentTextDialog.getText().toString().trim().equals("")) {

                                } else {
                                    sendComment(holder.postCommentCount, postResponseModelsList.get(position).getComments().size());
                                }
                            }
                        });

                cancelBtnDialog.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addCommentDialog.dismiss();
                            }
                        });
//
//                Intent i = new Intent(getApplicationContext(), AddCommentActivity.class);
//                Bundle b = new Bundle();
//                b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());
//                i.putExtras(b);
//                context.startActivity(i);

            }
        });
        View.OnClickListener commentListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (postResponseModelsList.get(position).getComments().size() == 0) {

                } else {
                    Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());

                    i.putExtras(b);
                    context.startActivity(i);
                }

            }
        };
        View.OnClickListener userProfileListener = new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), OtherProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("mName", holder.posterUserName.getText().toString());
                b.putInt("Id",postResponseModelsList.get(position).getPost().getUserId().getId() );
                b.putString("mImageURL", postResponseModelsList.get(position).getPost().getUserId().getImage());
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(b);
                context.startActivity(i);


            }
        };
        holder.postCommentCount.setOnClickListener(commentListener);
        holder.postCommentIcon.setOnClickListener(commentListener);
        holder.posterProfilePicture.setOnClickListener(userProfileListener);
        holder.posterUserName.setOnClickListener(userProfileListener);
        ////////////////YOUTUBE///
        if (postResponseModel.getLink() != "" && postResponseModel.getImage() == null) {
        //    holder.youtubeLinkTitle.setText(postResponseModel.getYoutubelink().getTitle());
         //   holder.youtubeLinkAuthor.setText("Channel: " + postResponseModel.getYoutubelink().getAuthor_name());
          //  imageUrl = GeneralInfo.SPRING_URL + "/" + postResponseModel.getYoutubelink().getImage();
            Picasso.with(getApplicationContext()).load(imageUrl).into(holder.youtubeLinkImage);
        } else {
            holder.youtubeLinkLayout.setVisibility(View.GONE);
        }

        View.OnClickListener youtubeListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), YoutubeDialogActivity.class);
                Bundle b = new Bundle();
                b.putString("youtubeSongUrl", postResponseModel.getLink());
                i.putExtras(b);
                getApplicationContext().startActivity(i);
            }
        };

        holder.youtubeLinkTitle.setOnClickListener(youtubeListener);
        holder.youtubeLinkAuthor.setOnClickListener(youtubeListener);
        holder.youtubeLinkImage.setOnClickListener(youtubeListener);

    }

    @Override
    public int getItemCount() {
        return postResponseModelsList.size();
    }

    public void sendComment(final TextView commentCoutnerView, final int commentCounter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext()))
                .build();
        PostInterface sendComment = retrofit.create(PostInterface.class);
        AddCommentModel addCommentModel = new AddCommentModel();
        addCommentModel.setPostId(addCommentDialogPostId);
        addCommentModel.setUserId(GeneralInfo.getGeneralUserInfo().getUser().getId());
        addCommentModel.setText(commentTextDialog.getText().toString());
        Call<AddCommentModel> addNewReactResponse = sendComment.addComment(addCommentModel);

        addNewReactResponse.enqueue(new Callback<AddCommentModel>() {
            @Override
            public void onResponse(Call<AddCommentModel> call, Response<AddCommentModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {
                    int newCoutner = commentCounter + 1;
                    addCommentDialog.dismiss();
                    commentTextDialog.setText("");
                    commentCoutnerView.setText(String.valueOf(newCoutner) + (newCoutner > 1 ? " comments" : " comment"));
                    commentCoutnerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<AddCommentModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }

        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView posterProfilePicture;
        TextView posterUserName;
        TextView postTime;
        TextView postCommentCount;
        TextView postBodyText;
        ImageView postImage;
        ImageView youtubeLinkImage;
        TextView youtubeLinkTitle;
        TextView youtubeLinkAuthor;
        LinearLayout youtubeLinkLayout;
        ImageView postCommentIcon, postStatusIcon, addComnent;

        public MyViewHolder(View view) {
            super(view);
            posterProfilePicture = (CircleImageView) itemView.findViewById(R.id.userProfilePicture);
            posterUserName = (TextView) itemView.findViewById(R.id.username);
            postBodyText = (TextView) view.findViewById(R.id.postText);
            postCommentIcon = (ImageView) view.findViewById(R.id.comment_icon);
            postCommentCount = (TextView) view.findViewById(R.id.commentText);
            postImage = (ImageView) view.findViewById(R.id.postImage);
            youtubeLinkImage = (ImageView) view.findViewById(R.id.youtubeLinkImage);
            youtubeLinkTitle = (TextView) view.findViewById(R.id.youtubeLinkTitle);
            youtubeLinkLayout = (LinearLayout) view.findViewById(R.id.youtubeLinkLayout);
            youtubeLinkAuthor = (TextView) view.findViewById(R.id.youtubeLinkAuthor);

            postStatusIcon = (ImageView) view.findViewById(R.id.postStatus);
            addComnent = (ImageView) view.findViewById(R.id.add_comment);
            postTime = (TextView) view.findViewById(R.id.postTime);
        }
    }
}
