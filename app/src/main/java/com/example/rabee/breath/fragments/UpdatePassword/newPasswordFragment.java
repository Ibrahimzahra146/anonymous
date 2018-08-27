package com.example.rabee.breath.fragments.UpdatePassword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rabee.breath.Activities.HomeActivity;
import com.example.rabee.breath.Activities.UpdatePasswordActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.SignInRequestModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AuthInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class newPasswordFragment extends android.app.Fragment {

    Button btn;
    EditText newPassword;
    AuthInterface service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.update_password_new_password_fragment, container, false);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if ((newPassword.getText().toString().trim().equals(""))) {
                    newPassword.setError("Password is required");
                } else {
                    if (newPassword.getText().toString().length() < 8) {
                        newPassword.setError("Password must contain at least 8 characters");
                    } else {
                        updatePassword(newPassword);
                    }
                }
            }
        });
        return view;
    }
    public void updatePassword(EditText newPassword){
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(GeneralInfo.getClient(getActivity().getApplicationContext())).build();

        service = retrofit.create(AuthInterface.class);
        final SignInRequestModel signInModel = new SignInRequestModel();
        signInModel.setEmail( ((UpdatePasswordActivity) getActivity()).getEmail());
        signInModel.setPassword(newPassword.getText().toString());
        final Call<UserModel> userModelCall = service.updatePassword(signInModel);
        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() ==200) {
                    UserModel userModel=response.body();
                    GeneralInfo.setUserID(userModel.getId());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", userModel.getEmail());
                    editor.putInt("id", GeneralInfo.getUserID());
                    editor.putString("isLogined", "1");
                    editor.apply();
                    Intent i = new Intent( getActivity(), HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
            }
        });
    }


}
