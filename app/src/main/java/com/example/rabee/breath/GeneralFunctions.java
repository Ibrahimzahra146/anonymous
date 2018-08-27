package com.example.rabee.breath;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rabee.breath.Models.DeviceTokenModel;
import com.example.rabee.breath.Models.RequestModels.UserDeviceIdRequestModel;
import com.example.rabee.breath.Models.ResponseModels.UserIdDeviceIdResponseModel;
import com.example.rabee.breath.Models.UserModel;
import com.example.rabee.breath.RequestInterface.DeviceTokenInterface;
import com.example.rabee.breath.RequestInterface.ImageInterface;
import com.example.rabee.breath.RequestInterface.UserIdDeviceIdInterface;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getClientToken;

public class GeneralFunctions {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GeneralInfo.SPRING_URL)
            .addConverterFactory(GsonConverterFactory.create()).
                    client(GeneralInfo.getClient(getApplicationContext())).build();
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static SharedPreferences getSharedPreferences(Context ctxt) {

        SharedPreferences sharedPreferences = ctxt.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int notifications_counter = sharedPreferences.getInt("notifications_counter", 0);
        return sharedPreferences;

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int getPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public void storeDeviceIdWithDeviceToken(int user_id, String deviceId) {

        DeviceTokenInterface tokenApi = retrofit.create(DeviceTokenInterface.class);
        DeviceTokenModel deviceTokenModel = new DeviceTokenModel();
        deviceTokenModel.setDeviceId(deviceId);
        deviceTokenModel.setToken(getClientToken());
        Call<DeviceTokenModel> call = tokenApi.storeDeviceToken(deviceTokenModel);
        call.enqueue(new Callback<DeviceTokenModel>() {

            @Override
            public void onResponse(Call<DeviceTokenModel> call, Response<DeviceTokenModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {
                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<DeviceTokenModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });
    }

    /**
     * @param user_id
     * @param deviceId
     */
    public void storeUserIdWithDeviceId(int user_id, String deviceId) {

        UserIdDeviceIdInterface tokenApi = retrofit.create(UserIdDeviceIdInterface.class);
        UserDeviceIdRequestModel userDeviceIdRequestModel = new UserDeviceIdRequestModel();
        userDeviceIdRequestModel.setUserId(user_id);
        userDeviceIdRequestModel.setDeviceId(deviceId);
        Call<UserIdDeviceIdResponseModel> call = tokenApi.storeUserIdWithDeviceId(userDeviceIdRequestModel);
        call.enqueue(new Callback<UserIdDeviceIdResponseModel>() {

            @Override
            public void onResponse(Call<UserIdDeviceIdResponseModel> call, Response<UserIdDeviceIdResponseModel> response) {
                if (response.code() == 404 || response.code() == 500 || response.code() == 502 || response.code() == 400) {

                    GeneralFunctions generalFunctions = new GeneralFunctions();
                    generalFunctions.showErrorMesaage(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<UserIdDeviceIdResponseModel> call, Throwable t) {
                GeneralFunctions generalFunctions = new GeneralFunctions();
                generalFunctions.showErrorMesaage(getApplicationContext());
            }
        });
    }

    /**
     * @param c
     * @return
     */
    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * @param bitmap
     * @param path
     * @return
     */
    public File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void showErrorMesaage(Context context) {
        Toast.makeText(context, "Something went worng",
                Toast.LENGTH_SHORT).show();
    }

    public void uploadImagetoDB(int user_id, String path, Bitmap bitmap, final int requestCode, final ProgressBar imageProgressBar) {
        File file = new File(path);
        final GeneralFunctions generalFunctions = new GeneralFunctions();

        file = generalFunctions.saveBitmap(bitmap, path);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploadfile", file.getName(), requestFile);


        ImageInterface imageInterface = retrofit.create(ImageInterface.class);
        Call<UserModel> userImageResponse;
        if (requestCode == 100) {
            userImageResponse = imageInterface.uploadProfileImage(body, GeneralInfo.userID);
        } else {
            userImageResponse = imageInterface.uploadCoverImage(body, GeneralInfo.userID);
        }

        userImageResponse.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (requestCode == 100) {
                    GeneralInfo.getGeneralUserInfo().getUser().setImage(response.body().getImage());
                } else {
                    GeneralInfo.getGeneralUserInfo().getUser().setCover_image(response.body().getCover_image());
                }
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(GeneralInfo.generalUserInfo);
                editor.putString("generalUserInfo", json);
                editor.apply();
                imageProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("ImagesCode ", " Error " + t.getMessage());

            }
        });
    }

    /**
     *
     * @return
     */
    public int  getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;

    }
}