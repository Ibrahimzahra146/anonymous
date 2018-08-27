package com.example.rabee.breath.Models.RequestModels;

/**
 * Created by Rabee on 1/22/2018.
 */

public class ReactRequestModel {

    int userId;
    int type;
    int postId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}