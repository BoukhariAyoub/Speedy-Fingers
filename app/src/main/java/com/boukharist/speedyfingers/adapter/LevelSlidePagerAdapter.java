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
import com.boukharist.speedyfingers.model.Level;
import com.boukharist.speedyfingers.model.LevelPagerEnum;
import com.boukharist.speedyfingers.utils.Constants;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;

import java.util.List;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by ayoub.boukhari on 02/02/2016.
 */

public class LevelSlidePagerAdapter extends PagerAdapter implements View.OnClickListener {

    private MainMenuActivity mActivity;
    List<Level> mLevelList;

    public LevelSlidePagerAdapter(MainMenuActivity activity, List<Level> levels) {
        mActivity = activity;
        mLevelList = levels;
    }

    @Override
    public float getPageWidth(int position) {
        return 1f;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View page = mActivity.getLayoutInflater().inflate(R.layout.level, container, false);


        Button button = (Button) page.findViewById(R.id.button_level);
        Button lockTextView = (Button) page.findViewById(R.id.lock);

        SwissArmyKnife.setFontawesomeContainer(Constants.FONT_AWESOME_PATH, lockTextView);

        final Level currentLevel = mLevelList.get(position);
        final Level nextLevel = position < mLevelList.size() - 1 ? mLevelList.get(position + 1) : null;


        String titleResId = currentLevel.getTitle();
        int colorResId = currentLevel.getColor();

        VectorDrawable vectorDrawable = (VectorDrawable) button.getBackground();
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(ContextCompat.getColor(mActivity, colorResId),
                PorterDuff.Mode.SRC_ATOP);
        vectorDrawable.setColorFilter(porterDuffColorFilter);

        button.setText(titleResId);

        container.addView(page);


        if (currentLevel.isPlayable()) {
            if (position > mActivity.mLastLevel) {
                mActivity.mLastLevel = position;
            }
            lockTextView.setVisibility(View.GONE);
            WaveTouchHelper.bindWaveTouchHelper(button, new WaveTouchHelper.OnWaveTouchHelperListener() {
                @Override
                public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {
                    click(view, currentLevel, nextLevel, locationInView);
                }
            });
        } else {
            button.setText(R.string.fa_lock);
            SwissArmyKnife.setFontawesomeContainer(Constants.FONT_AWESOME_PATH, button);
            button.setTextSize(40);
        }


        return (page);
    }

    private void click(View view, Level level, Level nextLevel, final Point locationInScreen) {
        final Intent intent = generateIntent(level, nextLevel);


        WaveCompat.startWaveFilter(mActivity,
                new WaveDrawable()
                        .setColor(ContextCompat.getColor(mActivity, level.getColor()))
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

    private Intent generateIntent(Level level, Level nextLevel) {
        Intent intent = new Intent(mActivity, GameActivity.class);
        intent.putExtra("level", SwissArmyKnife.getJsonFromObject(level));
        intent.putExtra("next_level", SwissArmyKnife.getJsonFromObject(nextLevel));
        intent.putExtra(WaveCompat.IntentKey.BACKGROUND_COLOR, ContextCompat.getColor(mActivity, level.getColor()));

        return intent;
    }


}
