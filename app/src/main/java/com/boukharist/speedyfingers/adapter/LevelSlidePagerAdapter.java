package com.boukharist.speedyfingers.adapter;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.activities.GameActivity;
import com.boukharist.speedyfingers.activities.MainMenuActivity;
import com.boukharist.speedyfingers.custom.animation.WaveCompat;
import com.boukharist.speedyfingers.custom.animation.WaveDrawable;
import com.boukharist.speedyfingers.custom.animation.WaveTouchHelper;
import com.boukharist.speedyfingers.model.LevelPagerEnum;
import com.boukharist.speedyfingers.utils.Constants;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by ayoub.boukhari on 02/02/2016.
 */

public class LevelSlidePagerAdapter extends PagerAdapter implements View.OnClickListener {

    private MainMenuActivity mActivity;


    public LevelSlidePagerAdapter(MainMenuActivity activity) {
        mActivity = activity;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View page = mActivity.getLayoutInflater().inflate(R.layout.level, container, false);


        Button button = (Button) page.findViewById(R.id.button_level);


        int titleResId = LevelPagerEnum.values()[position].getTitleResId();
        int colorResId = LevelPagerEnum.values()[position].getColorResId();

        VectorDrawable vectorDrawable = (VectorDrawable) button.getBackground();
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(ContextCompat.getColor(mActivity, colorResId),
                PorterDuff.Mode.SRC_ATOP);
        vectorDrawable.setColorFilter(porterDuffColorFilter);

        button.setText(titleResId);
        container.addView(page);


        WaveTouchHelper.bindWaveTouchHelper(button, new WaveTouchHelper.OnWaveTouchHelperListener() {
            @Override
            public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {
                click(view, position, locationInView);
            }
        });


        return (page);
    }

    private void click(View view, int position, final Point locationInScreen) {
        String difficulty = null;
        long time = 0;
        int[] pattern = new int[10];
        switch (position) {
            case 0:
                difficulty = "easy";
                time = Constants.COUNTDOWN_TIME_EASY;
                pattern = Constants.PATTERN_LEVEL_1;
                break;
            case 1:
                difficulty = "easy";
                time = Constants.COUNTDOWN_TIME_EASY;
                pattern = Constants.PATTERN_LEVEL_2;
                break;
            case 2:
                difficulty = "easy";
                time = Constants.COUNTDOWN_TIME_EASY;
                pattern = Constants.PATTERN_LEVEL_3;
                break;
            case 3:
                difficulty = "easy";
                time = Constants.COUNTDOWN_TIME_EASY;
                pattern = Constants.PATTERN_LEVEL_4;
                break;
            case 4:
                difficulty = "easy";
                time = Constants.COUNTDOWN_TIME_EASY;
                pattern = Constants.PATTERN_LEVEL_5;
                break;
        }

        int color = LevelPagerEnum.values()[position].getColorResId();
        final Intent intent = generateIntent(difficulty, time, pattern, ContextCompat.getColor(mActivity, color));


        WaveCompat.startWaveFilter(mActivity,
                new WaveDrawable()
                        .setColor(ContextCompat.getColor(mActivity, color))
                        .setTouchPoint(locationInScreen),
                intent);

        SmallBang.attach2Window(mActivity).bang(view, new SmallBangListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }


    private Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, View view, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(mActivity, color));
        anim.start();
        return anim;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return LevelPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void onClick(View v) {

    }

    private Intent generateIntent(String difficulty, long time, int[] pattern, int color) {
        Intent intent = new Intent(mActivity, GameActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("time", time);
        intent.putExtra("pattern", pattern);
        intent.putExtra(WaveCompat.IntentKey.BACKGROUND_COLOR, color);

        return intent;
    }


}
