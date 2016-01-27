package com.ayoub.speedyfingers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {


    /**
     * The number of pages  to show.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    @Bind(R.id.levelViewPager)
    ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mPagerAdapter = new LevelSlidePagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);

        mPager.setClipToPadding(false);
        mPager.setPadding(180, 0, 180, 0);
    }

    @Override
    public void onClick(View v) {


    }

    public enum CustomPagerEnum {

        LEVEL1(R.string.level1, R.layout.level),
        LEVEL2(R.string.level2, R.layout.level),
        LEVEL3(R.string.level3, R.layout.level),
        LEVEL4(R.string.level4, R.layout.level),
        LEVEL5(R.string.level5, R.layout.level);

        private int mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class LevelSlidePagerAdapter extends PagerAdapter implements View.OnClickListener {

        private Context mContext;


        public LevelSlidePagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public float getPageWidth(int position) {
            return 1f;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View page = getLayoutInflater().inflate(R.layout.level, container, false);
            TextView textview = (TextView) page.findViewById(R.id.title);
            RelativeLayout ribbonLayout = (RelativeLayout) page.findViewById(R.id.ribbon_layout);
            textview.setText(CustomPagerEnum.values()[position].mTitleResId);
            container.addView(page);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(position);
                }
            };

            textview.setOnClickListener(clickListener);


            return (page);
        }

        private void click(int position) {
            Intent intent = new Intent(Main2Activity.this, GameActivity.class);
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


            intent.putExtra("difficulty", difficulty);
            intent.putExtra("time", time);
            intent.putExtra("pattern", pattern);




         /*   intent.putExtra("difficulty", "medium");
            intent.putExtra("time", Constants.COUNTDOWN_TIME_MEDIUM);


            intent.putExtra("difficulty", "hard");
            intent.putExtra("time", Constants.COUNTDOWN_TIME_HARD); */


            startActivity(intent);

        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void onClick(View v) {

        }
    }


}
