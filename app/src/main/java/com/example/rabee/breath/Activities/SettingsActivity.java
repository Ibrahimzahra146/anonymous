package com.example.rabee.breath.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.rabee.breath.Activities.ChangeAccountInfoActivities.ChangeEmailActivity;
import com.example.rabee.breath.Activities.ChangeAccountInfoActivities.ChangeMobileActivity;
import com.example.rabee.breath.Activities.ChangeAccountInfoActivities.ChangePasswordActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.EditPrivacyRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AccountInfoInterface;
import com.google.gson.Gson;

import at.markushi.ui.CircleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SettingsActivity extends Activity {

    TextView toolbarText, saveSettings;
    CircleButton purpleColorBtn, greenColorBtn;
    RadioButton privateAccountRadio, publicAccountRadio, privateProfilePictureRadio, publicProfilePictureRadio;
    RadioButton purpleColorRadio, greenColorRadio;
    TextView editProfile, changePassword, changeEmail, changeMobile, userMobile, userMail;
    String themeColor;
    String isProfileImagePublic, isAccountPublic;


    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveSettings = (TextView) findViewById(R.id.saveSettings);
        editProfile = (TextView) findViewById(R.id.editProfileLabel);
        changePassword = (TextView) findViewById(R.id.changePasswordLabel);
        changeEmail = (TextView) findViewById(R.id.changeEmailLabel);
        changeMobile = (TextView) findViewById(R.id.changeMobileLabel);
        userMail = (TextView) findViewById(R.id.userMail);
        userMobile = (TextView) findViewById(R.id.userMobile);
        purpleColorBtn = (CircleButton) findViewById(R.id.purpleColorBtn);
        purpleColorRadio = (RadioButton) findViewById(R.id.purpleColorRadio);
        greenColorBtn = (CircleButton) findViewById(R.id.greenColorBtn);
        greenColorRadio = (RadioButton) findViewById(R.id.greenColorRadio);
        publicAccountRadio = (RadioButton) findViewById(R.id.publicAccountRadio);
        privateAccountRadio = (RadioButton) findViewById(R.id.privateAccountRadio);
        privateProfilePictureRadio = (RadioButton) findViewById(R.id.privateProfilePictureRadio);
        publicProfilePictureRadio = (RadioButton) findViewById(R.id.publicProfilePictureRadio);


        userMail.setText(GeneralInfo.getGeneralUserInfo().getUser().getEmail());
        userMobile.setText(GeneralInfo.getGeneralUserInfo().getUser().getMobile());
        if (GeneralInfo.getGeneralUserInfo().getUser().getIsPublic().equals("true")) {
            publicAccountRadio.setChecked(true);
        } else {
            privateAccountRadio.setChecked(true);
        }

        if (GeneralInfo.getGeneralUserInfo().getUser().getIsProfileImagePublic().equals("true")) {
            publicProfilePictureRadio.setChecked(true);
        } else {
            privateProfilePictureRadio.setChecked(true);
        }
        if (GeneralInfo.getGeneralUserInfo().getUser().getThemeColor().equals("GREEN")) {
            getTheme().applyStyle(R.style.OverlayPrimaryColorGreen, true);
            greenColorBtn.setVisibility(View.INVISIBLE);
            greenColorRadio.setChecked(true);
            purpleColorBtn.setVisibility(View.VISIBLE);
        } else {
            getTheme().applyStyle(R.style.OverlayPrimaryColorPurple, true);
            purpleColorBtn.setVisibility(View.INVISIBLE);
            purpleColorRadio.setChecked(true);
            greenColorBtn.setVisibility(View.VISIBLE);

        }

        toolbarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                if (event.getX() <= (toolbarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 40)) {
                    finish();
                    return true;
                }
                return false;
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeEmailActivity.class);
                startActivity(i);
            }
        });
        changeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeMobileActivity.class);
                startActivity(i);
            }
        });
        purpleColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purpleColorBtn.setVisibility(View.INVISIBLE);
                purpleColorRadio.setChecked(true);
                greenColorBtn.setVisibility(View.VISIBLE);
                setTheme(R.style.OverlayPrimaryColorGreen);


            }
        });

        greenColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenColorBtn.setVisibility(View.INVISIBLE);
                greenColorRadio.setChecked(true);
                purpleColorBtn.setVisibility(View.VISIBLE);
                setTheme(R.style.OverlayPrimaryColorPurple);
            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (greenColorRadio.isChecked()) {
                    themeColor = "GREEN";
                } else {
                    themeColor = "PURPLE";
                }
                if (privateAccountRadio.isChecked()) {
                    isAccountPublic = "fasle";
                } else {
                    isAccountPublic = "true";
                }
                if (publicProfilePictureRadio.isChecked()) {
                    isProfileImagePublic = "true";
                } else {
                    isProfileImagePublic = "fasle";
                }

                saveSettings();
            }
        });
    }

    public void saveSettings() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getApplicationContext()))
                .build();
        AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
        final EditPrivacyRequestModel editPrivacy = new EditPrivacyRequestModel();
        editPrivacy.setId(GeneralInfo.getUserID());
        editPrivacy.setThemeColor(themeColor);
        editPrivacy.setIsProfileImagePublic(isProfileImagePublic);
        editPrivacy.setIsPublic(isAccountPublic);

        final Call<UserProfileResponseModel> editPrivacyCall = service.editPrivacy(editPrivacy);
        editPrivacyCall.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                if (response.code() == 200) {
                    UserProfileResponseModel generalUserInfo = response.body();
                    UserModel userModel = generalUserInfo.getUser();
                    GeneralInfo.setGeneralUserInfo(generalUserInfo);
                    GeneralInfo.setUserID(userModel.getId());
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(generalUserInfo);
                    editor.putString("generalUserInfo", json);
                    editor.apply();
                    finish();
                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });
    }

}
