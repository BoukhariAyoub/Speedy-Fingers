package com.boukharist.speedyfingers.model;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.utils.Constants;

/**
 * Created by ayoub.boukhari on 28/01/2016.
 */
public enum LevelPagerEnum {

    LEVEL1(R.color.md_amber_300,Constants.PATTERN_LEVEL_1,R.string.achievement_level_1_cleared),
    LEVEL2(R.color.md_amber_400,Constants.PATTERN_LEVEL_2,R.string.achievement_level_2_cleared),
    LEVEL3(R.color.md_amber_500,Constants.PATTERN_LEVEL_3,R.string.achievement_level_3_cleared),
    LEVEL4(R.color.md_amber_600,Constants.PATTERN_LEVEL_4,R.string.achievement_level_4_cleared),
    LEVEL5(R.color.md_amber_700,Constants.PATTERN_LEVEL_5,R.string.achievement_level_5_cleared);

    private int color;
    private int[] pattern;
    private int achievementKey;

    LevelPagerEnum(int color, int[] pattern, int achievementKey) {
        this.color = color;
        this.pattern = pattern;
        this.achievementKey = achievementKey;
    }


    public int getColor() {
        return color;
    }

    public int[] getPattern() {
        return pattern;
    }

    public int getAchievementKey() {
        return achievementKey;
    }
}
