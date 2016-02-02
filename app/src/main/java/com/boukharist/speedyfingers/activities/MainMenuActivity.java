package com.boukharist.speedyfingers.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.adapter.LevelSlidePagerAdapter;
import com.boukharist.speedyfingers.custom.animation.WaveTouchHelper;
import com.boukharist.speedyfingers.utils.BaseGameUtils;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, WaveTouchHelper.OnWaveTouchHelperListener {


    /**
     * The number of pages  to show.
     */
    private static final int NUM_PAGES = 5;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;


    private static final int REQUEST_ACHIEVEMENTS = 9002;

    // Client used to interact with Google APIs.
    public static GoogleApiClient GoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    final static String TAG = MainMenuActivity.class.getSimpleName();


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    @Bind(R.id.levelViewPager)
    ViewPager mPager;


    @Bind(R.id.setting_info)
    Button mInfoButton;
    @Bind(R.id.setting_leader_board)
    Button mLeaderBoardButton;
    @Bind(R.id.setting_achievement)
    Button mAchievementButton;


    @Bind(R.id.sign_in_button)
    SignInButton mGoogleSignInButton;



    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        if (GoogleApiClient == null) {
            // Create the Google Api Client with access to Plus and Games
            GoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

        if (GoogleApiClient.isConnected()) {
            mGoogleSignInButton.setVisibility(View.GONE);
            GoogleApiClient.getConnectionResult(Games.API);
        } else {
            mGoogleSignInButton.setOnClickListener(this);
            mGoogleSignInButton.setVisibility(View.VISIBLE);
        }


        mAchievementButton.setOnClickListener(this);

        SwissArmyKnife.setFontawesomeContainer("fonts/fontawesome.ttf", mInfoButton, mLeaderBoardButton,mAchievementButton);

        mPagerAdapter = new LevelSlidePagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);

        mPager.setClipToPadding(false);
        mPager.setPadding(180, 0, 180, 0);
        mPager.setPageMargin(50);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mGoogleSignInButton.getId()) {
            GoogleApiClient.connect();
        }
        if (v.getId() == mAchievementButton.getId()) {
            if(GoogleApiClient.isConnected()){
                startActivityForResult(Games.Achievements.getAchievementsIntent(GoogleApiClient),
                        REQUEST_ACHIEVEMENTS);
            }

        }


    }

    @Override
    public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        GoogleApiClient.connect();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }


        mResolvingConnectionFailure = true;

        // Attempt to resolve the connection failure using BaseGameUtils.
        // The R.string.signin_other_error value should reference a generic
        // error string in your strings.xml file, such as "There was
        // an issue with sign in, please try again later."
        if (!BaseGameUtils.resolveConnectionFailure(this,
                GoogleApiClient, connectionResult,
                RC_SIGN_IN, getString(R.string.signin_other_error))) {
            mResolvingConnectionFailure = false;
        }
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult.getErrorMessage());
    }


    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (responseCode == RESULT_OK) {
                GoogleApiClient.connect();
            } else {
                Log.d(TAG, "sign in error other " + responseCode);
            }
        }

        if(requestCode == REQUEST_ACHIEVEMENTS){
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
        }
    }


}
