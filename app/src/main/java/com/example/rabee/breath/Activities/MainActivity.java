package com.example.rabee.breath.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.GeneralInfo;
import com.example.rabee.breath.Models.RequestModels.LoginWithFacebookRequestsModel;
import com.example.rabee.breath.Models.RequestModels.LoginWithGoogleRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserProfileResponseModel;
import com.example.rabee.breath.Models.SignInRequestModel;
import com.example.rabee.breath.R;
import com.example.rabee.breath.ReactsRecyclerViewModel;
import com.example.rabee.breath.RequestInterface.AuthInterface;
import com.example.rabee.breath.Services.FieldValidationService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    static Dialog LoggingInDialog;
    LoginButton loginButton;
    AuthInterface service;
    CallbackManager callbackManager;
    SignInButton signInButton;
    CircleImageView fb, google;
    GoogleApiClient googleApiClient;
    Dialog progressDialog;
    TextView AppTitle, directSignUp, registerNow, dontHaveAccount;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;

    private EditText emailEditText;
    private EditText passEditText;
    private List<ReactsRecyclerViewModel> reactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GeneralInfo.SPRING_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(GeneralInfo.getClient(getApplicationContext())).build();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        signInButton = (SignInButton) findViewById(R.id.loginWithGoogleBtn);
        // signInButton.setOnClickListener(this);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //setGooglePlusButtonText(signInButton, "Log in with google ");
        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        directSignUp = (TextView) findViewById(R.id.direct_signup);
        registerNow = (TextView) findViewById(R.id.register_now);
        dontHaveAccount = (TextView) findViewById(R.id.dont_have_account);

        registerNow.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.main_activity_text));
        dontHaveAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.main_activity_text));

        fb = (CircleImageView) findViewById(R.id.fb);
        google = (CircleImageView) findViewById(R.id.google);
        loginButton = (LoginButton) findViewById(R.id.login_btn);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();

        LoggingInDialog = new Dialog(this);
        LoggingInDialog.setContentView(R.layout.logging_in_dialog);
        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.facebook_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        service = retrofit.create(AuthInterface.class);
        //check the user if he already logined
        int id = sharedPreferences.getInt("id", -1);
        boolean isLogined = sharedPreferences.getBoolean("isLogined", false);
        String loginType = sharedPreferences.getString("loginType", "");
        GeneralInfo.setUserID(id);
        GeneralInfo.setLoginType(loginType);
        if ((isLogined == true && loginType.equals("DIRECT_SIGNUP"))) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("generalUserInfo", "");
            GeneralInfo.setGeneralUserInfo(gson.fromJson(json, UserProfileResponseModel.class));
            Intent i = new Intent(getApplicationContext(), OneTimeLogInActivity.class);
            startActivity(i);
        } else if ((isLogined == true)) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("generalUserInfo", "");
            GeneralInfo.setGeneralUserInfo(gson.fromJson(json, UserProfileResponseModel.class));
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        //Login with facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                d("Login success", loginResult.getAccessToken().getUserId());
                loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

                d("Login success", loginResult.getAccessToken().getUserId());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    d("Login onCompleted", "" + response.getError() + "" + object.getString("email"));
                                } catch (JSONException e) {
                                    d("Login onCompleted", "catch");

                                    e.printStackTrace();
                                }


                                LoginWithFacebookRequestsModel loginWithFacebookModel = new LoginWithFacebookRequestsModel();
                                try {
                                    loginWithFacebookModel.setFirstName(object.getString("first_name"));
                                    loginWithFacebookModel.setLastName(object.getString("last_name"));
                                    loginWithFacebookModel.setId(loginResult.getAccessToken().getUserId());
                                    loginWithFacebookModel.setAccessToken(loginResult.getAccessToken().getToken());
                                    loginWithFacebookModel.setImage("");
                                    loginWithFacebookModel.setGender(object.getString("gender"));
                                    loginWithFacebookModel.setEmail(object.getString("email"));
                                    loginWithFacebookRequest(loginWithFacebookModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                d("Login cancelld", "");

            }

            @Override
            public void onError(FacebookException error) {
                d("Login onError", "");

            }
        });
        //Sign in with google
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.server_client_id)).
                requestEmail().requestServerAuthCode(getString(R.string.default_web_client_id)).requestScopes(new Scope(Scopes.PLUS_LOGIN)).build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();


        //////////////////////direct signup flow///////////////////
        directSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final Call<UserProfileResponseModel> call = service.directSignUp("");
                call.enqueue(new Callback<UserProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                        GeneralInfo.setUserID(Integer.valueOf(response.body().getUser().getId()));
                        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        GeneralInfo.setLoginType("DIRECT_SINGUP");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        GeneralInfo.setGeneralUserInfo(response.body());
                        d("DIRECTSIGNUP",response.body().getUser() + " " );

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        editor.putString("generalUserInfo", json);
                        editor.putInt("id", GeneralInfo.getUserID());
                        d("SignpPage",response.code()+ " " + GeneralInfo.getUserID());
                        editor.putBoolean("isLogined", true);
                        editor.putString("loginType", "DIRECT_SIGNUP");
                        editor.apply();

                        //laucnh home activity
                        Intent i = new Intent(getApplicationContext(), OneTimeLogInActivity.class);
                        startActivity(i);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                        progressDialog.dismiss();

                    }
                });
            }
        });
    }

    public void signIn() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 9001);
        googleApiClient.connect();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginWithGoogleBtn:
                signIn();
                progressDialog.show();
                break;
        }
        if (v == fb) {
            progressDialog.show();
            loginButton.performClick();
        }
        if (v == google) {
            signInButton.performClick();
            progressDialog.show();
            signIn();
        }


    }

    public void register(View arg0) {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }

    public void forgot_pass(View arg0) {
        Intent i = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
        startActivity(i);
    }

    public void checkLogin(View arg0) {
        final SignInRequestModel signInRequestModel = new SignInRequestModel();
        signInRequestModel.setEmail(emailEditText.getText().toString());
        signInRequestModel.setPassword(passEditText.getText().toString());
        final GeneralInfo generalFunctions = new GeneralInfo();
        //final boolean isOnline = generalFunctions.isOnline(getApplicationContext());
        if (true == false) {
            Toast.makeText(this, "no internet connection!",
                    Toast.LENGTH_LONG).show();
        } else {
            final SignInRequestModel signInModel = new SignInRequestModel();
            signInModel.setEmail(emailEditText.getText().toString());
            signInModel.setPassword(passEditText.getText().toString());
            if (FieldValidationService.valid(signInModel.getEmail(), signInModel.getPassword(), emailEditText, passEditText)) {
                LoggingInDialog.show();

                final Call<UserProfileResponseModel> userModelCall = service.signIn(signInRequestModel);
                userModelCall.enqueue(new Callback<UserProfileResponseModel>() {
                    @Override
                    public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                        d("Header",response.headers().toString());
                       // Log.d("MainActivity1 ",((response.headers().toString().split(";")[0]).split("Set-Cookie:")[1].trim() +" Hi"));
//                        Log.d("MainActivity2 ",(response.headers().get("Set-Cookie").toString().split(";")[0])+"  Hi ");

                        String session=(response.headers().toString().split(";")[0]).split("Set-Cookie:")[1].trim();
                        String remember_me=(response.headers().get("Set-Cookie").toString().split(";")[0]);
                        String cookie=session+";"+remember_me;
                        if (response.code() == 200) {
                            UserProfileResponseModel userProfileResponseModel = response.body();
                            GeneralInfo.setUserID(Integer.valueOf(userProfileResponseModel.getUser().getId()));
                            GeneralInfo.setGeneralUserInfo(userProfileResponseModel);
                            saveLoginedUserInfo(userProfileResponseModel,cookie);


                        }else {

                            progressDialog.dismiss();
                            LoggingInDialog.dismiss();
                            GeneralFunctions.showErrorMesaage(getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                        LoggingInDialog.dismiss();
                        progressDialog.dismiss();
                        GeneralFunctions.showErrorMesaage(getApplicationContext());
                    }
                });
            }
        }

    }

    public void loginWithFacebookRequest(LoginWithFacebookRequestsModel loginWithFacebookRequestsModel) {
        final Call<UserProfileResponseModel> userModelCall = service.loginWithFacebook(loginWithFacebookRequestsModel);
        userModelCall.enqueue(new Callback<UserProfileResponseModel>() {

            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                UserProfileResponseModel userProfileResponseModel = response.body();
                if (response.code() == 200 || response.code() == 202) {
                    GeneralInfo.setUserID(Integer.valueOf(userProfileResponseModel.getUser().getId()));
                    GeneralInfo.setGeneralUserInfo(userProfileResponseModel);
                    saveLoginedUserInfo(userProfileResponseModel,(response.headers().get("Set-Cookie").split(";"))[0].toString());
                    progressDialog.dismiss();

                } else {
                    LoggingInDialog.dismiss();
                    progressDialog.dismiss();

                    GeneralFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                LoggingInDialog.dismiss();

                GeneralFunctions.showErrorMesaage(getApplicationContext());

            }
        });
    }

    public void saveLoginedUserInfo(UserProfileResponseModel userProfileResponseModel,String session) {
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userProfileResponseModel);
        editor.putString("session",session);
        editor.putString("generalUserInfo", json);
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("password", passEditText.getText().toString());
        editor.putString("profileImage", userProfileResponseModel.getUser().getImage());
        editor.putString("coverImage", userProfileResponseModel.getUser().getCover_image());
        editor.putInt("id", GeneralInfo.getUserID());
        editor.putBoolean("isLogined", true);
        editor.apply();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        LoggingInDialog.dismiss();
        startActivity(i);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
      //  Log.d("Direct sign up status code", "" + requestCode);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, responseCode, data);

        }

        if (requestCode == 9001) {

            // data.getStringExtra("")

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
            if (googleApiClient.hasConnectedApi(Plus.API)) {
                LoggingInDialog.show();

                com.google.android.gms.plus.model.people.Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);


            }
        }

    }

    public void handleGoogleResult(GoogleSignInResult googleSignInResult) {
        d("handleGoogleResult:", "handleGoogleResult " + googleSignInResult.getStatus());
        if (googleSignInResult.isSuccess()) {

            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            d("handleGoogleResult:", "handleGoogleResult" + account.getEmail());
            d("handleGoogleResult:", "handleGoogleResult" + account.getId());
            d("handleGoogleResult:", "handleGoogleResult" + account.getGivenName());
            d("handleGoogleResult:", "handleGoogleResult" + account.getIdToken());

            String email = account.getEmail();
            String userId = account.getId();
            LoginWithGoogleRequestModel loginWIthGoogleModel = new LoginWithGoogleRequestModel();
            loginWIthGoogleModel.setEmail(email);
            loginWIthGoogleModel.setAccessToken(account.getIdToken());
            loginWIthGoogleModel.setFirstName(account.getGivenName());
            loginWIthGoogleModel.setLastName(account.getFamilyName());
            loginWIthGoogleModel.setGender("male");
            loginWIthGoogleModel.setId(userId);
            loginWIthGoogleModel.setImage(account.getPhotoUrl().toString());
            loginWithGoogleRequest(loginWIthGoogleModel);
        }

    }

    public void loginWithGoogleRequest(LoginWithGoogleRequestModel loginWithGoogleRequestModel) {

        d("loginWithGoogleRequest", "loginWithGoogleRequest");
        final Call<UserProfileResponseModel> userModelCall = service.loginWithGoogle(loginWithGoogleRequestModel);
        userModelCall.enqueue(new Callback<UserProfileResponseModel>() {

            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                UserProfileResponseModel userProfileResponseModel = response.body();
                if (response.code() == 200 || response.code() == 202) {
                    GeneralInfo.setUserID(Integer.valueOf(userProfileResponseModel.getUser().getId()));
                    GeneralInfo.setGeneralUserInfo(userProfileResponseModel);
                    saveLoginedUserInfo(userProfileResponseModel,"");
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    LoggingInDialog.dismiss();
                    GeneralFunctions.showErrorMesaage(getApplicationContext());

                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                LoggingInDialog.dismiss();

                GeneralFunctions.showErrorMesaage(getApplicationContext());
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
