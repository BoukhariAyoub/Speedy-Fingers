package com.ayoub.speedyfingers;

/**
 * Created by ayoub.boukhari on 28/01/2016.
 */
public enum LevelPagerEnum {


    LEVEL1(R.string.level1, R.color.md_amber_100),
    LEVEL2(R.string.level2, R.color.md_amber_200),
    LEVEL3(R.string.level3, R.color.md_amber_300),
    LEVEL4(R.string.level4, R.color.md_amber_400),
    LEVEL5(R.string.level5, R.color.md_amber_600);

    private int mTitleResId;
    private int mColorResId;

    LevelPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mColorResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getColorResId() {
        return mColorResId;
    }
}
