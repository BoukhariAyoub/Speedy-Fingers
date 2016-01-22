package com.ayoub.speedyfingers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final WaveLoadingView waveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        final TextView textView = (TextView) findViewById(R.id.txt);
        waveLoadingView.setProgressValue(100);


        String from = "Breakfast procuring nay end happiness allowance assurance frankness. Met simplicity nor difficulty unreserved who. Entreaties mr conviction dissimilar me astonished estimating cultivated. On no applauded exquisite my additions. Pronounce add boy estimable nay suspected. You sudden nay elinor thirty esteem temper. Quiet leave shy you gay off asked large style.";
        final String[] splitted = from.split(" ");


        new CountDownTimer(time, 1000) {
            int countseconds = 0;

            public void onTick(long millisUntilFinished) {
                countseconds++;
            }

            public void onFinish() {
                waveLoadingView.setCenterTitle("done! ");
                Log.d("natija", "seconds = " + countseconds);

            }
        }.start();


        new CountDownTimer(time, time / 100) {
            int count = 1;

            public void onTick(long millisUntilFinished) {
                count++;
                waveLoadingView.setProgressValue(100 - count);
            }

            public void onFinish() {
                waveLoadingView.setCenterTitle("done! ");
                Log.d("natija", "updates = " + count);

            }
        }.start();
    }

}
