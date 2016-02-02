package com.boukharist.speedyfingers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.adapter.WordsAdapter;
import com.boukharist.speedyfingers.custom.views.AutoFitRecyclerView;
import com.boukharist.speedyfingers.custom.views.CountingTextView;
import com.boukharist.speedyfingers.custom.views.DismissHandleEditText;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    int mScore, mKeyStroke;
    WordsAdapter mAdapter;
    @Bind(R.id.back)
    TextView mBackTextView;
    @Bind(R.id.score)
    CountingTextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);


        // AutoFitRecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        RecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInAnimator());

        mBackTextView.setOnClickListener(this);

      //  getWindow().setBackgroundDrawable(new ColorDrawable((backgroundFromColor = getIntent().getIntExtra(WaveCompat.IntentKey.BACKGROUND_COLOR, 0xff8B7D6B))));
      //  WaveCompat.transitionDefaultInitial(this, ABTextUtil.dip2px(context, 80), backgroundFromColor, Color.GRAY);


        String from = SwissArmyKnife.getStringFromFile(this, "eng.txt", ";");
        assert from != null;

        String[] words = getResources().getStringArray(R.array.words);
        //  String[] splitted = from.split(";");

        final ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(words));
        SwissArmyKnife.randomizeList(wordsList);


        long time = getIntent().getLongExtra("time", 0);
        mAdapter = new WordsAdapter(wordsList, this, time, layoutManager, recyclerView);
        recyclerView.setAdapter(mAdapter);


        final DismissHandleEditText mEditText = (DismissHandleEditText) findViewById(R.id.edit);

        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (!text.toString().isEmpty()) {
                    mKeyStroke++;
                }
                //   Log.d("natija", "key stroke " + mKeyStroke);
                if (mAdapter.isWordHit(text.toString())) {
                    //  mScore++;
                    mEditText.setText("");

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
        mAdapter.cancel();
        finish();
        //  finish();
        //  startActivity(new Intent(this,MainActivity.class));
    }


    @Override
    protected void onStop() {
        super.onStop();
        // mAdapter.cancel();
        //  finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mBackTextView.getId()) {
            this.finish();
        }
    }

    public void addScore(int score) {
        int previous = mScore;
        mScore += score;
        mScoreTextView.animateText(previous, mScore);
    }

    public void levelFinished(){

    }
}
