package com.ayoub.speedyfingers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        String from = SwissArmyKnife.getStringFromFile(this,"eng.txt",";");
        assert from != null;
        final String[] splitted = from.split(";");


        final ArrayList<String> wordsList = new ArrayList<>(Arrays.asList(splitted));


        Random random = new Random();
        long time = random.nextInt(30000) + 10000;

        final WordsAdapter adapter = new WordsAdapter(wordsList, this, time);
        recyclerView.setAdapter(adapter);
    }

}
