package com.example.rabee.breath.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rabee.breath.Adapters.CommentAdapter;
import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.CommentModel;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.PostInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewPostActivity extends AppCompatActivity {
    RecyclerView commentRecyclerView;
    RecyclerView posttRecyclerView;
    ProgressBar progressBar;
    int postId=0;
    public List<CommentModel> commentModelsList = new ArrayList<>();
    public static List<PostCommentResponseModel> postResponseModelsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        PostInterface postInterface;
//        Bundle b=new Bundle();
//        if(b!=null){
//            postId=b.getInt("postId");
//             Log.d("Bundle ", " " +postId);
//        }
        postId = getIntent().getIntExtra("postId", -1);

        postResponseModelsList= new ArrayList<PostCommentResponseModel>();
        commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentRecyclerView.hasFixedSize();
        posttRecyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);
        posttRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        posttRecyclerView.hasFixedSize();

        progressBar =(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext()))
                .build();
        postInterface = retrofit.create(PostInterface.class);
        Log.d("ActivityAdd","id is " + postId);

        final Call<PostCommentResponseModel> postResponse = postInterface.getPost(postId);
        postResponse.enqueue(new Callback<PostCommentResponseModel>() {

            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {
                postResponseModelsList.add(response.body());
                Log.d("Respone", " " + postResponseModelsList.size());
                commentModelsList=response.body().getComments();
                //ensure that still in same activity
                if (this!= null) {


                    if (postResponseModelsList.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("ActivityAdd"," ==0 "+ response.body().getPost().getText());

                    } else {
                        Log.d("ActivityAdd"," ele "+ response.body().getPost().getText());

                        posttRecyclerView.setAdapter(new HomePostAdapter(getApplication(), postResponseModelsList));
                        progressBar.setVisibility(View.GONE);
                        commentRecyclerView.setAdapter(new CommentAdapter(getApplicationContext(), commentModelsList,response.body().getPost().getUserId()));
                    }
                }


            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {

            }
        });
    }
}
