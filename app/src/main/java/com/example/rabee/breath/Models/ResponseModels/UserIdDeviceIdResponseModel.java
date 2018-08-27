package com.example.rabee.breath.Models.ResponseModels;

import com.example.rabee.breath.Models.DeviceTokenModel;
import com.example.rabee.breath.Models.UserModel;

/**
 * Created by Rabee on 1/22/2018.
 */
 public class UserIdDeviceIdResponseModel {
    UserModel userModel;
    DeviceTokenModel deviceTokenModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public DeviceTokenModel getDeviceTokenModel() {
        return deviceTokenModel;
    }

    public void setDeviceTokenModel(DeviceTokenModel deviceTokenModel) {
        this.deviceTokenModel = deviceTokenModel;
    }
}
