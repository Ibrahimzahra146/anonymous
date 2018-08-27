package com.example.rabee.breath.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.SignUpRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.RequestInterface.AuthInterface;
import com.example.rabee.breath.fragments.SignUpFragments.NameFragment;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.rabee.breath.R;


public class SignUpActivity extends Activity {

    String FirstName;
    String LastName;
    String userEmail;
    String Password;
    String mobileNumber;
    String userGender;
    String userBirthDate;
    Dialog LoggingInDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.your_placeholder, new NameFragment());
        ft.commit();
    }


    public void replaceFragmnets(android.app.Fragment f) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.your_placeholder, f);
        ft.addToBackStack(null);
        ft.commit();
    }


    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void SignUp() {
        GeneralFunctions generalFunctions = new GeneralFunctions();
        SignUpRequestModel signUpModel = new SignUpRequestModel();
        signUpModel.setFirst_name(getFirstName());
        signUpModel.setLast_name(getLastName());
        signUpModel.setMobile(getMobileNumber());
        signUpModel.setEmail(getUserEmail());
        signUpModel.setPassword(getPassword());
        signUpModel.setGender(getUserGender());
        signUpModel.setBirthdate(getUserBirthDate());
        LoggingInDialog = new Dialog(this);
        LoggingInDialog.setContentView(R.layout.logging_in_dialog);
        boolean isOnline = generalFunctions.isOnline(getApplicationContext());

        if (isOnline == false) {
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            LoggingInDialog.show();
            AuthInterface authInterface;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GeneralInfo.SPRING_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(GeneralInfo.getClient(getApplicationContext()))
                            .build();
            authInterface = retrofit.create(AuthInterface.class);


            final Call<UserProfileResponseModel> signResponse = authInterface.signUp(signUpModel);
            signResponse.enqueue(new Callback<UserProfileResponseModel>() {


                @Override
                public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                    UserProfileResponseModel generalUserModel = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                    if (response.code() == 200) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] image = stream.toByteArray();
                        GeneralInfo.setUserID(Integer.valueOf(generalUserModel.getUser().getId()));
                        GeneralInfo.setGeneralUserInfo(generalUserModel);
                        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = gson.toJson(generalUserModel);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("generalUserInfo", json);
                        editor.putString("email", generalUserModel.getUser().getEmail());
                        editor.putInt("id", GeneralInfo.getUserID());
                        editor.putBoolean("isLogined", true);
                        editor.apply();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        LoggingInDialog.dismiss();
                        startActivity(i);
                        finish();

                    } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                        LoggingInDialog.dismiss();
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                    }
                }
                @Override
                public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                    LoggingInDialog.dismiss();
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            });
        }
    }

}

