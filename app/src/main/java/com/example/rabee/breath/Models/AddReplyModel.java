package com.example.rabee.breath.Models;

/**
 * Created by exalt on 3/8/2018.
 */

public class AddReplyModel {
    int userId;
    int commentId;
    String text;


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
