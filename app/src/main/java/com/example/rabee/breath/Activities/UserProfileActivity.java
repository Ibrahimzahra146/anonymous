package com.example.rabee.breath.Activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.AboutUserRequestModel;
import com.example.rabee.breath.Models.ResponseModels.AboutUserResponseModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AboutUserInterface;
import com.example.rabee.breath.RequestInterface.ImageInterface;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.example.rabee.breath.Services.ImageService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rabee.breath.Services.ImageService.RotateBitmap;
import static com.example.rabee.breath.Services.ImageService.scaleDown;

public class UserProfileActivity extends AppCompatActivity {
    private static final int SELECTED_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static ObjectAnimator anim;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SharedPreferences sharedPreferences;
    Retrofit retrofit;
    public static List<PostCommentResponseModel> postResponseModelsList;

    ImageView img, coverImage, imageView;
    TextView followingTxt, newPostTxt, followerCount, followingCount, userName, profileBio , editProfile;
    TextView changePic, viewPic, RemovePic, toolBarText, addPost;
    static TextView postCount;
    CircleImageView  editSong;
    Button saveAbout, saveSong;
    EditText bioTxt, statusTxt, songTxt;
    Dialog imgClick, ViewImgDialog, editMyBio, editMySong;
    ProgressBar coverProgressBar, progressBar;
    Uri imageuri;
    ImageView editBio;
    RecyclerView recyclerView;
    LinearLayout noFriendsLayout;


    String songUrl;
    int youtubeFlag = 0;
    //    TextView followerTxt;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext())).build();
        img = (ImageView) findViewById(R.id.user_profile_photo);
        coverImage = (ImageView) findViewById(R.id.coverImage);
        userName = (TextView) findViewById(R.id.user_profile_name);
        editProfile = (TextView) findViewById(R.id.editProfile);
        editSong = (CircleImageView) findViewById(R.id.editSong);
        coverImage = (ImageView) findViewById(R.id.coverImage);
        profileBio = (TextView) findViewById(R.id.profileBio);
        addPost = (TextView) findViewById(R.id.addPost);
        postCount = (TextView) findViewById(R.id.post_count);
        coverProgressBar = (ProgressBar) findViewById(R.id.coverProgressBar);
        img = (ImageView) findViewById(R.id.user_profile_photo);
        // songTxt = (EditText) editMySong.findViewById(R.id.songTxt);
        // saveSong = (Button) editMySong.findViewById(R.id.saveSong);
        editBio = (ImageView) findViewById(R.id.editBio);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        progressBar = (ProgressBar) findViewById(R.id.profilePictureProgressBar);
        followingCount = (TextView) findViewById(R.id.following_count);
        followerCount = (TextView) findViewById(R.id.followers_count);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        getUserPosts();
        ////////////////////////////////////////
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());
        String imageUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.getGeneralUserInfo().getUser().getImage();
        String coverUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.getGeneralUserInfo().getUser().getCover_image();
        Picasso.with(getApplicationContext()).load(imageUrl).into(img);
        Picasso.with(getApplicationContext()).load(coverUrl).into(coverImage);
        if (GeneralInfo.getGeneralUserInfo().getAboutUser().getUserBio().equals("")) {
            profileBio.setText("Nothing to show");

        } else {
            profileBio.setText(GeneralInfo.getGeneralUserInfo().getAboutUser().getUserBio());

        }
        followingCount.setText(String.valueOf(GeneralInfo.getGeneralUserInfo().getNumberOfFollowing()));
        followerCount.setText(String.valueOf(GeneralInfo.getGeneralUserInfo().getNumberOfFollower()));
        //animation
        ///////////////
        imgClick = new Dialog(this);
        ViewImgDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        editMyBio = new Dialog(this);
        editMySong = new Dialog(this);


        imgClick.setContentView(R.layout.profile_picture_dialog);
        ViewImgDialog.setContentView(R.layout.view_profilepic_dialog);
        editMyBio.setContentView(R.layout.edit_my_bio_dialog);
        editMyBio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editMySong.setContentView(R.layout.edit_song_profile_dialog);
        editMySong.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /////
        imageView = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
        saveAbout = (Button) editMyBio.findViewById(R.id.saveAbout);
        bioTxt = (EditText) editMyBio.findViewById(R.id.bioTxt);
        statusTxt = (EditText) editMyBio.findViewById(R.id.statusTxt);
        songTxt = (EditText) editMySong.findViewById(R.id.songTxt);
        saveSong = (Button) editMySong.findViewById(R.id.saveSong);
        //editBio = (TextView) findViewById(R.id.editBio);
        toolBarText = (TextView) findViewById(R.id.toolBarText);
        fillAbout();
        Log.d("Song url", songUrl);

        //Listens
        //Listener for profile picture and it's dialog
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgClick.getWindow().getAttributes().y = -90;
                imgClick.getWindow().getAttributes().x = 130;
                imgClick.show();
                changePic = (TextView) imgClick.findViewById(R.id.EditPic);
                changePic.setText("Change Profile Picture");
                viewPic = (TextView) imgClick.findViewById(R.id.ViewPic);
                viewPic.setText("View Profile Picture");
                RemovePic = (TextView) imgClick.findViewById(R.id.RemovePic);
                RemovePic.setText("Remove Profile Picture");
                changePic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        verifyStoragePermissions(UserProfileActivity.this);
                        imgClick.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 100);
                    }
                });

                viewPic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imgClick.dismiss();
                        imageView.setImageDrawable(img.getDrawable());
                        ImageView coverImageDialog = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
                        coverImageDialog.setImageDrawable(img.getDrawable());
                        ViewImgDialog.show();

                    }
                });

                RemovePic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imgClick.dismiss();
                        removeImage(0);/////////


                    }
                });
            }
        });
        //Listener for cover image
        coverImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgClick.getWindow().getAttributes().y = -280;
                imgClick.getWindow().getAttributes().x = 30;
                imgClick.show();
                changePic = (TextView) imgClick.findViewById(R.id.EditPic);
                changePic.setText("Change Cover Picture");
                viewPic = (TextView) imgClick.findViewById(R.id.ViewPic);
                viewPic.setText("View Cover Picture");
                RemovePic = (TextView) imgClick.findViewById(R.id.RemovePic);
                RemovePic.setText("Remove Cover Picture");

                changePic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        verifyStoragePermissions(UserProfileActivity.this);

                        imgClick.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 200);
                    }
                });

                viewPic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imgClick.dismiss();
                        coverImage.setImageDrawable(coverImage.getDrawable());
                        ImageView coverImageDialog = (ImageView) ViewImgDialog.findViewById(R.id.ImageView);
                        coverImageDialog.setImageDrawable(coverImage.getDrawable());
                        ViewImgDialog.show();
                    }
                });

                RemovePic.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imgClick.dismiss();
                        removeImage(1);

                    }
                });
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

                if (event.getX() >= 900) {
                    return true;
                }


                return false;
            }
        });
