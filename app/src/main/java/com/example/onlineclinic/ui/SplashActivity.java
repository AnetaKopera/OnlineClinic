package com.example.onlineclinic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineclinic.R;
import com.example.onlineclinic.UserActivity;
import com.example.onlineclinic.MainActivity;

public class SplashActivity extends AppCompatActivity {
    //private String CITY_NAME = "city_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splash_image);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transition_animation);
        imageView.startAnimation(animation);

        String noLogout = SharedPrefs.loadData(this, "who_is_logged");

        System.out.println("SPLASH: " + noLogout);

        final Intent intent;

        if (noLogout.equals("Nobody") || noLogout.equals(" ")) {
            intent = new Intent(this, MainActivity.class);
            UserActivity.setUserId("");
        } else {
            intent = new Intent(this, UserActivity.class);

            //CITY_NAME += noLogout;

            //String city_name = SharedPrefs.loadData(this,CITY_NAME);

            UserActivity.setUserId(noLogout);
            // UserActivity.setUserCity(city_name);
        }


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}