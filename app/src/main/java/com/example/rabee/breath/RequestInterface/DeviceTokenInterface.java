package com.example.rabee.breath.RequestInterface;

import com.example.rabee.breath.Models.DeviceTokenModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rabee on 6/28/2017.
 */

public interface DeviceTokenInterface {
    @POST("/api/v1/deviceToken/fcm")
    Call<DeviceTokenModel> storeDeviceToken(@Body DeviceTokenModel deviceTokenModel);
}
