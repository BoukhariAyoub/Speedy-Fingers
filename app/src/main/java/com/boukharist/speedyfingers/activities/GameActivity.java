package com.boukharist.speedyfingers.activities;

import android.graphics.drawable.ColorDrawable;
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
import com.boukharist.speedyfingers.custom.animation.WaveCompat;
import com.boukharist.speedyfingers.custom.views.AutoFitRecyclerView;
import com.boukharist.speedyfingers.custom.views.CountingTextView;
import com.boukharist.speedyfingers.custom.views.DismissHandleEditText;
import com.boukharist.speedyfingers.model.Level;
import com.boukharist.speedyfingers.utils.Constants;
import com.boukharist.speedyfingers.utils.PrefUtils;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    int mScore, mKeyStroke;
    WordsAdapter mAdapter;
    Level mLevel;
    Level mNextLevel;


    @Bind(R.id.back)
    TextView mBackTextView;
    @Bind(R.id.score)
    CountingTextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);


        getWindow().setBackgroundDrawable(new ColorDrawable((getIntent().getIntExtra(WaveCompat.IntentKey.BACKGROUND_COLOR, R.color.md_white_1000))));


        // AutoFitRecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        RecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new LandingAnimator());

        mBackTextView.setOnClickListener(this);

        SwissArmyKnife.setFontawesomeContainer("fonts/fontawesome.ttf", mBackTextView);
        String from = SwissArmyKnife.getStringFromFile(this, "eng.txt", ";");
        assert from != null;

        String[] words = getResources().getStringArray(R.array.words);
        final ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(words));
        SwissArmyKnife.randomizeList(wordsList);

        String levelJson = getIntent().getStringExtra("level");
        String nextLevelJson = getIntent().getStringExtra("next_level");


        mLevel = SwissArmyKnife.getObjectFromJson(levelJson, Level.class);

        if (nextLevelJson != null) {
            mNextLevel = SwissArmyKnife.getObjectFromJson(nextLevelJson, Level.class);
        }
        long time = Constants.COUNTDOWN_TIME_EASY;

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
                if (mAdapter.isWordHit(text.toString())) {
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

    public void levelFinished() {
        Games.Achievements.unlock(MainMenuActivity.GoogleApiClient, mLevel.getAchievementKey());
        mLevel.setCleared(true);
        mLevel.setScore(mScore);
        PrefUtils.putLevelProgression(this, mLevel);

        if (mNextLevel != null) {
            mNextLevel.setPlayable(true);
            PrefUtils.putLevelProgression(this, mNextLevel);
        }

    }

    public Level getLevel() {
        return mLevel;
    }
}
