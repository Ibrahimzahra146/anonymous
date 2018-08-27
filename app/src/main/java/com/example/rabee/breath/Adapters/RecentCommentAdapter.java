package com.example.rabee.breath.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
        import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rabee.breath.Activities.CommentActivity;
import com.example.rabee.breath.Activities.ViewPostActivity;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 2/25/2018.
 */

public class RecentCommentAdapter extends RecyclerView.Adapter<RecentCommentAdapter.UserViewHolder> {
    Context context;
    List<PostCommentResponseModel> postCommentResponseModels;
    View view;

    public RecentCommentAdapter(Context context, List<PostCommentResponseModel> postCommentResponseModels) {
        this.context = context;
        this.postCommentResponseModels = postCommentResponseModels;
    }

        @Override
        public RecentCommentAdapter.UserViewHolder onCreateViewHolder (ViewGroup parent,int viewType)
        {
            view = LayoutInflater.from(context).inflate(R.layout.general_post_item_view, null);
            return new RecentCommentAdapter.UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder (RecentCommentAdapter.UserViewHolder holder,final int position)
        {
            String imageUrl;
            holder.newCommentsCounter.setText(String.valueOf(postCommentResponseModels.get(position).getComments().size()));
            holder.postText.setText(postCommentResponseModels.get(position).getPost().getText());

            if (postCommentResponseModels.get(position).getPost().getImage() != null) {
                imageUrl = GeneralInfo.SPRING_URL + "/" + postCommentResponseModels.get(position).getPost().getImage();
                Picasso.with(context).load(imageUrl).into(holder.postImage);
                holder.postImage.setVisibility(View.VISIBLE);

            }
//        imageUrl = GeneralInfo.SPRING_URL + "/" +postCommentResponseModels.get(position).getPost().getUserId().getImage() ;
//        Picasso.with(context).load(imageUrl).into(holder.profilePic );
            if (!postCommentResponseModels.get(position).getPost().getLink().equals("")) {
                holder.postText.setText(postCommentResponseModels.get(position).getPost().getLink());

            }
            long now = System.currentTimeMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date convertedDate = dateFormat.parse(postCommentResponseModels.get(position).getPost().getTimestamp());
                CharSequence relativeTIme = DateUtils.getRelativeTimeSpanString(
                        convertedDate.getTime(),
                        now,
                        DateUtils.SECOND_IN_MILLIS);
                holder.postTime.setText(relativeTIme);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            /////////////////Listeners/////////
            holder.commentsNumSection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle b = new Bundle();
                    b.putInt("postId", postCommentResponseModels.get(position).getPost().getPostId());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            holder.postText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ViewPostActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // Bundle b = new Bundle();
                 //   b.putInt("postId", postCommentResponseModels.get(position).getPost().getPostId());
                    Log.d("ActivityAdd2", " " + postCommentResponseModels.get(position).getPost().getPostId());
                    i.putExtra("postId", postCommentResponseModels.get(position).getPost().getPostId());

                  //  i.putExtras(b);
                    context.startActivity(i);
                }
            });
//        imageUrl = GeneralInfo.SPRING_URL + "/" +postCommentResponseModels.get(position).getPost().getUserId().getImage() ;
//        Picasso.with(context).load(imageUrl).into(holder.profilePic );
        }

        @Override
        public int getItemCount () {
            return postCommentResponseModels.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {
            TextView postText, newCommentsCounter , postTime;
            CircleImageView postImage;
            RelativeLayout commentsNumSection;

            public UserViewHolder(View itemView) {
                super(itemView);
                postText=(TextView)itemView.findViewById(R.id.postText);
                postImage=(CircleImageView)itemView.findViewById(R.id.postImage);
                newCommentsCounter=(TextView)itemView.findViewById(R.id.newCommentsCounter);
                postTime=(TextView)itemView.findViewById(R.id.postTime);
                commentsNumSection=(RelativeLayout)itemView.findViewById(R.id.comments_num_section);


            }
        }
    }
