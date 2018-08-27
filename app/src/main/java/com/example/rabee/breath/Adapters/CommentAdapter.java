package com.example.rabee.breath.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rabee.breath.Activities.CommentActivity;
import com.example.rabee.breath.Activities.ReplyActivity;
import com.example.rabee.breath.Activities.UserProfileActivity;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.CommentModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.Services.FollowingService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 2/10/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.UserViewHolder>  {
    Context context;
    List<CommentModel> commentModelsList;
    TextView commentText;
    View view;
    UserModel user;

    public CommentAdapter(Context context, List<CommentModel> commentModelsList, UserModel user){
        this.context=context;
        this.commentModelsList=commentModelsList;
        this.user=user;
    }
    @Override
    public CommentAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.comment_list_view_item, null);
        return new CommentAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {

        long now = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date convertedDate = dateFormat.parse(commentModelsList.get(position).getTimestamp());
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            holder.commentTime.setText(relativeTime);
        } catch (ParseException e) {
            holder.commentTime.setText("");
            e.printStackTrace();
        }


            holder.commentText.setText(commentModelsList.get(position).getText());
        int id = user.getId();
        if (commentModelsList.get(position).getUser().getId() == id) {
            String imageUrl = GeneralInfo.SPRING_URL + "/" + user.getImage();
            Picasso.with(context).load(imageUrl).into(holder.profilePic);
            holder.fullName.setText(user.getFirst_name() + " " + user.getLast_name());
        }
        Log.d("User id",GeneralInfo.userID+" " + GeneralInfo.getLoginType());
        if (commentModelsList.get(position).getUser().getId() == GeneralInfo.userID && !GeneralInfo.getLoginType().equals("DIRECT_SIGNUP")) {

            String imageUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.generalUserInfo.getUser().getImage();
            Picasso.with(context).load(imageUrl).into(holder.profilePic);
            holder.fullName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());
        }
        if(commentModelsList.get(position).getReplies().size()==0){

            holder.repliesNumber.setText("0");
            //holder.repliesNumber.setVisibility(View.INVISIBLE);

        }else holder.repliesNumber.setText(String.valueOf(commentModelsList.get(position).getReplies().size()));
        holder.repliesNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ReplyActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putInt("commentId",commentModelsList.get(position).getId());

                i.putExtras(b);
                context.startActivity(i);

            }
        });
    }



    @Override
    public int getItemCount() {
        return commentModelsList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        TextView repliesNumber;
        CircleImageView profilePic;
        TextView fullName;
        TextView commentTime;


        public UserViewHolder(View itemView) {
            super(itemView);
            commentText=(TextView)itemView.findViewById(R.id.commentText);
            repliesNumber=(TextView)itemView.findViewById(R.id.replies_counter);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            fullName = (TextView) itemView.findViewById(R.id.full_name);
            commentTime= (TextView) itemView.findViewById(R.id.comment_time);


        }
    }
}
