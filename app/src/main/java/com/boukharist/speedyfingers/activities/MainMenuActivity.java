package com.boukharist.speedyfingers.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.adapter.LevelSlidePagerAdapter;
import com.boukharist.speedyfingers.custom.animation.WaveCompat;
import com.boukharist.speedyfingers.custom.animation.WaveTouchHelper;
import com.boukharist.speedyfingers.model.Level;
import com.boukharist.speedyfingers.utils.BaseGameUtils;
import com.boukharist.speedyfingers.utils.PrefUtils;
import com.boukharist.speedyfingers.utils.SwissArmyKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, WaveTouchHelper.OnWaveTouchHelperListener, RoomUpdateListener, RoomStatusUpdateListener, OnInvitationReceivedListener, RealTimeMessageReceivedListener {


    public static MainMenuActivity mInstance;


    final static int MIN_PLAYERS = 2;


    public static boolean MULTIPLAYER_MODE_ON = false;

    /**
     * Request code used to invoke multiplayers
     */
    final static int RC_SELECT_PLAYERS = 10000;

    final static int RC_INVITATION_INBOX = 10001;

    final static int RC_WAITING_ROOM = 10002;


    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;


    private static final int RC_ACHIEVEMENTS = 9002;

    // Client used to interact with Google APIs.
    public static GoogleApiClient mGoogleApiClient;

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

        if (mGoogleApiClient == null) {
            // Create the Google Api Client with access to Plus and Games
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }


        mLeaderBoardButton.setOnClickListener(this);
        mAchievementButton.setOnClickListener(this);
        mInfoButton.setOnClickListener(this);

        SwissArmyKnife.setFontawesomeContainer("fonts/fontawesome.ttf", mInfoButton, mLeaderBoardButton, mAchievementButton);

        mPager.setClipToPadding(false);
        mPager.setPadding(180, 0, 180, 0);
        mPager.setPageMargin(50);

        mInstance = this;

    }


    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) ++connectedPlayers;
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mGoogleSignInButton.getId()) {

            mGoogleApiClient.connect();
        }
        if (v.getId() == mLeaderBoardButton.getId()) {
            Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(MainMenuActivity.mGoogleApiClient, 1, 1);
            startActivityForResult(intent, RC_SELECT_PLAYERS);
        }
        if (v.getId() == mAchievementButton.getId()) {
            if (mGoogleApiClient.isConnected()) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                        RC_ACHIEVEMENTS);

            }
        }
        if (v.getId() == mInfoButton.getId()) {
            PrefUtils.clearPrefs(this);
            onResume();
        }

    }

    public int mLastLevel;

    @Override
    public void onResume() {
        super.onResume();

        mPagerAdapter = new LevelSlidePagerAdapter(this, initLevels());
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(mLastLevel);


        if (mGoogleApiClient.isConnected()) {
            mGoogleSignInButton.setVisibility(View.GONE);
        } else {
            mGoogleApiClient.connect();
            mGoogleSignInButton.setOnClickListener(this);
            mGoogleSignInButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {

    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");

    //    Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Invitation inv =
                    connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);

            if (inv != null) {
                // accept invitation
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId());
                Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

                // prevent screen from sleeping during handshake
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // go to game screen
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();

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
                mGoogleApiClient, connectionResult,
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
                mGoogleApiClient.connect();
            } else {
                Log.d(TAG, "sign in error other " + responseCode);
            }
        }

        if (requestCode == RC_ACHIEVEMENTS) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
        }


        if (requestCode == RC_SELECT_PLAYERS) {
            if (responseCode != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            // get the invitee list
            Bundle extras = intent.getExtras();
            final ArrayList<String> invitees =
                    intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get auto-match criteria
            Bundle autoMatchCriteria = null;
            int minAutoMatchPlayers =
                    intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers =
                    intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }


            // create the room and specify a variant if appropriate
            RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
            roomConfigBuilder.addPlayersToInvite(invitees);
            if (autoMatchCriteria != null) {
                roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
            }
            RoomConfig roomConfig = roomConfigBuilder.build();
            Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

            // prevent screen from sleeping during handshake
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        if (requestCode == RC_INVITATION_INBOX) {
            if (responseCode != Activity.RESULT_OK) {
                // canceled
                return;
            }

            // get the selected invitation
            Bundle extras = intent.getExtras();
            Invitation invitation =
                    extras.getParcelable(Multiplayer.EXTRA_INVITATION);

            // accept it!
            RoomConfig roomConfig = makeBasicRoomConfigBuilder()
                    .setInvitationIdToAccept(invitation.getInvitationId())
                    .build();
            Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfig);

            // prevent screen from sleeping during handshake
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // go to game screen
        }
    }


    public List<Level> initLevels() {
        List<Level> levels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            levels.add(Level.createLevel(this, i));
        }
        return levels;
    }



    /* MULTIPLAYER HANDLING  */

    private void startQuickGame() {
        // auto-match criteria to invite one random automatch opponent.
        // You can also specify more opponents (up to 3).
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        // build the room config:
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        // create room:
        Games.RealTimeMultiplayer.create(MainMenuActivity.mGoogleApiClient, roomConfig);

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // go to game screen
    }


    private RoomConfig.Builder makeBasicRoomConfigBuilder() {

        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }


    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d("natija", "onRoomCreated " + statusCode);

        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // display error
            return;
        }

        // get waiting room intent
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d("natija", "onJoinedRoom ");

        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            return;
        }

        // get waiting room intent
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);
        startActivityForResult(i, RC_WAITING_ROOM);
    }

    @Override
    public void onLeftRoom(int statusCode, String s) {
        Log.d("natija", "onLeftRoom " + s);

    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d("natija", "onRoomConnected ");

        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.d("natija", "onRoomConnected " + room.getRoomId());
            // show error message, return to main screen.
        }


    }


    @Override
    public void onRoomConnecting(Room room) {
        Log.d("natija", "onRoomConnecting " + room.getRoomId());

    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.d("natija", "onRoomAutoMatching " + room.getRoomId());

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.d("natija", "onPeerInvitedToRoom " + room.getRoomId());

    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.d("natija", "onPeerDeclined " + room.getRoomId());

    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.d("natija", "onPeerJoined " + room.getRoomId());

    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.d("natija", "onPeerLeft " + room.getRoomId());

    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d("natija", "onConnectedToRoom " + room.getRoomId());

    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.d("natija", "onDisconnectedFromRoom " + room.getRoomId());

    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.d("natija", "onPeersConnected " + room.getRoomId());

        if (shouldStartGame(room)) {
            startGame(room);
        }

    }


    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.d("natija", "onPeersDisconnected " + room.getRoomId());

    }

    @Override
    public void onP2PConnected(String s) {
        Log.d("natija", "onP2PConnected " + s);

    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.d("natija", "onP2PDisconnected " + s);

    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Log.d("natija", "onInvitationReceived " + invitation.getInvitationId());
        Intent intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_INVITATION_INBOX);
        // mIncomingInvitationId = invitation.getInvitationId();


    }

    @Override
    public void onInvitationRemoved(String s) {
        Log.d("natija", "onInvitationRemoved " + s);
    }


    private void startGame(Room room) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", SwissArmyKnife.getJsonFromObject(Level.createLevel(this, 0)));
        intent.putExtra("next_level", SwissArmyKnife.getJsonFromObject(Level.createLevel(this, 1)));
        intent.putExtra(WaveCompat.IntentKey.BACKGROUND_COLOR, ContextCompat.getColor(this, R.color.md_blue_grey_500));
        if (room != null)
            intent.putExtra("room", room);

        startActivity(intent);
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        Log.d("natija", "onRealTimeMessageReceived" + String.valueOf(realTimeMessage));
        GameActivity.mGameActivity.finish();
        Toast.makeText(this, "YOU LOSE", Toast.LENGTH_LONG).show();
    }

    public void leaveRoom(Room room) {
        // leave room
        if (room != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, room.getRoomId());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }
}
