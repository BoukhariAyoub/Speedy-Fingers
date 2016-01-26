package com.ayoub.speedyfingers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {


    int mScore, mKeyStroke;
    WordsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AutoFitRecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        // GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //  recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        String from = SwissArmyKnife.getStringFromFile(this, "eng.txt", ";");
        assert from != null;
        final String[] splitted = from.split(";");

        final ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(splitted));
      //  SwissArmyKnife.randomizeList(wordsList);


        Random random = new Random();
        long time = random.nextInt(30000) + 10000;

        mAdapter = new WordsAdapter(wordsList, this, time);
        recyclerView.setAdapter(mAdapter);


        final DismissHandleEditText mEditText = (DismissHandleEditText) findViewById(R.id.edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (!text.toString().isEmpty()) {
                    mKeyStroke++;
                }
                Log.d("natija", "key stroke " + mKeyStroke);
                if (mAdapter.isWordHit(text.toString())) {
                    mScore++;
                    mEditText.setText("");
                    //  mScoreText.setText(String.format("Score : %d", mScore));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.pauseCountDown();
      //  finish();
      //  startActivity(new Intent(this,MainActivity.class));
        Log.d("natija state", "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
       // mAdapter.pauseCountDown();
      //  finish();
        Log.d("natija state", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.resumeCountDown();
        Log.d("natija state", "onResume");
    }



}
