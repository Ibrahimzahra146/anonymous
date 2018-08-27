package com.example.rabee.breath.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ReplyModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rabee on 2/14/2018.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.UserViewHolder> {
    Context context;
    List<ReplyModel> replyModelList;
    View view;
    UserModel userModel;

    public ReplyAdapter(Context context, List<ReplyModel> replyModelList, UserModel userModel) {
        this.context = context;
        this.replyModelList = replyModelList;
        this.userModel = userModel;
    }

    @Override
    public ReplyAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.reply_list_view_item, null);
        return new ReplyAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.UserViewHolder holder, int position) {
        holder.replyText.setText(replyModelList.get(position).getText());
        Log.d("userModel.getId()", userModel.getId() + "");
        int id = userModel.getId();
        if (replyModelList.get(position).getUser().getId() == GeneralInfo.getUserID() && !GeneralInfo.getLoginType().equals("DIRECT_SIGNUP")) {
            String imageUrl = GeneralInfo.SPRING_URL + "/" + userModel.getImage();
            Picasso.with(context).load(imageUrl).into(holder.profilePic);
            holder.fullName.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
        }
        if (replyModelList.get(position).getUser().getId() == GeneralInfo.userID && !GeneralInfo.getLoginType().equals("DIRECT_SIGNUP")) {
            String imageUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.generalUserInfo.getUser().getImage();
            Picasso.with(context).load(imageUrl).into(holder.profilePic);
            holder.fullName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());
        }

    }

    @Override
    public int getItemCount() {
        return replyModelList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView replyText;
        CircleImageView profilePic;
        TextView fullName;

        public UserViewHolder(View itemView) {
            super(itemView);
            replyText = (TextView) itemView.findViewById(R.id.replyText);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            fullName = (TextView) itemView.findViewById(R.id.full_name);

        }
    }
}
