package com.example.phonary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashScreen extends AppCompatActivity {

    private int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
                String defaultLanguage = sharedPreferences.getString("language", null);
                if (defaultLanguage == null) {
                    Intent intent = new Intent(SplashScreen.this, LanguageChoosingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    switch (defaultLanguage) {
                        case "Thai":
                            Intent intent = new Intent(SplashScreen.this, ThaiActivity.class);
                            startActivity(intent);
                            finish();
                        break;
                        case "Japanese":
                            break;
                        case "Vietnamese":
                            break;
                    }
                }
            }
        }, SPLASH_TIME);
    }
}