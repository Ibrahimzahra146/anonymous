package com.example.rabee.breath.RequestInterface;


import com.example.rabee.breath.Models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by exalt on 7/5/2017.
 */

public interface ImageInterface {
    @Headers("Cache-Control: max-age=64000")
    @Multipart
    @POST("/api/v1/uploadFile")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @Multipart
    @POST("/api/v1/uploadProfilePic")
    Call<UserModel> uploadProfileImage(@Part MultipartBody.Part file, @Query("id") int id);

    @Multipart
    @POST("/api/v1/uploadCoverPic")
    Call<UserModel> uploadCoverImage(@Part MultipartBody.Part file, @Query("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/user/removeImage/{id}/{profileOrCover}")
    Call<Integer> removeImage(@Path("id") int id, @Path("profileOrCover") int profileOrCover);

}