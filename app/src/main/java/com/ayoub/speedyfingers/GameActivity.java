package com.ayoub.speedyfingers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    int mScore, mKeyStroke;
    WordsAdapter mAdapter;
    @Bind(R.id.toolbar_back)
    TextView mBackTextView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setSubtitle(null);
        // AutoFitRecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        RecyclerView recyclerView = (AutoFitRecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInAnimator());

        mBackTextView.setOnClickListener(this);


        String from = SwissArmyKnife.getStringFromFile(this, "eng.txt", ";");
        assert from != null;
        String[] splitted = from.split(";");

        final ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(splitted));
        //  SwissArmyKnife.randomizeList(wordsList);


        long time = getIntent().getLongExtra("time", 0);
        mAdapter = new WordsAdapter(wordsList, this, time,layoutManager,recyclerView);
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
                    mScore++;
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
        //  finish();
        //  startActivity(new Intent(this,MainActivity.class));
        Log.d("natija state", "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        // mAdapter.cancel();
        //  finish();
        Log.d("natija state", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == mBackTextView.getId()){
            this.finish();
        }
    }
}
