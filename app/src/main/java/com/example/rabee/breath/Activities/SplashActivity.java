package com.example.rabee.breath.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.rabee.breath.GeneralFunctions;
import com.example.rabee.breath.R;

public class SplashActivity extends AppCompatActivity {
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GeneralFunctions generalFunctions = new GeneralFunctions();
        logo = (TextView) findViewById(R.id.logo);
        logo.animate().translationY(-(generalFunctions.getScreenHeight()) * 3 / 10).setDuration(1500);
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
