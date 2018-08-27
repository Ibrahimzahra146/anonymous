package com.example.rabee.breath.RequestInterface;

import com.example.rabee.breath.Models.RequestModels.FollowingRequestModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingRelationResponseModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingResponseModel;
import com.example.rabee.breath.Models.UserModel;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zodiac on 06/29/2017.
 */


public interface FollowingInterface {


    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getFollowers/{id}")
    Call<List<FollowingResponseModel>> getFollowers(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getOtherFollowers/{friend_id}/{id}")
    Call<List<FollowingResponseModel>> getOtherFollowers(@Path("friend_id") int friend_id, @Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getOtherFollowing/{friend_id}/{id}")
    Call<List<FollowingResponseModel>> getOtherFollowing(@Path("friend_id") int friend_id, @Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getFollowing/{id}")
    Call<List<UserModel>> getFollowing(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getFollowRequest/{id}")
    Call<List<FollowingResponseModel>> getFollowRequest(@Path("id") int id);

    @Headers("Cache-Control: max-age=64000")
    @GET("/api/v1/friend/getFollowRelationState/{friend1_id}/{friend2_id}")
    Call<FollowingRelationResponseModel> getFollowRelationState(@Path("friend1_id") int friend1_id, @Path("friend2_id") int friend2_id);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/sendNewFollow")
    Call<FollowingRelationResponseModel> sendNewFollow(@Body FollowingRequestModel friendshipModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/deleteFollow")
    Call<Integer> deleteFollow(@Body FollowingRequestModel friendshipModel);

    @Headers("Cache-Control: max-age=64000")
    @POST("/api/v1/friend/confirmFollowRequest")
    Call<Integer> confirmFriendship(@Body FollowingRequestModel friendshipModel);


}