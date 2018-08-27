package com.example.rabee.breath.Activities.ChangeAccountInfoActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.EditEmailRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AccountInfoInterface;
import com.example.rabee.breath.Services.FieldValidationService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangeEmailActivity extends Activity {

    TextView toolbarText;
    Button saveNewEmail;
    EditText newEmailText, passwordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_email_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveNewEmail = (Button) findViewById(R.id.saveNewEmail);
        newEmailText = (EditText) findViewById(R.id.newEmailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        saveNewEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((newEmailText.getText().toString().equals(""))) {
                    newEmailText.setError("Email is required");
                } else if (!(FieldValidationService.valid(newEmailText.getText().toString(),"----------",newEmailText,null))) {
                    /// TO DO ? DEPARATED OR NOT
                    newEmailText.setError("Email is not valid");
                } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GeneralInfo.SPRING_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
                final EditEmailRequestModel editEmailModel = new EditEmailRequestModel();
                editEmailModel.setId(GeneralInfo.getUserID());
                editEmailModel.setPassword(passwordText.getText().toString());
                editEmailModel.setNew_email(newEmailText.getText().toString());

                final Call<UserProfileResponseModel> editEmailCall = service.editEmail(editEmailModel);
                editEmailCall.enqueue(new Callback<UserProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                        if (response.code() == 200) {
                            UserProfileResponseModel generalUserInfo = response.body();
                            UserModel userModel = generalUserInfo.getUser();
                            GeneralInfo.setUserID(userModel.getId());
                            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            GeneralInfo.getGeneralUserInfo().getUser().setEmail(userModel.getEmail());
                            Gson gson = new Gson();
                            String json = gson.toJson(GeneralInfo.generalUserInfo);
                            editor.putString("email", userModel.getEmail());
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
                });}
            }
        });

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
    }

}
