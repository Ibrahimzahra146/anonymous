package com.example.rabee.breath.fragments.UpdatePassword;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.rabee.breath.Activities.UpdatePasswordActivity;
import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.SignInRequestModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.RequestInterface.AuthInterface;
import com.example.rabee.breath.Services.EmailSenderService.BackgroundSender;
import com.example.rabee.breath.Services.FieldValidationService;

import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendEmailFragment extends android.app.Fragment {

    EditText recievedEmail;
    Button btn;
    String uniqueID;
    AuthInterface service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        View view = inflater.inflate(R.layout.update_paswword_send_mail_fragment, container, false);
        recievedEmail = (EditText) view.findViewById(R.id.userEmail);
        uniqueID = UUID.randomUUID().toString();
        uniqueID = uniqueID.split("-")[0];
        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recievedEmail.getText().toString().equals("")) {
                    recievedEmail.setError("Email is required");
                } else {
                    if (!(FieldValidationService.valid(recievedEmail.getText().toString(),"----------",recievedEmail,null))) {
                        recievedEmail.setError("Invalid Email");
                    } else {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(GeneralInfo.SPRING_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(GeneralInfo.getClient(getActivity().getApplicationContext())).build();
                        service = retrofit.create(AuthInterface.class);
                        final SignInRequestModel signInModel = new SignInRequestModel();
                        signInModel.setEmail(recievedEmail.getText().toString());
                        signInModel.setPassword("@(-_-)@");
                        final Call<UserProfileResponseModel> userModelCall = service.signIn(signInModel);
                        userModelCall.enqueue(new Callback<UserProfileResponseModel>() {
                            @Override
                            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                                if (response.code() != 204) {
                                    BackgroundSender BS = new BackgroundSender();
                                    BS.setRecievedEmail(recievedEmail.getText().toString());
                                    BS.setUniqueID(uniqueID);
                                    BS.execute("");

                                    ((UpdatePasswordActivity) getActivity()).setUniqueID(uniqueID);
                                    ((UpdatePasswordActivity) getActivity()).setEmail(recievedEmail.getText().toString());
                                    android.app.Fragment f = new CheckCodeFragment();
                                    ((UpdatePasswordActivity) getActivity()).replaceFragmnets(f);

                                } else if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                                    GeneralFunctions generalFunctions = new GeneralFunctions();
                                    generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                                } else {
                                    recievedEmail.setError("Incorrect Email");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                                GeneralFunctions generalFunctions = new GeneralFunctions();
                                generalFunctions.showErrorMesaage(getActivity().getApplicationContext());
                            }
                        });
                    }


                }
            }
        });
        return view;

    }


}
