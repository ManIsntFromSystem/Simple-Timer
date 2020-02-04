package com.example.simpletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.playImageView)
    ImageView playImageView;

    @BindView(R.id.pauseImageView)
    ImageView pauseImageView;
    @BindView(R.id.stopImageView)
    ImageView stopImageView;

    @BindView(R.id.editText4)
    EditText editSetTime;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private boolean instancePauseImage = false;
    private MyCountDownTimer myCountDownTimer;
    private Drawable drawable;
    private String defaultInterval;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setIntervalSharedPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void btnPlay(View view) {
            long totalTime = countSetTimer();
            if(totalTime > 0) {
                progressBar.setMax((int)totalTime);
                myCountDownTimer = new MyCountDownTimer(totalTime * 1000, 1000);
                myCountDownTimer.start();
                playImageView.setVisibility(view.GONE);
                pauseImageView.setVisibility(view.VISIBLE);
                stopImageView.setVisibility(view.VISIBLE);
            } else {
                Toast.makeText(MainActivity.this, "Set time", Toast.LENGTH_SHORT).show();
            }
    }

    public void stopTimerImgView(View view) {
        myCountDownTimer.cancel();
    }

    public void pauseTimerImgView(View view) {
        if(!instancePauseImage){
            myCountDownTimer.cancel();
            instancePauseImage = true;
            drawable = getResources().getDrawable(R.drawable.icon_play);
            pauseImageView.setImageDrawable(drawable);
        } else {
            myCountDownTimer.start();
            instancePauseImage = false;
            drawable = getResources().getDrawable(R.drawable.icon_pause);
            pauseImageView.setImageDrawable(drawable);
        }
    }

    private long countSetTimer(){
        String firstValueTime = editSetTime.getText().toString();
        Log.d("TimerCurrent",  firstValueTime +" ms");

        String[] arrOfStr = firstValueTime.split(":", -2);
        int[] arrOfInt = new int[3];
        for (int i = 0; i < arrOfStr.length; i++) {
            arrOfInt[i] = Integer.parseInt(arrOfStr[i]);
        }
        Log.d("TimerCurrent", Arrays.toString(arrOfInt) +" ms");
        long totalTime = ((arrOfInt[0] * 60) * 60) + (arrOfInt[1] * 60) + arrOfInt[2];
        return totalTime;
    }

    private void  setIntervalSharedPreferences(SharedPreferences sharedPreferences){
        defaultInterval = sharedPreferences.getString("default_interval",
                "00:01:00");
        editSetTime.setText(defaultInterval);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("default_interval")){
            setIntervalSharedPreferences(sharedPreferences);
        }
    }

    public class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/1000);
            Log.d("TimerCurrent", progress +" sec");
            progressBar.setProgress(progressBar.getMax() - progress);

            int hours1 = progress / 3600;
            int minutes1 = (progress / 3600) / 60;
            int seconds1 = progress % 60;

            String timeString = String.format("%02d:%02d:%02d", hours1, minutes1, seconds1);
            editSetTime.setText(timeString);
        }

        @Override
        public void onFinish() {
            chooseMelody();
            resetTimer();
        }

        private void resetTimer(){
            cancel();
            editSetTime.setText("00:00:00");
            progressBar.setProgress(0);
            pauseImageView.setVisibility(View.GONE);
            stopImageView.setVisibility(View.GONE);
            playImageView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Timer done!", Toast.LENGTH_SHORT).show();
        }

        private void chooseMelody(){
            if(sharedPreferences.getBoolean("enable_sound", true)) {
                String melodyName = sharedPreferences.getString("timer_melody", "bell");
                if (melodyName.equals("bell")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell_sound);
                    mediaPlayer.start();
                } else if (melodyName.equals("alarm_siren")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_siren_sound);
                    mediaPlayer.start();
                } else if (melodyName.equals("bip")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bip_sound);
                    mediaPlayer.start();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent openSettings = new Intent(this, SettingsActivity.class );
            startActivity(openSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
































/*        CountDownTimer myTimer = new CountDownTimer(10000,
                1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("myTimer: ", String.valueOf(millisUntilFinished/1000) + " seconds left");
            }

            @Override
            public void onFinish() {
                Log.d("myTimer: ", "Finish");
            }
        };

        myTimer.start();

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Runnable: ", "Two seconds were passing");
                handler.postDelayed(this, 2000);
            }
        };


        handler.post(runnable);
 */
