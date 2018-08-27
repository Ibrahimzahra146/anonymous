package com.example.rabee.breath.Models.ResponseModels;

import com.example.rabee.breath.Models.UserModel;

/**
 * Created by Rabee on 1/22/2018.
 */

public class FollowingRelationResponseModel {
    int id;
    UserModel friend1_id;
    UserModel friend2_id;
    int state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getFriend1_id() {
        return friend1_id;
    }

    public void setFriend1_id(UserModel friend1_id) {
        this.friend1_id = friend1_id;
    }

    public UserModel getFriend2_id() {
        return friend2_id;
    }

        public void setFriend2_id(UserModel friend2_id) {
        this.friend2_id = friend2_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
