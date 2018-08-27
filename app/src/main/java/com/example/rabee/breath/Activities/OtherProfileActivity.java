package com.example.rabee.breath.Activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabee.breath.Adapters.DirectSignUpHomePostsAdapter;
import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ResponseModels.AboutUserResponseModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingRelationResponseModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingResponseModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AboutUserInterface;
import com.example.rabee.breath.RequestInterface.FollowingInterface;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.example.rabee.breath.Services.FollowingService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherProfileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Retrofit  retrofit;
    public static ObjectAnimator anim,anim_button;
    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView img,showOtherSong;
    Dialog ViewImgDialog,AboutFriendDialog,ConfirmDeletion;
    TextView about_status, about_bio,user_profile_name,toolBarText,aboutUsername,editBio,textMsg;
    ImageView imageView,aboutFriendIcon,coverPhoto,otherFollowState;
    Button friendStatus,NoBtn,YesBtn,myFollowState;
    private ProgressBar progressBar,progressBar_button;
    int Id1, Id = -1; // or other values
    String mName = "",mImageUrl = "",youtubeSongUrl;
    UserModel userProfileModel;
    LinearLayout FollowLinearlayout;
    SharedPreferences sharedPreferences;
    RecyclerView postRecyclerView;
    PostInterface postInterface;
    public static List<PostCommentResponseModel> postResponseModelsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
        setContentView(R.layout.activity_other_profile);
        Bundle b = getIntent().getExtras();

        if (b != null) {
            Id = b.getInt("Id");
            mName = b.getString("mName");
            mImageUrl = b.getString("mImageURL");
        }
        Id1 = Id;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        toolBarText = (TextView) findViewById(R.id.ToolbarText);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        showOtherSong = (CircleImageView) findViewById(R.id.showSong);
        friendStatus = (Button) findViewById(R.id.friendStatus);
        aboutFriendIcon = (ImageView) findViewById(R.id.aboutFriendIcon);
        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
        myFollowState = (Button) findViewById(R.id.myFollowState);
        otherFollowState = (ImageView) findViewById(R.id.otherFollowState);
        progressBar_button = (ProgressBar) findViewById(R.id.progressBar_button);
        progressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);
        img = (CircleImageView) findViewById(R.id.user_profile_photo);
        editBio = (TextView) findViewById(R.id.editBio);
        FollowLinearlayout=(LinearLayout) findViewById(R.id.FollowLinearLayout);
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String loginType=sharedPreferences.getString("loginType", "");
        postRecyclerView = (RecyclerView)findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        postRecyclerView.hasFixedSize();


        if(loginType.equals("DIRECT_SIGNUP"))
        {
            FollowLinearlayout.setVisibility(View.GONE);
            progressBar_button.setVisibility(View.GONE);
            friendStatus.setVisibility(View.GONE);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
        postInterface = retrofit.create(PostInterface.class);
        final Call<List<PostCommentResponseModel>> postResponse = postInterface.getUserHomePost(GeneralInfo.getUserID(),0);
        postResponse.enqueue(new Callback<List<PostCommentResponseModel>>() {

            @Override
            public void onResponse(Call<List<PostCommentResponseModel>> call, Response<List<PostCommentResponseModel>> response) {
                postResponseModelsList = response.body();

                if(loginType.equals("DIRECT_SIGNUP")) {
                    postRecyclerView.setAdapter(new DirectSignUpHomePostsAdapter(getApplicationContext(),postResponseModelsList));
                }
                else
                {
                    postRecyclerView.setAdapter(new HomePostAdapter(getApplicationContext(),postResponseModelsList));
                }

            }

            @Override
            public void onFailure(Call<List<PostCommentResponseModel>> call, Throwable t) {

            }
        });



        //Set user info
        user_profile_name.setText(mName);
        toolBarText.setText(mName);
        friendStatus.setText(" ");
        String imageUrl = GeneralInfo.SPRING_URL + "/" + mImageUrl;
        Picasso.with(getApplicationContext()).load(imageUrl).into(img);
        ///////////////////////
        progressBar_button.setProgress(0);
        progressBar_button.setMax(100);
        anim_button = ObjectAnimator.ofInt(progressBar_button, "progress", 0, 100);
        anim_button.setDuration(1000);
        anim_button.setInterpolator(new DecelerateInterpolator());
        anim_button.start();
        //progressBar.setProgress(0);
        //progressBar.setMax(100);
        //progressBar.setVisibility(View.INVISIBLE);
        anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        ConfirmDeletion = new Dialog(OtherProfileActivity.this);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_following_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);
        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);
        GeneralFunctions generalFunctions = new GeneralFunctions();
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());

        if (isOnline == false) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar_button.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            getOtherProfileView();
            ViewImgDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            ViewImgDialog.setContentView(R.layout.view_profilepic_dialog);
            imageView = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
            AboutFriendDialog = new Dialog(this);
            AboutFriendDialog.setContentView(R.layout.about_other_dialog);
            AboutFriendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            about_bio = (TextView) AboutFriendDialog.findViewById(R.id.Bio);
            about_status = (TextView) AboutFriendDialog.findViewById(R.id.status);
            aboutUsername = (TextView) AboutFriendDialog.findViewById(R.id.aboutUsername);
            aboutUsername.setText("About " + mName);
            //Listenrs
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (userProfileModel.getIsProfileImagePublic().equals("true")) {
                        imageView.setImageDrawable(img.getDrawable());
                        ViewImgDialog.show();
                    }
                }
            });

            coverPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    imageView.setImageDrawable(coverPhoto.getDrawable());
                    ViewImgDialog.show();
                }
            });
            aboutFriendIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AboutFriendDialog.show();
                }
            });
            showOtherSong.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int flag = 0;
                    String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                    Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = compiledPattern.matcher(youtubeSongUrl);
                    if (matcher.find()) {
                        flag = 1;
                    }

                    if (flag == 1) {
                        Intent i = new Intent(getApplicationContext(), YoutubeDialogActivity.class);
                        Bundle b = new Bundle();
                        b.putString("youtubeSongUrl", youtubeSongUrl);
                        i.putExtras(b);
                        startActivity(i);
                    } else {
                        Toast.makeText(OtherProfileActivity.this, "No song", Toast.LENGTH_SHORT);
                    }

                }
            });
            toolBarText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    if (event.getX() <= (toolBarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }






    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOtherProfileView();
                swipeRefreshLayout.setRefreshing(false);


            }
        }, 1000);

    }
    public void getOtherProfileView() {
        //progressBar.setVisibility(View.VISIBLE);


        FollowingInterface getFreindApi = retrofit.create(FollowingInterface.class);
        Call<FollowingRelationResponseModel> call = getFreindApi.getFollowRelationState(GeneralInfo.getUserID(), Id);
        final Integer[] FollowRelationState = new Integer[2];

        final String finalMName = mName;
        call.enqueue(new Callback<FollowingRelationResponseModel>() {
            @Override
            public void onResponse(Call<FollowingRelationResponseModel> call, final Response<FollowingRelationResponseModel> response) {
                if (response.code() == 200) {
                    FollowRelationState[0] = response.body().getState();
                    userProfileModel = response.body().getFriend2_id();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar_button.setVisibility(View.INVISIBLE);
                    if (FollowRelationState[0] == 0) {
                        // No relation
                        GeneralInfo.friendMode = 0;
                        FollowingService.setFollowRelationState(friendStatus, OtherProfileActivity.this, userProfileModel, getApplicationContext());

                    } else if (FollowRelationState[0] == 1) { // Follow Request Pending
                        GeneralInfo.friendMode = 1;
                        FollowingService.setFollowRelationState(friendStatus, OtherProfileActivity.this, userProfileModel, getApplicationContext());
                    }




                    fillAbout(about_bio, about_status, Id1);


                }
            }

            @Override
            public void onFailure(Call<FollowingRelationResponseModel> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar_button.setVisibility(View.INVISIBLE);
                Log.d("OtherActivityProfile", " Error" + t.getMessage());

            }
        });

    }
    public void fillAbout(final TextView bio, final TextView status, final int ID) {

        AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);
        Call<AboutUserResponseModel> call = aboutUserApi.getAboutUser(ID);
        call.enqueue(new Callback<AboutUserResponseModel>() {
            @Override
            public void onResponse(Call<AboutUserResponseModel> call, Response<AboutUserResponseModel> response) {

                if (response != null) {
                    if (response.body() != null) {

                        editBio.setText(response.body().getUserBio());
                        bio.setText(response.body().getUserBio());
                        status.setText(response.body().getUserStatus());
                        youtubeSongUrl = response.body().getUserSong();
                    }
                }
            }

            @Override
            public void onFailure(Call<AboutUserResponseModel> call, Throwable t) {
            }
        });
    }
}
