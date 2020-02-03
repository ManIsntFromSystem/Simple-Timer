package com.example.simpletimer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.buttonStart)
    Button buttonStart;

    @BindView(R.id.pauseImageView)
    ImageView pauseImageView;
    @BindView(R.id.stopImageView)
    ImageView stopImageView;

    @BindView(R.id.editText4)
    EditText editSetTime;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    boolean instancePauseImage = false;
    MyCountDownTimer myCountDownTimer;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void btnPlay(View view) {
            int totalTime = countSetTimer();
            progressBar.setMax(totalTime + 1);
            myCountDownTimer = new MyCountDownTimer(totalTime * 1000, 1000);
            myCountDownTimer.start();
            buttonStart.setVisibility(view.GONE);
            pauseImageView.setVisibility(view.VISIBLE);
            stopImageView.setVisibility(view.VISIBLE);
    }

    public void stopTimerImgView(View view) {
        myCountDownTimer.onFinish();
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

    private int countSetTimer(){
        String firstValueTime = editSetTime.getText().toString();
        Log.d("TimerCurrent",  firstValueTime +" ms");

        String[] arrOfStr = firstValueTime.split(":", -2);
        int[] arrOfInt = new int[3];
        for (int i = 0; i < arrOfStr.length; i++) {
            arrOfInt[i] = Integer.parseInt(arrOfStr[i]);
        }
        Log.d("TimerCurrent", Arrays.toString(arrOfInt) +" ms");
        int totalTime = ((arrOfInt[0] * 60) * 60) + (arrOfInt[1] * 60) + arrOfInt[2];
        return totalTime;
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
        }

        @Override
        public void onFinish() {
            cancel();
            progressBar.setProgress(0);
            pauseImageView.setVisibility(View.GONE);
            stopImageView.setVisibility(View.GONE);
            buttonStart.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Timer done!", Toast.LENGTH_SHORT).show();

        }
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
