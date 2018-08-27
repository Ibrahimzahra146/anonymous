package com.example.rabee.breath.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.rabee.breath.Adapters.UserListAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.SearchInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    public static ArrayList<UserModel> userModelList = new ArrayList<>();
    public RecyclerView recyclerView;
    public SearchView mSearchView;
    SearchInterface searchInterface;
    ProgressBar searchProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchProgressBar= (ProgressBar) findViewById(R.id.searchProgressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final SearchView mSearchView = (SearchView) findViewById(R.id.search);
        searchProgressBar = (ProgressBar) findViewById(R.id.searchProgressBar);

        //not expanded when clicked.
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        //Handle events in serchView
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mSearchView.getQuery().length() == 0) {
                    userModelList.clear();
                    recyclerView.setAdapter(new UserListAdapter(getApplicationContext(), userModelList));
                } else
                    sendSearchQuery(newText);
                return false;
            }
        });


    }
    //Clear search result when resume search activity
    protected void onResume() {
        super.onResume();
        final SearchView mSearchView = (SearchView) findViewById(R.id.search);
        if (mSearchView.getQuery().length() == 0) {

            userModelList.clear();
            recyclerView.setAdapter(new UserListAdapter(SearchActivity.this, userModelList));
        } else {
            sendSearchQuery(mSearchView.getQuery().toString());
        }
    }


    //Send Serach query
    public void sendSearchQuery(String word) {
        searchProgressBar.setVisibility(View.VISIBLE);

        userModelList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext()))
                .build();

        searchInterface = retrofit.create(SearchInterface.class);
        final Call<List<UserModel>> searchResponse = searchInterface.getSearchResult(word);
        searchResponse.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                Log.d("STATUS","status " + response.code());
                userModelList = (ArrayList<UserModel>) response.body();
                searchProgressBar.setVisibility(View.GONE);

                recyclerView.setAdapter(new UserListAdapter(getApplicationContext(),userModelList));


            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                searchProgressBar.setVisibility(View.GONE);

            }
        });

    }

}