package com.example.rabee.breath.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rabee.breath.Adapters.UserListAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ReactSingleModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReactActivity extends AppCompatActivity {
    public static ArrayList<UserModel> userModelList = new ArrayList<>();

    public RecyclerView recyclerView;
    ProgressBar progressBar;
    PostInterface postInterface;
    TextView reactTypeLabel,backIcon;
    LinearLayout noReactLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_react);
        Bundle b = getIntent().getExtras();
        int postId = 0;
        int type = 0;
        if (b != null) {
            postId = b.getInt("postId");
            type = b.getInt("type");
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        noReactLayout = (LinearLayout) findViewById(R.id.no_react_Layout);
        reactTypeLabel = (TextView) findViewById(R.id.react_type_label);
        backIcon=(TextView)findViewById(R.id.back_icon);
        if (type == 3) {
            reactTypeLabel.setText("Love Reacts");
        } else if (type == 1) {
            reactTypeLabel.setText("Like Reacts");
        } else if (type == 2) {
            reactTypeLabel.setText("Dislike Reacts");
        }
        backIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if (event.getX() <= (backIcon.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 30)) {
                    //finish();
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();

        postInterface = retrofit.create(PostInterface.class);
        final Call<ReactSingleModel> reactResponse = postInterface.getPostReact(postId, type);
        reactResponse.enqueue(new Callback<ReactSingleModel>() {
            @Override
            public void onResponse(Call<ReactSingleModel> call, Response<ReactSingleModel> response) {
                userModelList = (ArrayList<UserModel>) response.body().getUsers();
                progressBar.setVisibility(View.GONE);
                if(userModelList!= null && userModelList.size()==0)
                {
                    noReactLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new UserListAdapter(getApplicationContext(), userModelList));
              }

            }

            @Override
            public void onFailure(Call<ReactSingleModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });


    }
}
