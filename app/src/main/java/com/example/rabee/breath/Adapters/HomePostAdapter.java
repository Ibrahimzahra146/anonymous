package com.example.rabee.breath.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabee.breath.Activities.CommentActivity;
import com.example.rabee.breath.Activities.OtherProfileActivity;
import com.example.rabee.breath.Activities.ReactActivity;
import com.example.rabee.breath.Activities.YoutubeDialogActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.AddCommentModel;
import com.example.rabee.breath.Models.RequestModels.ReactRequestModel;
import com.example.rabee.breath.Models.RequestModels.SavePostRequestModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.Models.ResponseModels.PostResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.MyViewHolder> {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GeneralInfo.SPRING_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
    PostInterface postInterface = retrofit.create(PostInterface.class);
    List<Boolean> pressedLoveFlag, PressedLikeFlag, PressedUnlikeFlag;
    List<Integer> likeCount, loveCount, disLikeCount;
    EditText commentTextDialog;
    ProgressBar addCommentDialogProgressBar;
    TextView cancelBtnDialog;
    ImageView sendBtnDialog;
    int addCommentDialogPostId;
    Dialog addCommentDialog;
    private List<PostCommentResponseModel> postResponseModelsList;
    private Context context;
    public HomePostAdapter( ) {

    }

    public HomePostAdapter(Context context, List<PostCommentResponseModel> postResponseModelsList) {
        this.postResponseModelsList = postResponseModelsList;
        if(postResponseModelsList!=null){


        this.likeCount = new ArrayList<Integer>(Collections.nCopies(this.postResponseModelsList.size(), 0));
        this.loveCount = new ArrayList<Integer>(Collections.nCopies(this.postResponseModelsList.size(), 0));
        this.disLikeCount = new ArrayList<Integer>(Collections.nCopies(this.postResponseModelsList.size(), 0));
        this.pressedLoveFlag = new ArrayList<Boolean>(Collections.nCopies(this.postResponseModelsList.size(), false));
        this.PressedLikeFlag = new ArrayList<Boolean>(Collections.nCopies(this.postResponseModelsList.size(), false));
        this.PressedUnlikeFlag = new ArrayList<Boolean>(Collections.nCopies(this.postResponseModelsList.size(), false));
        this.context = context;
        addCommentDialog = new Dialog(this.context);
        addCommentDialog.setContentView(R.layout.activity_add_comment);
        addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.post_list_view_item, parent, false);
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
        String imageUrl = GeneralInfo.SPRING_URL + "/" + image;
        Log.d("Image url ", imageUrl);
        Picasso.with(this.context).load(imageUrl).into(holder.posterProfilePicture);
        if (postResponseModel.getImage() != null) {
            imageUrl = GeneralInfo.SPRING_URL + "/" + postResponseModel.getImage();

            Picasso.with(this.context).load(imageUrl).into(holder.postImage);

        }
        likeCount.set(position, postResponseModelsList.get(position).getReacts().getLikeList().getCount());
        disLikeCount.set(position, postResponseModelsList.get(position).getReacts().getDislikeList().getCount());
        loveCount.set(position, postResponseModelsList.get(position).getReacts().getLoveList().getCount());
        holder.postLoveCount.setText(loveCount.get(position) > 0 ? (String.valueOf(loveCount.get(position))) : "");
        holder.postLikeCount.setText(likeCount.get(position) > 0 ? (String.valueOf(likeCount.get(position))) : "");
        holder.postDislikeCount.setText(disLikeCount.get(position) > 0 ? (String.valueOf(disLikeCount.get(position))) : "");

        //// postStatusIcon

//        holder.postStatusIcon.setImageResource(postResponseModelsList.get(position).getPost().is_public_comment() ? R.drawable.unlocked_icon : R.drawable.locked_icon);
        // holder.postTime.setText(postResponseModelsList.get(position).getPost());
        int commentSize = postResponseModelsList.get(position).getComments().size();
        if (commentSize > 0) {
            if (commentSize == 1) {
                holder.postCommentCount.setText(String.valueOf(commentSize) + " comment");
            } else {
                holder.postCommentCount.setText(String.valueOf(commentSize) + " comments");
            }
        } else {
            holder.postCommentCount.setText("no comments");

            //holder.postCommentCount.setVisibility(View.INVISIBLE);
        }

//        Log.d("Reacts", "id " + postResponseModelsList.get(position).getPost().getPostId());
//        Log.d("Reacts", "love " + postResponseModelsList.get(position).getReacts().getLoveList().getMyAction() + " ");
//        Log.d("Reacts", "like " + postResponseModelsList.get(position).getReacts().getLikeList().getMyAction() + " ");
//        Log.d("Reacts", "unlike " + postResponseModelsList.get(position).getReacts().getDislikeList().getMyAction() + " ");


        holder.postUnlikeIcon.setImageResource(R.drawable.unlike_icon);
        holder.postLoveIcon.setImageResource(R.drawable.love_icon);
        holder.postUnlikeIcon.setImageResource(R.drawable.unlike_icon);


        if (postResponseModelsList.get(position).getReacts().getLoveList().getMyAction()) {
            holder.postLoveIcon.setImageResource(R.drawable.love_icon_filled);
            pressedLoveFlag.set(position, true);
        }

        if (postResponseModelsList.get(position).getReacts().getLikeList().getMyAction()) {
            holder.postLikeIcon.setImageResource(R.drawable.filled_thumb_up);
            PressedLikeFlag.set(position, true);

        }
        if (postResponseModelsList.get(position).getReacts().getDislikeList().getMyAction()) {
            holder.postUnlikeIcon.setImageResource(R.drawable.dislike_filled_icon);
            PressedUnlikeFlag.set(position, true);

        }
        /////////////////Listeners//////////////////////////////
        //React listener
        View.OnClickListener LoveReactsClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (postResponseModelsList.get(position).getReacts().getLoveList().getCount() > 0) {
                    Log.d("ReactAction", "React Action == Pressed");
                    Intent i = new Intent(getApplicationContext(), ReactActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());
                    b.putInt("type", 3);
                    i.putExtras(b);
                    context.startActivity(i);
                    // your stuff
                }
            }
        };

        View.OnClickListener LikeReactsClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (postResponseModelsList.get(position).getReacts().getLikeList().getCount() > 0) {

                    Intent i = new Intent(getApplicationContext(), ReactActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());
                    b.putInt("type", 1);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            }
        };

        View.OnClickListener UnlikeReactsClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (postResponseModelsList.get(position).getReacts().getDislikeList().getCount() > 0) {

                    Intent i = new Intent(getApplicationContext(), ReactActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());
                    b.putInt("type", 2);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            }
        };

        holder.loveLayout.setOnClickListener(LoveReactsClickListener);
        holder.likeLayout.setOnClickListener(LikeReactsClickListener);
        holder.unlikeLayout.setOnClickListener(UnlikeReactsClickListener);
        ///////////////Listeners//////////////////////////////
        // React listener


        View.OnClickListener addCommentListener = (new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCommentDialog.show();
                commentTextDialog = (EditText) addCommentDialog.findViewById(R.id.commentText);
                cancelBtnDialog = (TextView) addCommentDialog.findViewById(R.id.cancelBtn);
                sendBtnDialog = (ImageView) addCommentDialog.findViewById(R.id.sendBtn);
                addCommentDialogPostId = postResponseModelsList.get(position).getPost().getPostId();
                addCommentDialogProgressBar = (ProgressBar) addCommentDialog.findViewById(R.id.progressBar);
                sendBtnDialog.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (commentTextDialog.getText().toString().trim().equals("")) {

                                } else {
                                    addCommentDialogProgressBar.setVisibility(View.VISIBLE);
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
            }
        });
        holder.addComnent.setOnClickListener(addCommentListener);
        holder.addCommentLayout.setOnClickListener(addCommentListener);

        View.OnClickListener commentListener = new View.OnClickListener() {
            public void onClick(View v) {
                // if (postResponseModelsList.get(position).getComments().size() == 0) {

                //} else {
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                Bundle b = new Bundle();
                b.putInt("postId", postResponseModelsList.get(position).getPost().getPostId());

                i.putExtras(b);
                context.startActivity(i);
                //}

            }
        };
        //      holder.postCommentCount.setOnClickListener(commentListener);
