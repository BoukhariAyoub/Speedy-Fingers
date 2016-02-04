package com.boukharist.speedyfingers.model;

import android.content.Context;
import android.util.Log;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.utils.PrefUtils;

import java.util.Arrays;

/**
 * Created by ayoub.boukhari on 03/02/2016.
 */
public class Level {

    private String title;
    int color;
    int[] pattern;
    String achievementKey;
    private int score;
    boolean cleared = false;
    boolean playable = false;

    public Level(String title, int score, int color, int[] pattern, String achievementKey, boolean cleared, boolean playable) {
        this(title, color, pattern, achievementKey);
        this.score = score;
        this.cleared = cleared;
        this.playable = playable;
    }

    public Level(String title, int color, int[] pattern, String achievementKey) {
        this.title = title;
        this.color = color;
        this.pattern = pattern;
        this.achievementKey = achievementKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getPattern() {
        return pattern;
    }

    public void setPattern(int[] pattern) {
        this.pattern = pattern;
    }

    public String getAchievementKey() {
        return achievementKey;
    }

    public void setAchievementKey(String achievementKey) {
        this.achievementKey = achievementKey;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    @Override
    public String toString() {
        return "Level{" +
                "title='" + title + '\'' +
                ", color=" + color +
                ", pattern=" + Arrays.toString(pattern) +
                ", achievementKey='" + achievementKey + '\'' +
                ", score=" + score +
                ", cleared=" + cleared +
                ", playable=" + playable +
                '}';
    }

    public static Level createLevel(Context context, int position) {

        Level existingLevel = PrefUtils.getLevelProgression(context, position);
        if (existingLevel != null) {
            return existingLevel;
        }


        LevelPagerEnum levelPagerEnum = LevelPagerEnum.values()[position];
        String title = context.getResources().getStringArray(R.array.level_titles)[position];
        int color = levelPagerEnum.getColor();
        int[] pattern = levelPagerEnum.getPattern();
        String achievementKey = context.getString(levelPagerEnum.getAchievementKey());
        Level newLevel = new Level(title, color, pattern, achievementKey);
        if (position == 0) {
            newLevel.setPlayable(true);
        }

        PrefUtils.putLevelProgression(context, newLevel);

        Log.d("natija", "level at " + position + " : " + newLevel);
        return newLevel;
    }
}

