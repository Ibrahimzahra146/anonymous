package com.example.rabee.breath.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rabee.breath.Activities.FollowingActivity;
import com.example.rabee.breath.Activities.MainActivity;
import com.example.rabee.breath.Activities.SavedPostActivity;
import com.example.rabee.breath.Activities.SettingsActivity;
import com.example.rabee.breath.Activities.UserProfileActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.SignOutRequestModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AuthInterface;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static TextView userName;
    LinearLayout activeFollowingLayout , followingLayout , savedPostsLayout , settingsLayout , logoutLayout;
    CircleImageView profilePicture , settingIcon;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_settings, container, false);
        userName = (TextView) view.findViewById(R.id.userName);
        activeFollowingLayout = (LinearLayout) view.findViewById(R.id.activeFollowingLayout);
        followingLayout = (LinearLayout) view.findViewById(R.id.followingLayout);
        savedPostsLayout = (LinearLayout) view.findViewById(R.id.savedPostsLayout);
        settingsLayout = (LinearLayout) view.findViewById(R.id.settingsLayout);
        logoutLayout = (LinearLayout) view.findViewById(R.id.logoutLayout);

        profilePicture = (CircleImageView) view.findViewById(R.id.profile_picture);
        settingIcon= (CircleImageView) view.findViewById(R.id.settingsIcon);
        userName.setText(GeneralInfo.generalUserInfo.getUser().getFirst_name()+ " " + GeneralInfo.generalUserInfo.getUser().getLast_name());
        String imageUrl= GeneralInfo.SPRING_URL+"/"+ GeneralInfo.generalUserInfo.getUser().getImage();
        Picasso.with(getContext()).load(imageUrl).into(profilePicture);
        LinearLayout showProfileLayout = (LinearLayout) view.findViewById(R.id.showProfileLayout);
        showProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UserProfileActivity.class);
                startActivity(i);
            }
        });

        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FollowingActivity.class);
                startActivity(i);
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        savedPostsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SavedPostActivity.class);
                startActivity(i);
            }
        });

        return view;
    }


    public void logout() {

        String android_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        AuthInterface logoutApi = retrofit.create(AuthInterface.class);

        SignOutRequestModel signOutModel = new SignOutRequestModel();
        signOutModel.setDeviceId(android_id);
        signOutModel.setUserId(GeneralInfo.getUserID());
        Call<Integer> logOutnResponse = logoutApi.signOut(signOutModel);

        logOutnResponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                } else {


                    Log.d("SignOut", " " + response.code());
                    SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                    //.activity.finish();

//                ActivityCompat.finishAffinity((Activity) context);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
                Log.d("Fail", t.getMessage());
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userName.setText(GeneralInfo.getGeneralUserInfo().getUser().getFirst_name() + " " + GeneralInfo.getGeneralUserInfo().getUser().getLast_name());

    }
}
