package com.boukharist.speedyfingers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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
    ArrayList<Item> subList;
    Activity activity;
    WordsViewHolder mHolder;
    GridLayoutManager mLayoutManager;
    long time;
    public static int[] pattern;
    public static int currentIndex; //current Index of main LIST
    public int currentStep = 0; // current step Index
    int[] solvedByStep = new int[10]; //number of answers by step
    boolean isPause = false;
    RecyclerView recyclerView;

    @Override
    public void onViewRecycled(WordsViewHolder holder) {
        super.onViewRecycled(holder);
    }


    public WordsAdapter(ArrayList<String> wordList, Activity activity, long time, GridLayoutManager layoutManager, RecyclerView recyclerView) {
        this.wordList = wordList;
        this.activity = activity;
        this.time = time;
        this.mLayoutManager = layoutManager;
        this.recyclerView = recyclerView;
        pattern = activity.getIntent().getIntArrayExtra("pattern");
        currentIndex = pattern[currentStep] - 1;

        subList = new ArrayList<>();
        for (int i = 0; i < pattern[currentStep]; i++) {
            Item item = new Item(wordList.get(i));
            subList.add(item);
        }
    }

    @Override
    public WordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_row, parent, false);
        return mHolder = new WordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordsViewHolder holder, int position) {
        holder.waveLoadingView.setCenterTitle(subList.get(position).getText());

        reSizeLayoutManager();


        if (holder.waveLoadingView.mCountDownTimer != null) {
            holder.waveLoadingView.mCountDownTimer.cancel();
        }
        holder.waveLoadingView.startCountDown(activity, time, position, this, subList.get(position).isStopped);
    }


    public void cancel() {
        isPause = true;
        // notifyDataSetChanged();
    }

    public void addItem() {
        Log.d("natija", "add Item");
        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            subList.add(new Item(wordList.get(currentIndex)));
            notifyItemInserted(subList.size() - 1);
        }
    }

    public void replaceItem(final int pos) {
        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            subList.set(pos, new Item(wordList.get(currentIndex)));
            notifyItemChanged(pos);
        }
    }

    public void stopItem(int pos) {
        subList.get(pos).setIsStopped(true);
        notifyItemChanged(pos);
    }

    private void update() {
        //  notifyItemChanged(0);
        Log.d("natija", "update");
        recyclerView.setLayoutManager(mLayoutManager);
        notifyDataSetChanged();
        // this.onBindViewHolder(mHolder,0);
    }

    public void next(int position) {
        if (!isPause) {
            solvedByStep[currentStep]++;
            if (solvedByStep[currentStep] < pattern[currentStep]) {
                stopItem(position);
            } else {
                currentStep++;
                if (pattern[currentStep - 1] == pattern[currentStep]) {
                    for (int i = 0; i < pattern[currentStep]; i++) {
                        replaceItem(i);
                    }
                } else {
                    for (int i = 0; i < pattern[currentStep] - 1; i++) {
                        replaceItem(i);
                    }
                    addItem();
                }
            }
        }
    }


    public boolean isWordHit(String typed) {
        for (int i = 0; i < subList.size(); i++) {
            if (typed.equalsIgnoreCase(subList.get(i).getText())) {
              //  SwissArmyKnife.playSound(activity,R.raw.);
                next(i);
                return true;
            }
        }
        return false;
    }

    public void gameOver() {
        final Intent intent = new Intent(activity, Main2Activity.class);
        activity.startActivity(intent);
    }

    private void reSizeLayoutManager() {
        if ((currentStep - 1 < pattern.length && solvedByStep[currentStep] == pattern[currentStep] - 1) || (currentStep == 0 && solvedByStep[currentStep] == 0)) {
            switch (pattern[currentStep + 1]) {
                case 1:
                    mLayoutManager.setSpanCount(1);
                    break;
                case 2:
                    mLayoutManager.setSpanCount(2);
                    break;
                default:
                    mLayoutManager.setSpanCount(3);
                    break;
            }
        }


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
