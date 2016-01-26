package com.ayoub.speedyfingers;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder> {

    ArrayList<String> wordList;
    ArrayList<String> subList;
    Activity activity;
    WordsViewHolder mHolder;
    long time;


    public static int currentIndex = 1;
    boolean isPause = false;

    @Override
    public void onViewRecycled(WordsViewHolder holder) {
        super.onViewRecycled(holder);
    }


    public WordsAdapter(ArrayList<String> wordList, Activity activity, long time) {
        this.wordList = wordList;
        this.activity = activity;
        this.time = time;
        subList = (ArrayList<String>) wordList.clone();
        subList = new ArrayList<>(subList.subList(0, currentIndex + 1));
    }

    @Override
    public WordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_row, parent, false);
        return mHolder = new WordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordsViewHolder holder, int position) {
        Log.d("natija", "onBindViewHolder " + position);
        holder.waveLoadingView.setCenterTitle(subList.get(position));
        long time = 10000;// new Random().nextInt(20000) + 10000;
        if (holder.waveLoadingView.mCountDownTimer != null) {
            holder.waveLoadingView.mCountDownTimer.cancel();
        }
        holder.waveLoadingView.startCountDown(activity, time, position, this);
    }

    public void resumeCountDown() {
        isPause = false;
        notifyDataSetChanged();
    }

    public void pauseCountDown() {
        isPause = true;
        // notifyDataSetChanged();
    }

    public void replaceItem(final int pos) {

        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            subList.set(pos, wordList.get(currentIndex));
            notifyItemChanged(pos);
        }

    }


    public boolean isWordHit(String typed) {
        for (int i = 0; i < subList.size(); i++) {
            if (typed.equalsIgnoreCase(subList.get(i))) {
                replaceItem(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return subList.size();
    }

    public static class WordsViewHolder extends RecyclerView.ViewHolder {

        protected WaveLoadingView waveLoadingView;

        public WordsViewHolder(View itemView) {
            super(itemView);
            waveLoadingView = (WaveLoadingView) itemView.findViewById(R.id.waveLoadingView);
        }
    }

}
