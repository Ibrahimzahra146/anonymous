package com.example.rabee.breath.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.PostRequestModel;
import com.example.rabee.breath.Models.ResponseModels.PostResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rabee.breath.Services.ImageService.RotateBitmap;
import static com.example.rabee.breath.Services.ImageService.scaleDown;

public class AddPostActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int SELECTED_PICTURE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String api_key = "AIzaSyAa3QEuITB2WLRgtRVtM3jZwziz9Fc5EV4";
    static int ReceiverID;
    TextView toolbarText;
    static Bitmap imagePost = null;
    static boolean isThereIsImage = false;
    static String path;
    String youtubeLinkString = "";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public String video_id;
    Switch showComments;
    YouTubePlayerView youTubePlayerView;
    int youtubeFlag = 0;
    EditText PostText;
    TextView username;
    CircleImageView userProfilePicture;
    ImageView PostImage, AddImage, sendPost;//, deletePostYoutube;
    ImageButton deletePostImage ,deletePostYoutube;
    static ProgressBar progressBar;

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    public static void setRecieverID(int ID) {
        ReceiverID = ID;
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube);
        youTubePlayerView.setVisibility(View.GONE);
        PostText = (EditText) findViewById(R.id.postText);
        sendPost = (ImageView) findViewById(R.id.sendPost);
        PostImage = (ImageView) findViewById(R.id.showPhoto);
        AddImage = (ImageView) findViewById(R.id.addPhoto);
        showComments = (Switch) findViewById(R.id.showComments);
        deletePostImage = (ImageButton) findViewById(R.id.btn_close);
        deletePostYoutube = (ImageButton) findViewById(R.id.closeYoutubeIcon);
        progressBar= (ProgressBar) findViewById(R.id.postProgressBar);

        deletePostYoutube.setVisibility(View.INVISIBLE);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        username=(TextView) findViewById(R.id.username);
        username.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name()+ " "+GeneralInfo.getGeneralUserInfo().getUser().getLast_name());
        userProfilePicture = ( CircleImageView) findViewById(R.id.userProfilePicture);
        String imageUrl = GeneralInfo.SPRING_URL + "/" + GeneralInfo.getGeneralUserInfo().getUser().getImage();
        //   progressBar.setVisibility(View.INVISIBLE);
        Picasso.with(getApplicationContext()).load(imageUrl).into(userProfilePicture);
