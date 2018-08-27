package com.example.rabee.breath.RequestInterface;

import com.example.rabee.breath.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Rabee on 1/20/2018.
 */

public interface SearchInterface {
    @GET("/api/v1/serach/{word}")
    Call<List<UserModel>> getSearchResult(@Path("word") String word);
}
