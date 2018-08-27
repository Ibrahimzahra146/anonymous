package com.example.rabee.breath.Services.FirebaseServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.rabee.breath.Activities.OtherProfileActivity;
import com.google.firebase.messaging.*;


import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Rabee on 5/13/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        int count = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("notifications_counter",
                0);


        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putInt("notifications_counter",
                ++count).commit();
        count = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("notifications_counter",
                0);
//        Log.d("notifications_counter1222", " " + remoteMessage.getNotification().getTitle());

        //check if the apploication is in the foreground
        int state = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("isRunning",
                0);
        if (state == 0)
            createNotification(count + "",remoteMessage);
        else Log.d("Notficationcounter", "" + count);
        //HomeTabbedActivity.showBadge(getApplicationContext());
        createNotification(count + "",remoteMessage);


    }

    private void createNotification(String messageBody,RemoteMessage remoteMessage) {
        String title="";

       String type= remoteMessage.getData().get("type");
        Log.d("Type ",type);
        if(type.equals("3")){
            String first_name= remoteMessage.getData().get("first_name");
            String id= remoteMessage.getData().get("id");
            String ImageUrl=remoteMessage.getData().get("image");
            Intent intent = new Intent(this, OtherProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putString("mName", first_name);
            b.putInt("Id", Integer.parseInt(id));
            b.putString("mImageURL", ImageUrl);
            intent.putExtras(b);

            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            title=first_name+" send a new follow request for you";

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("TickaPost")
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, mNotificationBuilder.build());

        }else   if(type.equals("4")){
            String first_name= remoteMessage.getData().get("first_name");
            String id= remoteMessage.getData().get("id");
            String ImageUrl=remoteMessage.getData().get("image");
            Intent intent = new Intent(this, OtherProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putString("mName", first_name);
            b.putInt("Id", Integer.parseInt(id));
            b.putString("mImageURL", ImageUrl);
            intent.putExtras(b);

            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            title=first_name+" accepted your  follow request.";

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                   // .setSmallIcon(R.drawable.follower_icon)
                    .setContentTitle("TickaPost")
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, mNotificationBuilder.build());

        }
        else   if(type.equals("5")){
            String first_name= remoteMessage.getData().get("first_name");
            String id= remoteMessage.getData().get("id");
            String ImageUrl=remoteMessage.getData().get("image");
            Intent intent = new Intent(this, OtherProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putString("mName", first_name);
            b.putInt("Id", Integer.parseInt(id));
            b.putString("mImageURL", ImageUrl);
            intent.putExtras(b);

            PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            title=first_name+" delete your  follow request.";

            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
            NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                 //   .setSmallIcon(R.drawable.remove_friend_style)
                    .setContentTitle("TickaPost")
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setSound(notificationSoundURI)
                    .setContentIntent(resultIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, mNotificationBuilder.build());

        }


    }
}
