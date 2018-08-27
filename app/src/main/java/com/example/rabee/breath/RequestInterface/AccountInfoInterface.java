package com.example.rabee.breath.RequestInterface;

import com.example.rabee.breath.Models.RequestModels.EditEmailRequestModel;
import com.example.rabee.breath.Models.RequestModels.EditMobileRequestModel;
import com.example.rabee.breath.Models.RequestModels.EditPasswordRequestModel;
import com.example.rabee.breath.Models.RequestModels.EditPrivacyRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by zodiac on 10/09/2017.
 */

public interface AccountInfoInterface {
    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-email")
    Call<UserProfileResponseModel> editEmail(@Body EditEmailRequestModel editEmailModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-password")
    Call<UserProfileResponseModel> editPassword(@Body EditPasswordRequestModel editPasswordModel);


    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-mobile")
    Call<UserProfileResponseModel> editMobile(@Body EditMobileRequestModel editMobileModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/user/settings/edit-privacy")
    Call<UserProfileResponseModel> editPrivacy(@Body EditPrivacyRequestModel editPrivacyModel);

}

