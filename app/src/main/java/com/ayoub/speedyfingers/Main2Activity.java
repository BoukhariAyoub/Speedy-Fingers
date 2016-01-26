package com.ayoub.speedyfingers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_easy)
    Button mEasyButton;
    @Bind(R.id.btn_medium)
    Button mMediumButton;
    @Bind(R.id.btn_hard)
    Button mHardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mEasyButton.setOnClickListener(this);
        mMediumButton.setOnClickListener(this);
        mHardButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);

        if (v.getId() == mEasyButton.getId()) {
            intent.putExtra("difficulty", "easy");
            intent.putExtra("time",Constants.COUNTDOWN_TIME_EASY);
        }

        if (v.getId() == mMediumButton.getId()) {
            intent.putExtra("difficulty", "medium");
            intent.putExtra("time",Constants.COUNTDOWN_TIME_MEDIUM);
        }

        if (v.getId() == mHardButton.getId()) {
            intent.putExtra("difficulty", "hard");
            intent.putExtra("time",Constants.COUNTDOWN_TIME_HARD);
        }

        startActivity(intent);


    }
}
