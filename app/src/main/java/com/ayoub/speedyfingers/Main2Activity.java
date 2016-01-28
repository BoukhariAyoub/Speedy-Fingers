package com.ayoub.speedyfingers;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

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

    @Bind(R.id.setting_info)
    Button mSettingInfo;
    @Bind(R.id.setting_leader_board)
    Button mSettingLeaderBoard;
    @Bind(R.id.setting_statistics)
    Button mSettingStatistics;

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


        SwissArmyKnife.setFontawesomeContainer("fonts/fontawesome.ttf",mSettingInfo,mSettingLeaderBoard,mSettingStatistics);

        mPagerAdapter = new LevelSlidePagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);

        mPager.setClipToPadding(false);
        mPager.setPadding(180, 0, 180, 0);
    }

    @Override
    public void onClick(View v) {


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


            Button button = (Button) page.findViewById(R.id.button_level);
            RelativeLayout ribbonLayout = (RelativeLayout) page.findViewById(R.id.ribbon_layout);


            int titleResId = LevelPagerEnum.values()[position].getTitleResId();
            int colorResId = LevelPagerEnum.values()[position].getColorResId();

            VectorDrawable vectorDrawable = (VectorDrawable) button.getBackground();
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(ContextCompat.getColor(mContext, colorResId),
                    PorterDuff.Mode.SRC_ATOP);
            vectorDrawable.setColorFilter(porterDuffColorFilter);

            button.setText(titleResId);
            container.addView(page);


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(view, position);
                }
            };

            button.setOnClickListener(clickListener);


            return (page);
        }

        private void click(View view, int position) {


            final Intent intent = new Intent(Main2Activity.this, GameActivity.class);
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




            SmallBang.attach2Window(Main2Activity.this).bang(view, new SmallBangListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    startActivity(intent);
                }
            });

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
