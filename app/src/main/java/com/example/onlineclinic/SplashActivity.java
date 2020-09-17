package com.example.onlineclinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineclinic.ui.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

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

            UserActivity.setUserId(noLogout);
        }


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
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