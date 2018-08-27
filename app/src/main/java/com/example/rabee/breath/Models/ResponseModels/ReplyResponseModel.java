package com.example.rabee.breath.Models.ResponseModels;

import com.example.rabee.breath.Models.ReplyModel;
import com.example.rabee.breath.Models.UserModel;

import java.util.List;

/**
 * Created by Rabee on 2/15/2018.
 */

public class ReplyResponseModel {
    List<ReplyModel> replies;
    UserModel user;

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
