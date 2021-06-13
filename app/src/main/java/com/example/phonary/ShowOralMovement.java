package com.example.phonary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ShowOralMovement extends AppCompatActivity implements View.OnClickListener {

    public TextView tvType, tvLetterDisplay, tvPronunDisplay;
    public ImageView imgFlag;
    public VideoView vidRecord;
    public Button btnBack, btnPractice;

    public Intent intent;

    public String strType, strLetter, strPronun, strLanguage;
    public int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_oral_movement);

        // Bind views
        tvType = findViewById(R.id.tv_type);
        tvLetterDisplay = findViewById(R.id.tv_letter_display);
        tvPronunDisplay = findViewById(R.id.tv_pronun_display);
        imgFlag = findViewById(R.id.img_flag);
        btnBack = findViewById(R.id.btnBack);
        btnPractice = findViewById(R.id.btnPractice);
        vidRecord = findViewById(R.id.vid_realRecord);
        // Set onClick Listeners
        btnBack.setOnClickListener(this);
        btnPractice.setOnClickListener(this);

        receiveBundleData();

        initLanguageInfo();

        playVideo();

    }

    public void receiveBundleData() {
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        strType = bundle.getString("type");
        strLanguage = bundle.getString("language");
        strLetter = bundle.getString("letter");
        strPronun = bundle.getString("pronunciation");
        position = bundle.getInt("position");
    }

    public void initLanguageInfo() {
        // Set type of consonant (vowel / phoneme)
        tvType.setText(strType);
        // Set flag
        switch (strLanguage) {
            case "thai":
                imgFlag.setImageResource(R.drawable.ic_thailand);
                break;
            case "japanese":
                imgFlag.setImageResource(R.drawable.ic_japan);
                break;
            case "vietnamese":
                imgFlag.setImageResource(R.drawable.ic_vietnam);
                break;
        }
        // Set letter
        tvLetterDisplay.setText(strLetter);
        tvPronunDisplay.setText(strPronun);
    }

    public void playVideo() {
        MediaController mediaController = new MediaController(this);
        vidRecord.setMediaController(mediaController);
        String fileName = "thai" + strType.toLowerCase() + "_" + (position + 1);
        int rawId = getResources().getIdentifier(fileName,  "raw", getPackageName());
        String path = "android.resource://" + getPackageName() + "/" + rawId;

        Uri uri = Uri.parse(path);
        vidRecord.setVideoURI(uri);
        vidRecord.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPractice:
                // Prepare data bundle to letter-explaining activity
                Bundle bundleForward = new Bundle();
                bundleForward.putString("type", strType);
                bundleForward.putString("language", "Thai");
                bundleForward.putString("letter", strLetter);
                bundleForward.putString("pronunciation", strPronun);
                bundleForward.putInt("position", position);
                // Transfer to letter-explaining activity
                Intent intentForward = new Intent(ShowOralMovement.this, PracticeActivity.class);
                intentForward.putExtras(bundleForward);
                startActivity(intentForward);
                break;
        }
    }
}