package com.example.rabee.breath.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.rabee.breath.Adapters.DirectSignUpHomePostsAdapter;
import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.SignOutRequestModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AuthInterface;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.facebook.login.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OneTimeLogInActivity extends Activity{

    RecyclerView recyclerView ;
    public static List<PostCommentResponseModel> postResponseModelsList;
    LinearLayout searchLayout;
    ProgressBar progressBar;
    ImageView logOut;
    // Add the search bar ---------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_time_login_activity);
        logOut = (ImageView) findViewById(R.id.logOut);
        searchLayout = (LinearLayout) findViewById(R.id.SearchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });

        PostInterface postInterface;
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view1);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        postInterface = retrofit.create(PostInterface.class);


        //Change the API
        progressBar.setVisibility(View.VISIBLE);
        final Call<List<PostCommentResponseModel>> postResponse = postInterface.getRandomPosts(GeneralInfo.getUserID(),0 );
        postResponse.enqueue(new Callback<List<PostCommentResponseModel>>() {

            @Override
            public void onResponse(Call<List<PostCommentResponseModel>> call, Response<List<PostCommentResponseModel>> response) {
                Log.d("DirectSignUp", response.code()+ " " );
                postResponseModelsList = response.body();
                recyclerView.setAdapter(new DirectSignUpHomePostsAdapter(getApplicationContext(),postResponseModelsList));
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<PostCommentResponseModel>> call, Throwable t) {

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

            }
        });



    }


    public void logout() {

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AuthInterface logoutApi = retrofit.create(AuthInterface.class);

        SignOutRequestModel signOutModel = new SignOutRequestModel();
        signOutModel.setDeviceId(android_id);
        signOutModel.setUserId(GeneralInfo.getUserID());
        Call<Integer> logOutnResponse = logoutApi.signOut(signOutModel);

        logOutnResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    Log.d("SignOut", " " + response.code());
                    SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //.activity.finish();

//                ActivityCompat.finishAffinity((Activity) context);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("Fail", t.getMessage());
            }

        });
    }
}