//        Log.d("Duration", " " +progressBar.getScrollBarFadeDuration());
//        progressBar.setScrollBarFadeDuration(1000);


        PostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pattern = "https://m.youtube.com/watch?v=";
                String pattern1="https://www.youtube.com/watch?v=";
                String pattern2="https://youtu.be/";
                String s = String.valueOf(PostText.getText());
                int i = s.indexOf(pattern);
                int j = s.indexOf(pattern1);
                int k = s.indexOf(pattern2);

                if ((i == 0||j==0||k==0) && youtubeFlag == 0 && !isThereIsImage) {


                    String[] split = s.split("v=");
                    youtubeLinkString = s;
                    if(split.length==1)
                    {
                        split = s.split(".be/");
                    }
                    video_id = split[1];
                    youTubePlayerView.initialize(api_key, AddPostActivity.this);
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    deletePostYoutube.setVisibility(View.VISIBLE);

                    youTubePlayerView.initialize(api_key, AddPostActivity.this);
                    youtubeFlag = 1;
                } else if ((i == -1||j==-1||k==-1)&& youtubeFlag == 0) {
                    youtubeLinkString = s;
                    youTubePlayerView.setVisibility(View.INVISIBLE);


                }

            }


        });


        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThereIsImage == true || (!PostText.getText().toString().trim().equals("") || !youtubeLinkString.equals(""))){
                    progressBar.setVisibility(View.VISIBLE);}
                AddNewPost();

            }
        });
        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(AddPostActivity.this);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);
            }
        });
        deletePostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostImage.setImageBitmap(null);
                deletePostImage.setVisibility(View.INVISIBLE);
                isThereIsImage = false;
            }
        });

        deletePostYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.setVisibility(View.INVISIBLE);
                deletePostYoutube.setVisibility(View.INVISIBLE);
                youtubeFlag = 0;
                youtubeLinkString = "";

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECTED_PICTURE) {
            Uri imageuri = data.getData();
            try {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                path = generalFunctions.getRealPathFromURI(this, imageuri);

                Bitmap bitmap = setPostImage(imageuri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);

                int newWidth = (int) (bitmap.getWidth());
                int newHeight = (int) (bitmap.getHeight());
                if (newHeight >= 500) {
                    if (newWidth > newHeight) {
                        double scale = 920.0 / newWidth;
                        newWidth = (int) (newWidth * scale);
                        newHeight = (int) (newHeight * scale);
                    }
                } else {
                    double scale = 1500 / newWidth;
                    newWidth = (int) (newWidth * scale);
                    newHeight = (int) (newHeight * scale);
                }
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                youTubePlayerView.setVisibility(View.INVISIBLE);
                PostImage.setImageBitmap(resized);
                deletePostImage.setVisibility(View.VISIBLE);
                deletePostYoutube.setVisibility(View.INVISIBLE);
                imagePost = resized;
                isThereIsImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void AddNewPost() {
        //   progressBar.setVisibility(View.VISIBLE);

        if (isThereIsImage == true) {
            //   progressBar.setVisibility(View.VISIBLE);
            AddNewImagePost();
        } else if (!PostText.getText().toString().trim().equals("") || !youtubeLinkString.equals("")) {
            //  progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
            PostInterface Postservice;
            Postservice = retrofit.create(PostInterface.class);

            PostRequestModel postRequestModel = new PostRequestModel();
            postRequestModel.setUserId(GeneralInfo.getGeneralUserInfo().getUser().getId());
            postRequestModel.setText(PostText.getText().toString());
            postRequestModel.setLink("");
            postRequestModel.setIs_public_comment(showComments.isChecked());


            Call<PostResponseModel> PostRespone = Postservice.addNewPost(postRequestModel);
            PostRespone.enqueue(new Callback<PostResponseModel>() {
                @Override
                public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                   // startActivity(i);
                    onBackPressed();
                }

                @Override
                public void onFailure(Call<PostResponseModel> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddPostActivity.this, "Oops! Something went wrong, please try again.",
                            Toast.LENGTH_SHORT).show();
                }

            });
        }

    }


    public void AddNewImagePost() {
        isThereIsImage = false;
        File file = new File(path);
        final GeneralFunctions generalFunctions = new GeneralFunctions();

        file = generalFunctions.saveBitmap(imagePost, path);
        Log.d("file", path);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext())).build();
        PostInterface Postservice;
        Postservice = retrofit.create(PostInterface.class);
        Postservice = retrofit.create(PostInterface.class);

        PostRequestModel postRequestModel = new PostRequestModel();
        postRequestModel.setUserId(GeneralInfo.getGeneralUserInfo().getUser().getId());
        postRequestModel.setText(PostText.getText().toString() + "::" + youtubeLinkString);
        postRequestModel.setIs_public_comment(showComments.isChecked());

        Call<PostResponseModel> PostRespone = Postservice.addNewPost(body, postRequestModel.getUserId(), postRequestModel.getText(), postRequestModel.getIs_public_comment());
        PostRespone.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, Response<PostResponseModel> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddPostActivity.this, "Oops! Something went wrong, please try again.",
                        Toast.LENGTH_SHORT).show();
                Log.d("ImagePost", t.getMessage());
            }

        });


    }

    public Bitmap setPostImage(Uri imageuri) throws IOException {
        GeneralFunctions generalFunctions = new GeneralFunctions();
        String path = generalFunctions.getRealPathFromURI(this, imageuri);
        int rotate = generalFunctions.getPhotoOrientation(path);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        bitmap = scaleDown(bitmap, 3000, true);
        bitmap = RotateBitmap(bitmap, rotate);

        return bitmap;

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.cueVideo(video_id);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

}