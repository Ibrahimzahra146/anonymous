package com.example.rabee.breath.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by Rabee on 1/20/2018.
 */

public class CommentModel {
    int id;
    String text;
    private String timestamp;
    List<ReplyModel> replies;
    UserModel userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<ReplyModel> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyModel> replies) {
        this.replies = replies;
    }

    public UserModel getUser() {
        return userId;
    }

    public void setUser(UserModel user) {
        this.userId = user;
    }
}
