package com.example.phonary;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.john.waveview.WaveView;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView tvType, tvStatus, tvLetterDisplay, tvPronunDisplay, tvAnalyze;
    public ImageView imgFlag;
    public VideoView vidRecord;
    public Button btnStartRecord, btnStopRecord, btnStartReplay, btnStopReplay, btnBack;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    public WaveView waveView;

    public Intent intent;

    public String strType, strLetter, strPronun, strLanguage;
    private static String mFileName = null;
    public int position;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public boolean isPlaying = false;
    public boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        // Bind views
        tvType = findViewById(R.id.tv_type);
        tvStatus = findViewById(R.id.tv_Status);
        tvLetterDisplay = findViewById(R.id.tv_letter_display);
        tvPronunDisplay = findViewById(R.id.tv_pronun_display);
        tvAnalyze = findViewById(R.id.tv_Analyze);
        imgFlag = findViewById(R.id.img_flag);

        btnStartRecord = findViewById(R.id.btnStartRecord);
        btnStopRecord = findViewById(R.id.btnStopRecord);
        btnStartReplay = findViewById(R.id.btnStartReplay);
        btnStopReplay = findViewById(R.id.btnStopReplay);
        btnBack = findViewById(R.id.btnBack);

        vidRecord = findViewById(R.id.vid_realRecord);

        waveView = findViewById(R.id.waveView);

        // Set onClick Listeners
        btnStartRecord.setOnClickListener(this);
        btnStopRecord.setOnClickListener(this);
        btnStartReplay.setOnClickListener(this);
        btnStopReplay.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        // Initiate button state
        btnStopRecord.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStartReplay.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStopReplay.setBackgroundColor(getResources().getColor(R.color.gray));

        receiveBundleData();

        initLanguageInfo();

        playVideo();

        // Initiate wave animation
        waveView.setProgress(0);

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
        tvType.setText("Practice " + strType.toLowerCase());
        // Set flag
        switch (strLanguage.toLowerCase()) {
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
            case R.id.btnStartRecord:
                startRecording();
                break;
            case R.id.btnStopRecord:
                pauseRecording();
                break;
            case R.id.btnStartReplay:
                playAudio();
                break;
            case R.id.btnStopReplay:
                pausePlaying();
                break;
        }
    }

    private void startRecording() {

        raiseUpTheWave();

        if (CheckPermissions()) {

            btnStopRecord.setBackgroundColor(getResources().getColor(R.color.aux_color));
            btnStartRecord.setBackgroundColor(getResources().getColor(R.color.gray));
            btnStartReplay.setBackgroundColor(getResources().getColor(R.color.gray));
            btnStopReplay.setBackgroundColor(getResources().getColor(R.color.gray));

            tvAnalyze.setVisibility(View.INVISIBLE);

            mFileName = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
            mFileName += "/AudioRecording.3gp";
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOutputFile(mFileName);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }

            mRecorder.start();
            tvStatus.setText("Recording Started");
        } else {
            RequestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(PracticeActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    public void playAudio() {
        btnStopRecord.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStartRecord.setBackgroundColor(getResources().getColor(R.color.aux_color));
        btnStartReplay.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStopReplay.setBackgroundColor(getResources().getColor(R.color.aux_color));

        raiseUpTheWave();

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            tvStatus.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
        btnStopRecord.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStartRecord.setBackgroundColor(getResources().getColor(R.color.aux_color));
        btnStartReplay.setBackgroundColor(getResources().getColor(R.color.aux_color));
        btnStopReplay.setBackgroundColor(getResources().getColor(R.color.aux_color));

        analyzeRecord();

        turnOffTheWave();

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        tvStatus.setText("Recording Stopped");
    }

    public void pausePlaying() {
        mPlayer.release();
        mPlayer = null;
        btnStopRecord.setBackgroundColor(getResources().getColor(R.color.gray));
        btnStartRecord.setBackgroundColor(getResources().getColor(R.color.aux_color));
        btnStartReplay.setBackgroundColor(getResources().getColor(R.color.aux_color));
        btnStopReplay.setBackgroundColor(getResources().getColor(R.color.gray));

        turnOffTheWave();

        tvStatus.setText("Recording Play Stopped");
    }

    public void analyzeRecord() {
        int floor = 90;
        int ceil = 99;
        int percentage = (int)(Math.ceil(Math.random() * (ceil - floor) + floor));
        tvAnalyze.setVisibility(View.VISIBLE);
        tvAnalyze.setText(percentage + "% compared to native speech");
    }

    public void raiseUpTheWave() {
        ValueAnimator va = ValueAnimator.ofInt(0, 50);
        int mDuration = 500; //in millis
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                waveView.setProgress((int)animation.getAnimatedValue());
            }
        });
        va.start();
    }

    public void turnOffTheWave() {
        ValueAnimator va = ValueAnimator.ofInt(50, 0);
        int mDuration = 500; //in millis
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                waveView.setProgress((int)animation.getAnimatedValue());
            }
        });
        va.start();
    }
}