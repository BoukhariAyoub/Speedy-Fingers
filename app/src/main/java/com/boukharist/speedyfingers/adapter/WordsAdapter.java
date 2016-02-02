package com.boukharist.speedyfingers.adapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.activities.GameActivity;
import com.boukharist.speedyfingers.activities.MainMenuActivity;
import com.boukharist.speedyfingers.custom.views.WaveLoadingView;
import com.boukharist.speedyfingers.model.Item;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;

import java.util.ArrayList;

/**
 * Created by Administrateur on 1/22/2016.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordsViewHolder> {

    ArrayList<String> wordList;
    ArrayList<Item> subList;
    WordsViewHolder mHolder;
    GridLayoutManager mLayoutManager;
    long time;
    public static int[] pattern;
    public static int currentIndex; //current Index of main LIST
    public int currentStep = 0; // current step Index
    int[] solvedByStep = new int[10]; //number of answers by step
    RecyclerView recyclerView;
    public GameActivity mGameActivity;
    public boolean isPause = false;


    @Override
    public void onViewRecycled(WordsViewHolder holder) {
        super.onViewRecycled(holder);
    }


    public WordsAdapter(ArrayList<String> wordList, GameActivity activity, long time, GridLayoutManager layoutManager, RecyclerView recyclerView) {
        this.wordList = wordList;
        this.mGameActivity = activity;
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
        mLayoutManager.setSpanCount(pattern[currentStep]);
    }

    @Override
    public WordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_row, parent, false);
        return mHolder = new WordsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordsViewHolder holder, int position) {
        holder.waveLoadingView.setCenterTitle(subList.get(position).getText());

        if (holder.waveLoadingView.mCountDownTimer != null) {
            holder.waveLoadingView.mCountDownTimer.cancel();
        }

        holder.waveLoadingView.startCountDown(mGameActivity, time, position, this, subList.get(position).isStopped());
        subList.get(position).setWaveLoadingView(holder.waveLoadingView);
    }


    public void cancel() {
        isPause = true;
        // notifyDataSetChanged();
    }

    public void addItem() {
        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            subList.add(new Item(wordList.get(currentIndex)));
            notifyItemInserted(subList.size() - 1);
        }
    }

    public void replaceItem(final int position) {
        if (currentIndex < wordList.size() - 1) {
            currentIndex++;
            subList.set(position, new Item(wordList.get(currentIndex)));
            notifyItemChanged(position);
        }
    }

    /**
     * stop the count of the current word
     *
     * @param position
     */
    public void stopItem(int position) {
        subList.get(position).setIsStopped(true);
        notifyItemChanged(position);
    }

    private void update() {
        //  notifyItemChanged(0);
        Log.d("natija", "update");
        recyclerView.setLayoutManager(mLayoutManager);
        notifyDataSetChanged();
        // this.onBindViewHolder(mHolder,0);
    }

    /**
     * what to do next on word hit
     *
     * @param position the position of the hit word
     */
    public void next(int position) {
        if (!isPause) { //if not paused
            if (currentStep < pattern.length) { //if current step exists
                solvedByStep[currentStep]++; //increase number of solved words in this step
                stopItem(position); //stop counting time for the solved word
                if (solvedByStep[currentStep] >= pattern[currentStep]) {//if  last word to solve

                    subList.get(position).getWaveLoadingView().wordHit(this, mGameActivity);

                    reSizeLayoutManager();
                    if (currentStep + 1 < pattern.length) {//if next step exists
                        currentStep++;//move to next step
                        if (pattern[currentStep - 1] == pattern[currentStep]) { //if number of words in current step == number of words in next step
                            for (int i = 0; i < pattern[currentStep]; i++) { //replace each word
                                replaceItem(i);
                            }
                        } else { //if number of words in current step != number of words in next step
                            for (int i = 0; i < pattern[currentStep - 1]; i++) {
                                replaceItem(i);//replace the existing words
                            }
                            for (int i = 0; i < pattern[currentStep] - pattern[currentStep - 1]; i++) {
                                addItem();//and add new items by number diff
                            }
                        }
                    } else {//if next step does not exist ( LEVEL FINISHED )
                        mGameActivity.finish();
                    }
                }

            }
        } else {//if current step does not exist

            mGameActivity.finish();
            mGameActivity.levelFinished(1);
            //WON THE CURRENT LEVEL
        }
    }


    public boolean isWordHit(String typed) {
        for (int i = 0; i < subList.size(); i++) {
            if (typed.equalsIgnoreCase(subList.get(i).getText())) {
                SwissArmyKnife.playSound(mGameActivity, R.raw.mario);
                next(i);
                return true;
            }
        }
        return false;
    }

    public void gameOver() {
        final Intent intent = new Intent(mGameActivity, MainMenuActivity.class);
        mGameActivity.startActivity(intent);
    }

    private void reSizeLayoutManager() {

        if (currentStep + 1 < pattern.length) { //if next step exists

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
