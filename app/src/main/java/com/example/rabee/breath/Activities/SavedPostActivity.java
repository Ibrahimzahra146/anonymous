package com.example.rabee.breath.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SavedPostActivity extends AppCompatActivity {
    Retrofit retrofit;
    public static List<PostCommentResponseModel> postResponseModelsList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout noFriendsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();

        setContentView(R.layout.activity_saved_post);
        PostInterface postInterface;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
       // noFriendsLayout = (LinearLayout) findViewById(R.id.no_friends_Layout);

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).
                        client(GeneralInfo.getClient(getApplicationContext())).build();
        postInterface = retrofit.create(PostInterface.class);
        final Call<List<PostCommentResponseModel>> postResponse = postInterface.getSavedPost(GeneralInfo.getUserID());
        postResponse.enqueue(new Callback<List<PostCommentResponseModel>>() {

            @Override
            public void onResponse(Call<List<PostCommentResponseModel>> call, Response<List<PostCommentResponseModel>> response) {
                postResponseModelsList = response.body();
                //ensure that still in homefragment
                if (this!= null) {

                    recyclerView.setAdapter(new HomePostAdapter(getApplicationContext(), postResponseModelsList));
                    if (postResponseModelsList.size() == 0) {
                        //noFriendsLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        recyclerView.setAdapter(new HomePostAdapter(getApplication(), postResponseModelsList));
                        progressBar.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(Call<List<PostCommentResponseModel>> call, Throwable t) {

            }
        });

    }


}
