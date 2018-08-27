package com.example.rabee.breath.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.AddCommentModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCommentActivity extends AppCompatActivity {
    EditText commentText;
    TextView cancelBtn;
    ImageView sendBtn;
    int postId;
    ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_add_comment);
        Log.d("Raghad hyo zabaet :P","");
        Log.d("AddCommentActivity", " start ");

        commentText = (EditText) findViewById(R.id.commentText);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);
        progressBar = (ProgressBar) findViewById(R.id.postProgressBar);

        Bundle b = getIntent().getExtras();
        postId = b.getInt("postId");


        sendBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressBar.setVisibility(View.VISIBLE);
                        if (commentText.getText().toString().trim().equals("")) {

                        } else {
                            sendComment();
                        }


                    }
                });

        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

    public void sendComment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PostInterface sendComment = retrofit.create(PostInterface.class);
        AddCommentModel addCommentModel = new AddCommentModel();
        addCommentModel.setPostId(postId);
        addCommentModel.setUserId(GeneralInfo.getGeneralUserInfo().getUser().getId());
        addCommentModel.setText(commentText.getText().toString());
        Call<AddCommentModel> addNewReactResponse = sendComment.addComment(addCommentModel);

        addNewReactResponse.enqueue(new Callback<AddCommentModel>() {
            @Override
            public void onResponse(Call<AddCommentModel> call, Response<AddCommentModel> response) {
                Log.d("PostHolder", " Post react addition " + response.code());
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    progressBar.setVisibility(View.INVISIBLE);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddCommentModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("PostHolder", t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);

            }

        });
    }
}