//        followingTxt.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent i = new Intent(getApplicationContext(), FollowingActivity.class);
//                startActivity(i);
//            }
//        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
              startActivity(i);
            }
        });

        editSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserYoutubeActivity.class);
                Bundle b = new Bundle();
                Log.d("Song url", songUrl);
                if (songUrl != null) {
                    b.putString("youtubeSongUrl", GeneralInfo.getGeneralUserInfo().getAboutUser().getUserSong());
                }
                i.putExtras(b);
                startActivity(i);

            }
        });
        editBio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fillAbout();
                editMyBio.show();


//                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
//                startActivity(i);
            }
        });

        saveAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAbout.setPressed(true);
                String bioText, statusText, songText;
                bioText = bioTxt.getText().toString();
                songText = songTxt.getText().toString();
                statusText = statusTxt.getText().toString();
                updateAbout(bioText, statusText, songText);
                editMyBio.dismiss();

            }
        });

//        addPost.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                addPost.setPressed(true);
//                return true;
//            }
//        });

        addPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPost.setPressed(true);
                Intent i = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(i);
            }
        });


    }

    public void getUserInfo() {
        userName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());
        String imageUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.getGeneralUserInfo().getUser().getImage();
        Picasso.with(getApplicationContext()).load(imageUrl).into(img);

        String coverUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.getGeneralUserInfo().getUser().getCover_image();
        Picasso.with(getApplicationContext()).load(coverUrl).into(coverImage);


    }

    public void fillAbout() {

        profileBio.setText(GeneralInfo.getGeneralUserInfo().getAboutUser().getUserBio());
        bioTxt.setText(GeneralInfo.getGeneralUserInfo().getAboutUser().getUserBio());
        statusTxt.setText(GeneralInfo.getGeneralUserInfo().getAboutUser().getUserStatus());
        songTxt.setText(GeneralInfo.getGeneralUserInfo().getAboutUser().getUserSong());
        songUrl = GeneralInfo.getGeneralUserInfo().getAboutUser().getUserSong();
        Log.d("Song url", songUrl);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == 100 || requestCode == 200)) {
            imageuri = data.getData();
            try {
                ImageService imageService = new ImageService();
                String path = imageService.getRealPathFromURI(this, imageuri);
                int rotate = imageService.getPhotoOrientation(path);
                sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
                bitmap = scaleDown(bitmap, 1000, true);
                bitmap = RotateBitmap(bitmap, rotate);
                if (requestCode == 100) {
                    img.setImageBitmap(bitmap);
                   // GeneralInfo.generalUserInfo.getUser().setImage(path);

                }
                if (requestCode == 200) {
                    coverImage.setImageBitmap(bitmap);
                   // GeneralInfo.generalUserInfo.getUser().setCover_image(path);

                }
                this.onCreate(null);
                byte[] image = imageService.getBytes(bitmap);
//                String encodedImage = Base64.encodedImagencodeToString(image, Base64.DEFAULT);
                imageService.uploadImagetoDB(GeneralInfo.getUserID(), path, bitmap, requestCode, coverProgressBar);

                Gson gson = new Gson();
                String json = gson.toJson(GeneralInfo.generalUserInfo);
                editor.putString("generalUserInfo", json);
                editor.apply();

            } catch (Exception e) {
                Log.d("XX", "Image cannot be uploaded");
            }

        }
    }

    //Update user info
    public void updateAbout(final String bioText, final String statusText, final String songText) {
        AboutUserRequestModel aboutUserModel = new AboutUserRequestModel(GeneralInfo.getUserID(), bioText, statusText, songText);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext())).build();
        AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);

        Call<AboutUserResponseModel> call = aboutUserApi.addNewAboutUser(aboutUserModel);
        call.enqueue(new Callback<AboutUserResponseModel>() {
            @Override
            public void onResponse(Call<AboutUserResponseModel> call, Response<AboutUserResponseModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralInfo generalFunctions = new GeneralInfo();
                    // generalFunctions.showErrorMesaage(getApplicationContext());
                } else {
                    GeneralInfo.generalUserInfo.setAboutUser(response.body());
                    sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(GeneralInfo.generalUserInfo);
                    editor.putString("generalUserInfo", json);
                    editor.apply();
                    fillAbout();
                }
            }

            @Override
            public void onFailure(Call<AboutUserResponseModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });

    }

    public void removeImage(final int profileOrCover) {


        ImageInterface imageInterface = retrofit.create(ImageInterface.class);
        Call<Integer> removeImageResponse = imageInterface.removeImage(GeneralInfo.userID, profileOrCover);
        removeImageResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (profileOrCover == 0) {
                    GeneralInfo.generalUserInfo.getUser().setImage("");
                } else {
                    GeneralInfo.generalUserInfo.getUser().setCover_image("");
                }
                sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(GeneralInfo.generalUserInfo);
                editor.putString("generalUserInfo", json);
                editor.apply();
                getUserInfo();

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ImagesCode ", " Error " + t.getMessage());
            }
        });

    }

    public void getUserPosts() {
        PostInterface postInterface;
        postInterface = retrofit.create(PostInterface.class);
        final Call<List<PostCommentResponseModel>> postResponse = postInterface.getUserPost(GeneralInfo.getUserID());
        postResponse.enqueue(new Callback<List<PostCommentResponseModel>>() {

            @Override
            public void onResponse(Call<List<PostCommentResponseModel>> call, Response<List<PostCommentResponseModel>> response) {
                postResponseModelsList = response.body();
                recyclerView.setAdapter(new HomePostAdapter(getApplicationContext(), postResponseModelsList));
                if (postResponseModelsList.size() == 0) {

                } else {
                    postCount.setText(postResponseModelsList.size() + "");
                    recyclerView.setAdapter(new HomePostAdapter(getApplicationContext(), postResponseModelsList));
                }


            }

            @Override
            public void onFailure(Call<List<PostCommentResponseModel>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());

    }
}