//        holder.postCommentIcon.setOnClickListener(commentListener);
        holder.viewCommentLayout.setOnClickListener(commentListener);

        //////////////react icon listener//////////////////////
        holder.postLoveIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!pressedLoveFlag.get(position)) {
                            AddNewReact(position, 3);
                            holder.postLoveIcon.setImageResource(R.drawable.love_icon_filled);
                            holder.postLikeIcon.setImageResource(R.drawable.like_icon);
                            holder.postUnlikeIcon.setImageResource(R.drawable.unlike_icon);
                            loveCount.set(position, loveCount.get(position) + 1);
                            likeCount.set(position, !PressedLikeFlag.get(position) ? likeCount.get(position) : (likeCount.get(position) - 1));
                            disLikeCount.set(position, !PressedUnlikeFlag.get(position) ? disLikeCount.get(position) : (disLikeCount.get(position) - 1));
                        } else {
                            DeleteReact(position, 0);
                            holder.postLoveIcon.setImageResource(R.drawable.love_icon);
                            loveCount.set(position, loveCount.get(position) - 1);
                        }
                        holder.postLoveCount.setText(loveCount.get(position) > 0 ? (String.valueOf(loveCount.get(position))) : "");
                        holder.postLikeCount.setText(likeCount.get(position) > 0 ? (String.valueOf(likeCount.get(position))) : "");
                        holder.postDislikeCount.setText(disLikeCount.get(position) > 0 ? (String.valueOf(disLikeCount.get(position))) : "");
                        pressedLoveFlag.set(position, !pressedLoveFlag.get(position));
                        PressedUnlikeFlag.set(position, false);
                        PressedLikeFlag.set(position, false);
                    }
                });
        holder.postLikeIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!PressedLikeFlag.get(position)) {
                            AddNewReact(position, 1);
                            holder.postLikeIcon.setImageResource(R.drawable.filled_thumb_up);
                            holder.postUnlikeIcon.setImageResource(R.drawable.unlike_icon);
                            holder.postLoveIcon.setImageResource(R.drawable.love_icon);
                            likeCount.set(position, likeCount.get(position) + 1);
                            loveCount.set(position, !pressedLoveFlag.get(position) ? loveCount.get(position) : (loveCount.get(position) - 1));
                            disLikeCount.set(position, !PressedUnlikeFlag.get(position) ? disLikeCount.get(position) : (disLikeCount.get(position) - 1));
                        } else {
                            DeleteReact(position, 0);
                            holder.postLikeIcon.setImageResource(R.drawable.like_icon);
                            likeCount.set(position, likeCount.get(position) - 1);
                        }
                        holder.postLoveCount.setText(loveCount.get(position) > 0 ? (String.valueOf(loveCount.get(position))) : "");
                        holder.postLikeCount.setText(likeCount.get(position) > 0 ? (String.valueOf(likeCount.get(position))) : "");
                        holder.postDislikeCount.setText(disLikeCount.get(position) > 0 ? (String.valueOf(disLikeCount.get(position))) : "");
                        PressedLikeFlag.set(position, !PressedLikeFlag.get(position));
                        pressedLoveFlag.set(position, false);
                        PressedUnlikeFlag.set(position, false);
                    }
                });

        holder.postUnlikeIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!PressedUnlikeFlag.get(position)) {
                            AddNewReact(position, 2);
                            holder.postUnlikeIcon.setImageResource(R.drawable.dislike_filled_icon);
                            holder.postLoveIcon.setImageResource(R.drawable.love_icon);
                            holder.postLikeIcon.setImageResource(R.drawable.like_icon);
                            disLikeCount.set(position, disLikeCount.get(position) + 1);
                            loveCount.set(position, !pressedLoveFlag.get(position) ? loveCount.get(position) : (loveCount.get(position) - 1));
                            likeCount.set(position, !PressedLikeFlag.get(position) ? likeCount.get(position) : (likeCount.get(position) - 1));
                        } else {
                            DeleteReact(position, 0);
                            holder.postUnlikeIcon.setImageResource(R.drawable.unlike_icon);
                            disLikeCount.set(position, disLikeCount.get(position) - 1);
                        }
                        holder.postLoveCount.setText(loveCount.get(position) > 0 ? (String.valueOf(loveCount.get(position))) : "");
                        holder.postLikeCount.setText(likeCount.get(position) > 0 ? (String.valueOf(likeCount.get(position))) : "");
                        holder.postDislikeCount.setText(disLikeCount.get(position) > 0 ? (String.valueOf(disLikeCount.get(position))) : "");
                        PressedUnlikeFlag.set(position, !PressedUnlikeFlag.get(position));
                        pressedLoveFlag.set(position, false);
                        PressedLikeFlag.set(position, false);
                    }
                });

        holder.savePostLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( holder.saveIcon.getDrawable().getConstantState() !=  getDrawable(getApplicationContext(),R.drawable.saved_icon).getConstantState() )
                {
                    SavePost(position, holder.saveIcon);
                }
                else
                {
                    unSavePost(position,holder.saveIcon);
                }
            }
        });

        if (postResponseModel.getLink() != "" && postResponseModel.getImage() == null) {
           // holder.youtubeLinkTitle.setText(postResponseModel.getYoutubelink().getTitle());
            //holder.youtubeLinkAuthor.setText("Channel: " + postResponseModel.getYoutubelink().getAuthor_name());
            //imageUrl = GeneralInfo.SPRING_URL + "/" + postResponseModel.getYoutubelink().getImage();
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

        View.OnClickListener userProfileListener = new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), OtherProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("mName", holder.posterUserName.getText().toString());
                b.putInt("Id", postResponseModelsList.get(position).getPost().getUserId().getId());
                b.putString("mImageURL", postResponseModelsList.get(position).getPost().getUserId().getImage());
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(b);
                context.startActivity(i);
            }
        };

        holder.posterProfilePicture.setOnClickListener(userProfileListener);
        holder.posterUserName.setOnClickListener(userProfileListener);
    }

    @Override
    public int getItemCount() {
        return postResponseModelsList.size();
    }


    public void AddNewReact(int position, int type) {

        PostInterface addNewReact = retrofit.create(PostInterface.class);

        ReactRequestModel reactRequestModel = new ReactRequestModel();
        int postId = postResponseModelsList.get(position).getPost().getPostId();
        reactRequestModel.setPostId(postId);
        reactRequestModel.setType(type);
        reactRequestModel.setUserId(GeneralInfo.getUserID());
        Call<Integer> addNewReactResponse = addNewReact.addNewReact(reactRequestModel);

        addNewReactResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }

        });
    }

    public void DeleteReact(int position, int type) {

        PostInterface deleteReact = retrofit.create(PostInterface.class);

        ReactRequestModel reactRequestModel = new ReactRequestModel();
        int postId = postResponseModelsList.get(position).getPost().getPostId();
        reactRequestModel.setPostId(postId);
        reactRequestModel.setType(type);
        reactRequestModel.setUserId(GeneralInfo.getGeneralUserInfo().getUser().getId());
        Call<Integer> addNewReactResponse = deleteReact.deleteReact(reactRequestModel);

        addNewReactResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }

        });
    }

    public void SavePost(int position, final ImageView saveIcon) {

        PostInterface savePost = retrofit.create(PostInterface.class);
        SavePostRequestModel savePostRequestModel = new SavePostRequestModel();

        int postId = postResponseModelsList.get(position).getPost().getPostId();
        savePostRequestModel.setUserId(GeneralInfo.getUserID());
        savePostRequestModel.setPostId(postId);
        Call<SavePostRequestModel> savePostRequestModelCall = savePost.savePost(savePostRequestModel);

        savePostRequestModelCall.enqueue(new Callback<SavePostRequestModel>() {
            @Override
            public void onResponse(Call<SavePostRequestModel> call, Response<SavePostRequestModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {
                    saveIcon.setImageResource(R.drawable.saved_icon);
                    Toast.makeText(context, "Post saved",
                            Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<SavePostRequestModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("PostHolder", t.getMessage());
            }

        });
    }

    public void unSavePost(int position, final ImageView saveIcon) {

        PostInterface unsavePost = retrofit.create(PostInterface.class);
        SavePostRequestModel unsavePostRequestModel = new SavePostRequestModel();

        int postId = postResponseModelsList.get(position).getPost().getPostId();
        unsavePostRequestModel.setUserId(GeneralInfo.getUserID());
        unsavePostRequestModel.setPostId(postId);
        Call<SavePostRequestModel> savePostRequestModelCall = unsavePost.unsavePost(unsavePostRequestModel);

        savePostRequestModelCall.enqueue(new Callback<SavePostRequestModel>() {
            @Override
            public void onResponse(Call<SavePostRequestModel> call, Response<SavePostRequestModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {
                    saveIcon.setImageResource(R.drawable.save_icon);
                    Toast.makeText(context, "Post unsaved",
                            Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<SavePostRequestModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("PostHolder", t.getMessage());
            }

        });
    }
    public void sendComment(final TextView commentCoutnerView, final int commentCounter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
        PostInterface sendComment = retrofit.create(PostInterface.class);
        AddCommentModel addCommentModel = new AddCommentModel();
        addCommentModel.setPostId(addCommentDialogPostId);
        addCommentModel.setUserId(GeneralInfo.getUserID());
        addCommentModel.setText(commentTextDialog.getText().toString());
        Call<AddCommentModel> addNewReactResponse = sendComment.addComment(addCommentModel);

        addNewReactResponse.enqueue(new Callback<AddCommentModel>() {
            @Override
            public void onResponse(Call<AddCommentModel> call, Response<AddCommentModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    addCommentDialogProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    addCommentDialogProgressBar.setVisibility(View.INVISIBLE);

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
                addCommentDialogProgressBar.setVisibility(View.INVISIBLE);

            }

        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView posterProfilePicture;
        TextView posterUserName;
        TextView postTime;
        TextView postLoveCount, postLikeCount, postDislikeCount;
        TextView postCommentCount;
        TextView postBodyText;
        ImageView postImage;
        ImageView youtubeLinkImage;
        TextView youtubeLinkTitle;
        TextView youtubeLinkAuthor;
        LinearLayout youtubeLinkLayout, unlikeLayout, likeLayout, loveLayout;
        LinearLayout viewCommentLayout, addCommentLayout, savePostLayout;
        ImageView postCommentIcon, postLoveIcon, postLikeIcon, postUnlikeIcon, postStatusIcon, saveIcon, addComnent;

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
            postLoveCount = (TextView) itemView.findViewById(R.id.loveCount);
            postLikeCount = (TextView) view.findViewById(R.id.likeCount);
            postDislikeCount = (TextView) view.findViewById(R.id.unlikeCount);
            postLoveIcon = (ImageView) view.findViewById(R.id.love_post);
            postLikeIcon = (ImageView) view.findViewById(R.id.like_post);
            postUnlikeIcon = (ImageView) view.findViewById(R.id.dislike_post);
            saveIcon = (ImageView) view.findViewById(R.id.save_icon);
            postStatusIcon = (ImageView) view.findViewById(R.id.postStatus);
            addComnent = (ImageView) view.findViewById(R.id.add_comment);
            postTime = (TextView) view.findViewById(R.id.postTime);
            unlikeLayout = (LinearLayout) view.findViewById(R.id.dislikeLayout);
            likeLayout = (LinearLayout) view.findViewById(R.id.likeLayout);
            loveLayout = (LinearLayout) view.findViewById(R.id.loveLayout);
            viewCommentLayout = (LinearLayout) view.findViewById(R.id.viewCommentLayout);
            addCommentLayout = (LinearLayout) view.findViewById(R.id.addCommentLayout);
            savePostLayout = (LinearLayout) view.findViewById(R.id.savePostLayout);


        }
    }
}
