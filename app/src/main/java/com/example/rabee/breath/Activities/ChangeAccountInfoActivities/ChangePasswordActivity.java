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
import android.widget.Toast;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.EditPasswordRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AccountInfoInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends Activity {

    TextView toolbarText;
    Button saveNewEmail;
    EditText currentPasswordText, newPasswordText, confirmPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_password_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveNewEmail = (Button) findViewById(R.id.saveNewPassword);
        currentPasswordText = (EditText) findViewById(R.id.currentPassword);
        newPasswordText = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        saveNewEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (newPasswordText.getText().toString().length()<8)
                {
                    Toast.makeText(ChangePasswordActivity.this, "New password can't be less than 8 characters",
                            Toast.LENGTH_SHORT).show();
                }

                else {
                    if (!(confirmPassword.getText().toString().equals(newPasswordText.getText().toString()))) {
                        Toast.makeText(ChangePasswordActivity.this, "New and confirm passwords are not the same",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(GeneralInfo.SPRING_URL)
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
                        final EditPasswordRequestModel editPassword = new EditPasswordRequestModel();
                        editPassword.setId(GeneralInfo.getUserID());
                        editPassword.setOld_password(currentPasswordText.getText().toString());
                        editPassword.setNew_password(newPasswordText.getText().toString());
                        final Call<UserProfileResponseModel> editPasswordCall = service.editPassword(editPassword);
                        editPasswordCall.enqueue(new Callback<UserProfileResponseModel>() {
                            @Override
                            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                                if (response.code() == 200) {
                                    UserProfileResponseModel generalUserInfo = response.body();
                                    UserModel userModel = generalUserInfo.getUser();
                                    GeneralInfo.setUserID(userModel.getId());
                                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("password", newPasswordText.getText().toString());
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
