package com.example.rabee.breath.Activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.EditEmailRequestModel;
import com.example.rabee.breath.Models.RequestModels.EditProfileRequestModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AboutUserInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends Activity {

    TextView backEdit, saveEdit;
    EditText firstName, lastName;
    RadioButton maleBtn, femaleBtn;
    DatePicker birthDate;
    AboutUserInterface service;
    UserModel userModel;
    EditProfileRequestModel editProfileModle;
    ProgressBar progressBar;
    ObjectAnimator anim;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        backEdit = (TextView) findViewById(R.id.backEdit);
        saveEdit = (TextView) findViewById(R.id.saveEdit);
        firstName = (EditText) findViewById(R.id.FirstName);
        lastName = (EditText) findViewById(R.id.LastName);
        maleBtn = (RadioButton) findViewById(R.id.radioM);
        femaleBtn = (RadioButton) findViewById(R.id.radioF);
        birthDate = (DatePicker) findViewById(R.id.BirthDatePicker);
        progressBar= (ProgressBar) findViewById(R.id.ProgressBar);


        FillUserInfo();

        backEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(0);
                progressBar.setMax(100);
                anim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                anim.setDuration(2000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
                editProfileModle = new EditProfileRequestModel();
                String stringDate = String.valueOf(birthDate.getYear()) + "-" + String.valueOf(birthDate.getMonth() + 1) + "-" + String.valueOf(birthDate.getDayOfMonth());
                String gender = "";
                if (maleBtn.isChecked()) gender = "male";
                if (femaleBtn.isChecked()) gender = "female";
                editProfileModle.setId(GeneralInfo.getUserID());
                editProfileModle.setFirst_name(firstName.getText().toString());
                editProfileModle.setLast_name(lastName.getText().toString());
                editProfileModle.setBirthdate(stringDate);

                editProfileModle.setGender(gender);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GeneralInfo.SPRING_URL)
                        .addConverterFactory(GsonConverterFactory.create()).
                                client(GeneralInfo.getClient(getApplicationContext())).build();
                AboutUserInterface aboutUserApi = retrofit.create(AboutUserInterface.class);

                Call<Integer> call = aboutUserApi.updateProfile(editProfileModle);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d("EditProfile", " " + response.code());
                        if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                            GeneralFunctions generalFunctions = new GeneralFunctions();
                            generalFunctions.showErrorMesaage(getApplicationContext());
                        }
                        else
                        {
                            GeneralInfo.getGeneralUserInfo().getUser().setFirst_name(editProfileModle.getFirst_name());
                            GeneralInfo.getGeneralUserInfo().getUser().setLast_name(editProfileModle.getLast_name());
                            GeneralInfo.getGeneralUserInfo().getUser().setBirthdate(editProfileModle.getBirthdate());
                            GeneralInfo.getGeneralUserInfo().getUser().setGender(editProfileModle.getGender());
                            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            GeneralInfo.generalUserInfo=GeneralInfo.getGeneralUserInfo();
                            Gson gson = new Gson();
                            String json = gson.toJson(GeneralInfo.generalUserInfo);
                            editor.putString("generalUserInfo", json);
                            editor.apply();
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        GeneralFunctions generalFunctions = new GeneralFunctions();
                        generalFunctions.showErrorMesaage(getApplicationContext());
                        Log.d("AboutProfileUpdate", "Failure " + t.getMessage());
                    }
                });
            }
        });
    }

    public void FillUserInfo() {

        String userBirthdate="";
        userModel = GeneralInfo.generalUserInfo.getUser();
        firstName.setText(userModel.getFirst_name());
        lastName.setText(userModel.getLast_name());
        if (userModel.getGender().equals("female"))
            femaleBtn.setChecked(true);
        else if (userModel.getGender().equals("male"))
            maleBtn.setChecked(true);
        //When the user doesnt has birthdate
        //like sign up useing gmail or faceook
        if (userModel.getBirthdate()==null) {
            userBirthdate = "1990-01-01";
        } else
            userBirthdate = userModel.getBirthdate();
        String[] separated = userBirthdate.split("-");
        String year = separated[0];
        String month = separated[1];
        String day = separated[2];
        birthDate.updateDate(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));


    }
}
