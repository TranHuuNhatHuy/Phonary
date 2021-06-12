package com.example.phonary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.intellij.lang.annotations.Language;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LanguageChoosingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnThai, btnJapanese, btnVietnamese;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        RequestPermissions();

        // Bind views
        btnThai = findViewById(R.id.btn_Thai);
        btnJapanese = findViewById(R.id.btn_Japanese);
        btnVietnamese = findViewById(R.id.btn_Vietnamese);
        // Set onClick Listeners
        btnThai.setOnClickListener(this);
        btnJapanese.setOnClickListener(this);
        btnVietnamese.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Thai:
                updateDefaultLanguage("Thai");
                Intent intent = new Intent(LanguageChoosingActivity.this, ThaiActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_Japanese:
                //pdateDefaultLanguage("Japanese");
                showSUD();
                break;
            case R.id.btn_Vietnamese:
                //updateDefaultLanguage("Vietnamese");
                showSUD();
                break;
        }
    }

    // Save default language
    public void updateDefaultLanguage(String idLanguage) {
        SharedPreferences sharedPreferences = getSharedPreferences("defaultLanguage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", idLanguage);
        editor.commit();
    }

    // Show an under-development notification
    public void showSUD() {
        Toast.makeText(LanguageChoosingActivity.this,
                "This function is currently under development. We apologize for this inconvenience.",
                Toast.LENGTH_SHORT).show();
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(LanguageChoosingActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}