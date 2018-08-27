package com.example.rabee.breath.Activities;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.Adapters.UserListAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.FollowingInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowingActivity extends AppCompatActivity {
    public static ArrayList<UserModel> userModelList = new ArrayList<>();
    Retrofit retrofit;
    ObjectAnimator anim;

    public RecyclerView recyclerView;
    FollowingInterface followingInterface;
    ProgressBar progressBar;
    TextView following_label,toolbarText;
    LinearLayout noFollowingLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).
                        client(GeneralInfo.getClient(getApplicationContext())).build();
        setContentView(R.layout.activity_following);
        progressBar= (ProgressBar) findViewById(R.id.ProgressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        following_label = (TextView) findViewById(R.id.following_label);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        noFollowingLayout = (LinearLayout) findViewById(R.id.no_friends_Layout);
        following_label.setText("Following");
        progressBar.setProgress(0);
        progressBar.setMax(100);
        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        followingInterface=retrofit.create(FollowingInterface.class);
        final Call<List<UserModel>> followingResponse= followingInterface.getFollowing(GeneralInfo.getUserID());
        followingResponse.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                userModelList = (ArrayList<UserModel>) response.body();
                progressBar.setVisibility(View.GONE);

                if (userModelList != null && userModelList.size() == 0)
                {
                    noFollowingLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    recyclerView.setAdapter(new UserListAdapter(getApplicationContext(),userModelList));

                }

            }



            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {

            }
        });
        toolbarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if (event.getX() <= (toolbarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 30)) {
                    //finish();
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });

    }
//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        this.onCreate(null);
//    }
}
