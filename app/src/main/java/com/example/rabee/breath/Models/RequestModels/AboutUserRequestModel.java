package com.example.rabee.breath.Models.RequestModels;

public class AboutUserRequestModel {

    int userId;
    String userBio;
    String userStatus;
    String userSong;

    public AboutUserRequestModel(int user_id, String user_bio, String user_status, String user_song) {
        this.userId = user_id;
        this.userBio = user_bio;
        this.userStatus = user_status;
        this.userSong = user_song;

    }
    public AboutUserRequestModel(){

    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSong() {
        return userSong;
    }

    public void setUserSong(String userSong) {
        this.userSong = userSong;
    }

}
