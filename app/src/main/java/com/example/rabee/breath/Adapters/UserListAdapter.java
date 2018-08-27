package com.example.rabee.breath.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rabee.breath.Activities.UserProfileActivity;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.Services.FollowingService;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hbb20.R.styleable.RecyclerView;

/**
 * Created by Rabee on 1/20/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    public View view;
    private Context mContext;
    public static List<UserModel> userModelList;
    CircleImageView ivProfile;
    TextView tvName;


    public UserListAdapter(Context mContext, List<UserModel> userModelList) {
        this.mContext = mContext;
        this.userModelList = userModelList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.user_list_view_item, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        UserModel userModel=new UserModel();
        userModel=userModelList.get(position);
        String fullName=userModel.getFirst_name() +" "+userModel.getLast_name();

        holder.tvName.setText(fullName);
        String imageUrl = GeneralInfo.SPRING_URL + "/" +userModelList.get(position).getImage() ;
        Picasso.with(mContext).load(imageUrl).into(holder.ivProfile);
        final UserModel finalUserModel = userModel;
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userModelList.get(position).getId() == GeneralInfo.getUserID()) {
                    Intent i = new Intent(mContext, UserProfileActivity.class);
                    mContext.startActivity(i);
                } else {
                    FollowingService.startRightActivity(mContext, userModelList.get(position).getFirst_name(), userModelList.get(position).getId(), finalUserModel.getImage());
                }
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userModelList.get(position).getId() == GeneralInfo.getUserID()) {
                    Intent i = new Intent(mContext, UserProfileActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                } else {
                    FollowingService.startRightActivity(mContext, userModelList.get(position).getFirst_name(), userModelList.get(position).getId(), finalUserModel.getImage());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvName;


        public UserViewHolder(View itemView) {

            super(itemView);

            ivProfile = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            tvName = (TextView) itemView.findViewById(R.id.Name);
        }
    }
}
