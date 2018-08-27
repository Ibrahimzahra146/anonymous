package com.example.rabee.breath.Activities.ChangeAccountInfoActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.EditMobileRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AccountInfoInterface;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChangeMobileActivity extends Activity {

    TextView toolbarText;
    Button saveNewEmail;
    EditText newMobileText, passwordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_mobile_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        saveNewEmail = (Button) findViewById(R.id.saveNewMobile);
        newMobileText = (EditText) findViewById(R.id.newMobileText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        saveNewEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(newMobileText.getText().toString().trim().length() >= 9){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(GeneralInfo.SPRING_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                AccountInfoInterface service = retrofit.create(AccountInfoInterface.class);
                final EditMobileRequestModel editMobileModel = new EditMobileRequestModel();
                editMobileModel.setId(GeneralInfo.getUserID());
                editMobileModel.setPassword(passwordText.getText().toString());
                editMobileModel.setNew_mobile(newMobileText.getText().toString());
                final Call<UserProfileResponseModel> editMobileCall = service.editMobile(editMobileModel);
                editMobileCall.enqueue(new Callback<UserProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                        if (response.code() == 200) {
                            UserProfileResponseModel generalUserInfo = response.body();
                            UserModel userModel = generalUserInfo.getUser();
                            GeneralInfo.setUserID(userModel.getId());
                            GeneralInfo.getGeneralUserInfo().getUser().setMobile(userModel.getMobile());
                            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(GeneralInfo.generalUserInfo);
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

            else
                {
                    Toast.makeText(ChangeMobileActivity.this, "Entered mobile is not valid.",
                            Toast.LENGTH_SHORT).show();
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
