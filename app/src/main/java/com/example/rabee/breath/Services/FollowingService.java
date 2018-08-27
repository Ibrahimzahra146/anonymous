package com.example.rabee.breath.Services;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rabee.breath.Activities.OtherProfileActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.FollowingRequestModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingRelationResponseModel;
import com.example.rabee.breath.Models.ResponseModels.FollowingResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.FollowingInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 1/27/2018.
 */

public class FollowingService {

    static Dialog ConfirmDeletion;
    static Button NoBtn;
    static Button YesBtn;
    static TextView textMsg;
    public static void startRightActivity(Context mContext, String mName, int Id, String ImageUrl) {
        if (Id == GeneralInfo.getUserID()) {
            Intent i = new Intent(mContext, OtherProfileActivity.class);
            mContext.startActivity(i);
        } else {
            Intent i = new Intent(mContext, OtherProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("mName", mName);
            b.putInt("Id", Id);
            b.putString("mImageURL", ImageUrl);
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(i);
        }
    }
    public static void setFollowRelationState(final Button friendStatus, final Context context, final UserModel userModel, Context c) {
        // recyclerView.setVisibility(View.VISIBLE);
        final int Id=userModel.getId();

        if (GeneralInfo.friendMode == 0)
            friendStatus.setText("Follow");

        if (GeneralInfo.friendMode == 1)
            friendStatus.setText("Following");


        ConfirmDeletion = new Dialog(context);
        ConfirmDeletion.setContentView(R.layout.confirm_delete_following_dialog);
        NoBtn = (Button) ConfirmDeletion.findViewById(R.id.NoBtn);
        YesBtn = (Button) ConfirmDeletion.findViewById(R.id.YesBtn);

        textMsg = (TextView) ConfirmDeletion.findViewById(R.id.TextMsg);

        friendStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Pending state
                if (GeneralInfo.friendMode == 1) {
                    textMsg.setText("Are you sure you want to delete the follow ?");
                    ConfirmDeletion.show();
                    NoBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            ConfirmDeletion.dismiss();
                        }
                    });
                    YesBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // recyclerView.setVisibility(View.GONE);
                            GeneralInfo.friendMode = 0;
                            friendStatus.setText("Follow");
                            ConfirmDeletion.dismiss();
                            DeleteFriend( GeneralInfo.getUserID() , Id);
                        }
                    });
                }
                //Friend state

                //not friend state
                else if (GeneralInfo.friendMode == 0) {
                    Log.d("OtherActivityProfile","Requested");

                    GeneralInfo.friendMode = 1;
                    friendStatus.setText("Following");
                    addNewFollowing(GeneralInfo.getUserID(), Id);
                }
            }
        });
    }
    public  static  void addNewFollowing(int friend1_id, int friend2_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        FollowingInterface followingInterface = retrofit.create(FollowingInterface.class);

        FollowingRequestModel friendshipModel = new FollowingRequestModel();
        friendshipModel.setFriend1_id(friend1_id);
        friendshipModel.setFriend2_id(friend2_id);


        Call<FollowingRelationResponseModel> addFrienshipCall = followingInterface.sendNewFollow(friendshipModel);
        addFrienshipCall.enqueue(new Callback<FollowingRelationResponseModel>() {
            @Override
            public void onResponse(Call<FollowingRelationResponseModel> call, Response<FollowingRelationResponseModel> response) {

            }

            @Override
            public void onFailure(Call<FollowingRelationResponseModel> call, Throwable t) {

            }
        });

    }

    public static void DeleteFriend(int friend1_id, int friend2_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
        FollowingInterface FriendApi = retrofit.create(FollowingInterface.class);

        final FollowingRequestModel followingRequestModel = new FollowingRequestModel();
        followingRequestModel.setFriend1_id(friend1_id);
        followingRequestModel.setFriend2_id(friend2_id);


        final Call<Integer> deleteCall = FriendApi.deleteFollow(followingRequestModel);
        deleteCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("fail to get friends ", "Failure to Get friends");

            }


        });


    }

}
