package com.example.rabee.breath.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.rabee.breath.Activities.AddPostActivity;
import com.example.rabee.breath.Activities.RecentCommentsActivity;
import com.example.rabee.breath.Adapters.HomePostAdapter;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ResponseModels.PostCommentResponseModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.ReactsRecyclerViewModel;
import com.example.rabee.breath.RequestInterface.PostInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<PostCommentResponseModel> postResponseModelsList = new ArrayList<>();
    static boolean loading = true; // True if we are still waiting for the last set of data to load.
    static List<PostCommentResponseModel> currentList;
    private static HomePostAdapter adapter;
    ShimmerRecyclerView recyclerView;
    LinearLayoutManager linearLayout;
    ProgressBar progressBar;
    LinearLayout noFriendsLayout;
    PostInterface postInterface;
    boolean isLastPage = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int page = 0;


    boolean isScrolling = false, isLoading = false;
    //flag to indicate we should load new posts or should return to same previous instance

    boolean firstTime = true;

    //save the post list before swap to can retrieve it when return
    View view;
    private Parcelable recyclerViewState;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        ReactsRecyclerViewModel reactSingleModel = new ReactsRecyclerViewModel(1, "Ibrahim zahra", "75782539973_288842465026282085_n.jpg");
        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView.showShimmerAdapter();
        linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.hasFixedSize();
        recyclerView.stopScroll();

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        noFriendsLayout = (LinearLayout) view.findViewById(R.id.no_friends_Layout);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .client(GeneralInfo.getClient(getContext()))
                .addConverterFactory(GsonConverterFactory.create()).build();

        postInterface = retrofit.create(PostInterface.class);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayout.getItemCount();
                firstVisibleItem = linearLayout.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && !isLastPage && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    // Do something
                    page++;
                    loading = true;
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                    performPagination(page);
                }
            }

        });


        if (firstTime == false) {
            adapter = new HomePostAdapter(getActivity(), postResponseModelsList);

            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            performPagination(page);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton RecentActivityFab = (FloatingActionButton) view.findViewById(R.id.RecentActivityFab);
        FloatingActionButton AddPostActivityfab = (FloatingActionButton) view.findViewById(R.id.AddPostActivityfab);
        RecentActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), RecentCommentsActivity.class);
                startActivity(i);
            }
        });
        AddPostActivityfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddPostActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    //////////////////

    public void performPagination(final int page) {
        final Call<List<PostCommentResponseModel>> postResponse = postInterface.getUserHomePost(GeneralInfo.getUserID(), page);

        postResponse.enqueue(new Callback<List<PostCommentResponseModel>>() {

            @Override
            public void onResponse(Call<List<PostCommentResponseModel>> call, Response<List<PostCommentResponseModel>> response) {
                currentList = postResponseModelsList;
                //ensure that still in homefragment
                if (getActivity() != null) {


                    if (response.body().size() == 0 && page == 0) {
                        noFriendsLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.INVISIBLE);
                        isLastPage = true;

                    } else if (response.body().size() == 0) {
                        isLastPage = true;

                    } else {
                        if (page == 0) {

                            int startPosition = postResponseModelsList.size();
                            postResponseModelsList.addAll(response.body());
                            adapter = new HomePostAdapter(getContext(), postResponseModelsList);
                            recyclerView.setAdapter(adapter);
                            //adapter.notifyItemRangeInserted(adapter.getItemCount(), postResponseModelsList.size() - 1);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            //firstTime = false;
                        } else {


                            postResponseModelsList.addAll(response.body());
                            Log.d("-------", "   ACTIVITY  NOT  PAGE  0 " + postResponseModelsList.size());
                            adapter.notifyItemRangeChanged(postResponseModelsList.size() - 10, postResponseModelsList.size() - 1);
//                            adapter.notifyItemChanged();
                            recyclerView.setAdapter(adapter);
                            //adapter.notifyItemRangeInserted(adapter.getItemCount(), postResponseModelsList.size() - 1);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<List<PostCommentResponseModel>> call, Throwable t) {

            }
        });
    }


}
