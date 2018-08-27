package com.example.rabee.breath.Models.RequestModels;

/**
 * Created by Rabee on 2/16/2018.
 */

public class AddCommentModel {
    int userId;
    int postId;
    String text;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
