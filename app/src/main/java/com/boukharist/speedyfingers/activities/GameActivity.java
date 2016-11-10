package com.boukharist.speedyfingers.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;
import com.google.android.gms.games.multiplayer.realtime.Room;

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
    Room mRoom;

    public static Activity mGameActivity;


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

        String levelJson = getIntent().getStringExtra("level");
        String nextLevelJson = getIntent().getStringExtra("next_level");

        mRoom = getIntent().getParcelableExtra("room");

        mGameActivity = this;

        mLevel = SwissArmyKnife.getObjectFromJson(levelJson, Level.class);

        if (nextLevelJson != null) {
            mNextLevel = SwissArmyKnife.getObjectFromJson(nextLevelJson, Level.class);
        }


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


        long time = Constants.COUNTDOWN_TIME_HARD;
        Log.d("natija", " level " + mLevel);
        Log.d("natija", " getlevel " + getLevel());

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
        MainMenuActivity.mInstance.leaveRoom(mRoom);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

        Log.d("natija", "level finished");
        sendMessage("ended".getBytes());

        Log.d("natija", "unlock : " + mLevel.getAchievementKey());
        Games.Achievements.unlockImmediate(MainMenuActivity.mGoogleApiClient, mLevel.getAchievementKey()).setResultCallback(new ResultCallback<Achievements.UpdateAchievementResult>() {
            @Override
            public void onResult(Achievements.UpdateAchievementResult result) {
                Log.d("natija", result.getStatus().getStatusCode() + " " + result.getStatus().getStatusMessage());
            }
        });

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


    public void sendMessage(byte[] message) {

        if (mRoom != null) {


            RealTimeMultiplayer.ReliableMessageSentCallback callback = new RealTimeMultiplayer.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {
                    Log.d("natija", "statusCode : " + statusCode);
                    Log.d("natija", "recipientParticipantId : " + recipientParticipantId);
                }
            };
            String roomId = mRoom.getRoomId();

            String myId = Games.Players.getCurrentPlayerId(MainMenuActivity.mGoogleApiClient);

            for (String opponentId : mRoom.getParticipantIds()) {
                if (!opponentId.equals(myId)) {
                    Games.RealTimeMultiplayer.sendReliableMessage(MainMenuActivity.mGoogleApiClient, callback, message,
                            roomId, opponentId);
                }

            }

          /*  Games.RealTimeMultiplayer.sendUnreliableMessage(MainMenuActivity.mGoogleApiClient, message,
                    roomId, opponentId);*/
        }


    }
}
