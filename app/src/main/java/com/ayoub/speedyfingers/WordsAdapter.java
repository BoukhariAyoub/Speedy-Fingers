package com.ayoub.speedyfingers;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder> {

    ArrayList<String> wordList;
    Activity activity;
    long time;

    public static int currentIndex = 3;


    public WordsAdapter(ArrayList<String> wordList, Activity activity, long time) {
        this.wordList = wordList;
        this.activity = activity;
        this.time = time;
    }

    @Override
    public WordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_row, parent, false);
        return new WordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordsViewHolder holder, int position) {
        holder.waveLoadingView.setCenterTitle(wordList.get(position));
        long time = new Random().nextInt(20000) + 10000;

        holder.waveLoadingView.startCountDown(activity, time, this);

    }

    public void replaceItem(final int pos) {

        Log.d("natija", "size = " + wordList.size() + "; pos = " + pos + "; current " + currentIndex);

        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            wordList.set(pos, wordList.get(currentIndex));
            notifyItemChanged(pos);
        }

    }


    @Override
    public int getItemCount() {
        return 4;
    }

    public static class WordsViewHolder extends RecyclerView.ViewHolder {

        protected WaveLoadingView waveLoadingView;

        public WordsViewHolder(View itemView) {
            super(itemView);
            waveLoadingView = (WaveLoadingView) itemView.findViewById(R.id.waveLoadingView);
        }
    }

}
