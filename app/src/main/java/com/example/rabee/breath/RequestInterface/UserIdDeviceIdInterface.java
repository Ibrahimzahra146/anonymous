package com.example.rabee.breath.RequestInterface;


import com.example.rabee.breath.Models.RequestModels.UserDeviceIdRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserIdDeviceIdResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Rabee on 9/26/2017.
 */

public interface UserIdDeviceIdInterface {
    @POST("/api/v1/deviceToken/saveIdWithDeviceId")
    Call<UserIdDeviceIdResponseModel> storeUserIdWithDeviceId(@Body UserDeviceIdRequestModel userDeviceIdRequestModel);
}
