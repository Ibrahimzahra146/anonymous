package com.example.rabee.breath.Models.ResponseModels;

import com.example.rabee.breath.Models.UserModel;

/**
 * Created by Rabee on 1/22/2018.
 */
         //GeneralUserInfoModel
public class UserProfileResponseModel {
    UserModel user;
    AboutUserResponseModel aboutUser;
    int numberOfFollower ;
    int numberOfFollowing;

    public int getNumberOfFollower() {
        return numberOfFollower;
    }

        public void setNumberOfFollower(int numberOfFollower) {
        this.numberOfFollower = numberOfFollower;
    }

    public int getNumberOfFollowing() {
        return numberOfFollowing;
    }

    public void setNumberOfFollowing(int numberOfFollowing) {
        this.numberOfFollowing = numberOfFollowing;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
        }

    public AboutUserResponseModel getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(AboutUserResponseModel aboutUser) {
        this.aboutUser = aboutUser;
    }
}