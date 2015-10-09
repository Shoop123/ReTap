package com.custom.retap.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.custom.retap.R;
import com.custom.retap.additions.Achievements;
import com.custom.retap.additions.CircleCreator;
import com.custom.retap.additions.ThemeManager;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.ColorCircle;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.purplebrain.giftiz.sdk.GiftizSDK;

public class MyActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static SharedPreferences savedData;

    public static Resources res;

    private static SharedPreferences.Editor editor;

    private static TextView tvExplain;
    private static TextView tvScore;
    private static TextView tvBest;
    private static ImageView imgLives1;
    private static ImageView imgLives2;
    private static ImageView imgLives3;
    private static ImageView imgPause;
    private static ImageView imgBack;
    private static ImageView ssImgLivesP11;
    private static ImageView ssImgLivesP12;
    private static ImageView ssImgLivesP13;
    private static ImageView ssImgLivesP21;
    private static ImageView ssImgLivesP22;
    private static ImageView ssImgLivesP23;
    private static TextView ssTvIndicatorP1;
    private static TextView ssTvIndicatorP2;

    private static android.os.Handler handler;

    private final Input input = new Input();

    public static MyActivity activity;

    private static final int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    public static boolean mSignInClicked = false;

    public static boolean mSignInFlow = false;

    public GoogleApiClient mGoogleApiClient;

    public static final int IMMERSIVE_MODE_MIN_SDK = 19;

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mSignInFlow && savedData.getBoolean(res.getString(R.string.autoSignInOption), true)) {
            // auto sign in
            mSignInFlow = true;
            mGoogleApiClient.connect();
        }

        activity = this;

        ThemeManager.retrieve();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_my);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        tvExplain = (TextView) findViewById(R.id.tvExplain);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBest = (TextView) findViewById(R.id.tvBest);
        imgLives1 = (ImageView) findViewById(R.id.life1);
        imgLives2 = (ImageView) findViewById(R.id.life2);
        imgLives3 = (ImageView) findViewById(R.id.life3);
        imgPause = (ImageView) findViewById(R.id.imgPause);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        ssImgLivesP11 = (ImageView) findViewById(R.id.imgLivesP11);
        ssImgLivesP12 = (ImageView) findViewById(R.id.imgLivesP12);
        ssImgLivesP13 = (ImageView) findViewById(R.id.imgLivesP13);
        ssImgLivesP21 = (ImageView) findViewById(R.id.imgLivesP21);
        ssImgLivesP22 = (ImageView) findViewById(R.id.imgLivesP22);
        ssImgLivesP23 = (ImageView) findViewById(R.id.imgLivesP23);
        ssTvIndicatorP1 = (TextView) findViewById(R.id.tvIndicatorP1);
        ssTvIndicatorP2 = (TextView) findViewById(R.id.tvIndicatorP2);

        imgPause.setOnTouchListener(new Pause());
        imgBack.setOnTouchListener(new Back());

        handler = new Handler();

        savedData = getPreferences(Context.MODE_PRIVATE);

        res = getResources();

        Game game = new Game(this, input);

        SurfaceView view;
        view = (SurfaceView) findViewById(R.id.game);
        view.getHolder().addCallback(game);
        view.setOnTouchListener(input);
    }

    public static boolean hasConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MyActivity.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setText(int id, final String text) {

        switch (id) {

            case R.id.tvExplain:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvExplain.setText(text);
                    }
                });
                break;
            case R.id.tvBest:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvBest.setText("Best: " + text);
                    }
                });
                break;
            case R.id.tvScore:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvScore.setText(text);
                    }
                });
                break;
            case R.id.tvIndicatorP1:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssTvIndicatorP1.setText(text);
                    }
                });
                break;
            case R.id.tvIndicatorP2:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssTvIndicatorP2.setText(text);
                    }
                });
                break;
        }
    }

    public static void setImageSource(int id, final int srcID) {

        switch (id) {

            case R.id.imgPause:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgPause.setImageResource(srcID);
                    }
                });
                break;
            case R.id.life1:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgLives1.setImageResource(srcID);
                        MyActivity.imgLives2.setImageResource(srcID);
                        MyActivity.imgLives3.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgBack:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgBack.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP11:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP11.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP12:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP12.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP13:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP13.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP21:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP21.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP22:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP22.setImageResource(srcID);
                    }
                });
                break;
            case R.id.imgLivesP23:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP23.setImageResource(srcID);
                    }
                });
                break;
        }

    }

    public static void setImageColor(final int COLOR) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (COLOR == ThemeManager.colors[0]) {
                    imgBack.setImageResource(R.drawable.back);
                    imgLives1.setImageResource(R.drawable.lives);
                    imgLives2.setImageResource(R.drawable.lives);
                    imgLives3.setImageResource(R.drawable.lives);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play);
                    else imgPause.setImageResource(R.drawable.pause);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives);
                        ssImgLivesP12.setImageResource(R.drawable.lives);
                        ssImgLivesP13.setImageResource(R.drawable.lives);
                        ssImgLivesP21.setImageResource(R.drawable.lives);
                        ssImgLivesP22.setImageResource(R.drawable.lives);
                        ssImgLivesP23.setImageResource(R.drawable.lives);
                    }
                } else if (COLOR == ThemeManager.colors[1]) {
                    imgBack.setImageResource(R.drawable.back_blue);
                    imgLives1.setImageResource(R.drawable.lives_blue);
                    imgLives2.setImageResource(R.drawable.lives_blue);
                    imgLives3.setImageResource(R.drawable.lives_blue);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_blue);
                    else imgPause.setImageResource(R.drawable.pause_blue);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_blue);
                        ssImgLivesP12.setImageResource(R.drawable.lives_blue);
                        ssImgLivesP13.setImageResource(R.drawable.lives_blue);
                        ssImgLivesP21.setImageResource(R.drawable.lives_blue);
                        ssImgLivesP22.setImageResource(R.drawable.lives_blue);
                        ssImgLivesP23.setImageResource(R.drawable.lives_blue);
                    }
                } else if (COLOR == ThemeManager.colors[2]) {
                    imgBack.setImageResource(R.drawable.back_red);
                    imgLives1.setImageResource(R.drawable.lives_red);
                    imgLives2.setImageResource(R.drawable.lives_red);
                    imgLives3.setImageResource(R.drawable.lives_red);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_red);
                    else imgPause.setImageResource(R.drawable.pause_red);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_red);
                        ssImgLivesP12.setImageResource(R.drawable.lives_red);
                        ssImgLivesP13.setImageResource(R.drawable.lives_red);
                        ssImgLivesP21.setImageResource(R.drawable.lives_red);
                        ssImgLivesP22.setImageResource(R.drawable.lives_red);
                        ssImgLivesP23.setImageResource(R.drawable.lives_red);
                    }
                } else if (COLOR == ThemeManager.colors[3]) {
                    imgBack.setImageResource(R.drawable.back_green);
                    imgLives1.setImageResource(R.drawable.lives_green);
                    imgLives2.setImageResource(R.drawable.lives_green);
                    imgLives3.setImageResource(R.drawable.lives_green);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_green);
                    else imgPause.setImageResource(R.drawable.pause_green);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_green);
                        ssImgLivesP12.setImageResource(R.drawable.lives_green);
                        ssImgLivesP13.setImageResource(R.drawable.lives_green);
                        ssImgLivesP21.setImageResource(R.drawable.lives_green);
                        ssImgLivesP22.setImageResource(R.drawable.lives_green);
                        ssImgLivesP23.setImageResource(R.drawable.lives_green);
                    }
                } else if (COLOR == ThemeManager.colors[4]) {
                    imgBack.setImageResource(R.drawable.back_orange);
                    imgLives1.setImageResource(R.drawable.lives_orange);
                    imgLives2.setImageResource(R.drawable.lives_orange);
                    imgLives3.setImageResource(R.drawable.lives_orange);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_orange);
                    else imgPause.setImageResource(R.drawable.pause_orange);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_orange);
                        ssImgLivesP12.setImageResource(R.drawable.lives_orange);
                        ssImgLivesP13.setImageResource(R.drawable.lives_orange);
                        ssImgLivesP21.setImageResource(R.drawable.lives_orange);
                        ssImgLivesP22.setImageResource(R.drawable.lives_orange);
                        ssImgLivesP23.setImageResource(R.drawable.lives_orange);
                    }
                } else if (COLOR == ThemeManager.colors[5]) {
                    imgBack.setImageResource(R.drawable.back_purple);
                    imgLives1.setImageResource(R.drawable.lives_purple);
                    imgLives2.setImageResource(R.drawable.lives_purple);
                    imgLives3.setImageResource(R.drawable.lives_purple);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_purple);
                    else imgPause.setImageResource(R.drawable.pause_purple);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_purple);
                        ssImgLivesP12.setImageResource(R.drawable.lives_purple);
                        ssImgLivesP13.setImageResource(R.drawable.lives_purple);
                        ssImgLivesP21.setImageResource(R.drawable.lives_purple);
                        ssImgLivesP22.setImageResource(R.drawable.lives_purple);
                        ssImgLivesP23.setImageResource(R.drawable.lives_purple);
                    }
                } else if (COLOR == ThemeManager.colors[6]) {
                    imgBack.setImageResource(R.drawable.back_pink);
                    imgLives1.setImageResource(R.drawable.lives_pink);
                    imgLives2.setImageResource(R.drawable.lives_pink);
                    imgLives3.setImageResource(R.drawable.lives_pink);
                    if (Game.state == GameState.Paused)
                        imgPause.setImageResource(R.drawable.play_pink);
                    else imgPause.setImageResource(R.drawable.pause_pink);
                    if(Game.splitScreenMode){
                        ssImgLivesP11.setImageResource(R.drawable.lives_pink);
                        ssImgLivesP12.setImageResource(R.drawable.lives_pink);
                        ssImgLivesP13.setImageResource(R.drawable.lives_pink);
                        ssImgLivesP21.setImageResource(R.drawable.lives_pink);
                        ssImgLivesP22.setImageResource(R.drawable.lives_pink);
                        ssImgLivesP23.setImageResource(R.drawable.lives_pink);
                    }
                }
            }
        });
    }

    public static void setTextColor(final int COLOR) {

        handler.post(new Runnable() {
            public void run() {
                tvBest.setTextColor(COLOR);
                tvScore.setTextColor(COLOR);
                tvExplain.setTextColor(COLOR);
                ssTvIndicatorP1.setTextColor(COLOR);
                ssTvIndicatorP2.setTextColor(COLOR);
            }
        });

    }

    public static void setVisible(int id, final int visibility) {

        switch (id) {

            case R.id.tvExplain:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvExplain.setVisibility(visibility);
                    }
                });
                break;
            case R.id.tvScore:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvScore.setVisibility(visibility);
                    }
                });
                break;
            case R.id.tvBest:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.tvBest.setVisibility(visibility);
                    }
                });
                break;
            case R.id.life1:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgLives1.setVisibility(visibility);
                    }
                });
                break;
            case R.id.life2:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgLives2.setVisibility(visibility);
                    }
                });
                break;
            case R.id.life3:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgLives3.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgPause:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgPause.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgBack:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.imgBack.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP11:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP11.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP12:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP12.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP13:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP13.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP21:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP21.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP22:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP22.setVisibility(visibility);
                    }
                });
                break;
            case R.id.imgLivesP23:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssImgLivesP23.setVisibility(visibility);
                    }
                });
                break;
            case R.id.tvIndicatorP1:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssTvIndicatorP1.setVisibility(visibility);
                    }
                });
                break;
            case R.id.tvIndicatorP2:
                handler.post(new Runnable() {
                    public void run() {
                        MyActivity.ssTvIndicatorP2.setVisibility(visibility);
                    }
                });
                break;
        }
    }

    public static int getHighScore() {

        return savedData.getInt(res.getString(R.string.high_score_normal_mode), 0);

    }

    public static int getFrenzyHighScore() {

        return savedData.getInt(res.getString(R.string.high_score_frenzy_mode), 0);

    }

    public static void saveHighScore() {

        editor = savedData.edit();

        editor.putInt(res.getString(R.string.high_score_normal_mode), Game.currentHighScore);

        editor.apply();

    }

    public static void saveTheme() {

        editor = savedData.edit();

        editor.putInt(res.getString(R.string.theme), Draw.color);

        editor.putBoolean(res.getString(R.string.rainbow), Draw.rainbow);

        editor.apply();

    }

    public static int getColorTheme() {

        Draw.rainbow = savedData.getBoolean(res.getString(R.string.rainbow), false);

        return savedData.getInt(res.getString(R.string.theme), Color.BLACK);

    }

    public static void saveFrenzyHighScore() {

        editor = savedData.edit();

        editor.putInt(res.getString(R.string.high_score_frenzy_mode), Game.currentFrenzyHighScore);

        editor.apply();

    }

    public static void saveAutoSignInOption(boolean autoSignIn) {

        editor = savedData.edit();

        editor.putBoolean(res.getString(R.string.autoSignInOption), autoSignIn);

        editor.apply();
    }

    private static void saveUnlockedThemes() {

        editor = savedData.edit();

        editor.putBoolean(res.getString(R.string.blue), ThemeManager.unlocked[1]);
        editor.putBoolean(res.getString(R.string.red), ThemeManager.unlocked[2]);
        editor.putBoolean(res.getString(R.string.green), ThemeManager.unlocked[3]);
        editor.putBoolean(res.getString(R.string.orange), ThemeManager.unlocked[4]);
        editor.putBoolean(res.getString(R.string.purple), ThemeManager.unlocked[5]);
        editor.putBoolean(res.getString(R.string.pink), ThemeManager.unlocked[6]);
        editor.putBoolean(res.getString(R.string.rainbowColor), ThemeManager.unlocked[7]);

        editor.apply();

    }

    public static boolean[] getUnlockedThemes() {

        boolean[] unlocked = new boolean[ThemeManager.unlocked.length];

        unlocked[0] = true;

        unlocked[1] = savedData.getBoolean(res.getString(R.string.blue), false);
        unlocked[2] = savedData.getBoolean(res.getString(R.string.red), false);
        unlocked[3] = savedData.getBoolean(res.getString(R.string.green), false);
        unlocked[4] = savedData.getBoolean(res.getString(R.string.orange), false);
        unlocked[5] = savedData.getBoolean(res.getString(R.string.purple), false);
        unlocked[6] = savedData.getBoolean(res.getString(R.string.pink), false);
        unlocked[7] = savedData.getBoolean(res.getString(R.string.rainbowColor), false);

        return unlocked;

    }

    private void saveData() {

        saveHighScore();

        saveUnlockedThemes();

        saveFrenzyHighScore();

        saveSetting();

    }

    private int getSavedTouchDownTime() {

        return savedData.getInt(res.getString(R.string.currentTouchDownTime), 0);

    }

    private void saveSetting() {

        if (Game.state == GameState.Paused) {

            editor = savedData.edit();

            editor.putInt(res.getString(R.string.currentTouchDownTime), input.touchDownTime);

            editor.apply();

        }

         GameLoop.update.saveGameData();

    }

    public static void saveCurrentGameData(float interval, float currentMaxTime) {
        editor = savedData.edit();

        editor.putFloat(res.getString(R.string.interval), interval);

        editor.putFloat(res.getString(R.string.currentMaxTime), currentMaxTime);

        editor.apply();
    }

    public static float getInterval() {
        return savedData.getFloat(res.getString(R.string.interval), 0);
    }

    public static float getCurrentMaxTime() {
        return savedData.getFloat(res.getString(R.string.currentMaxTime), 0);
    }

    private void cleanUI() {
        if (Build.VERSION.SDK_INT >= IMMERSIVE_MODE_MIN_SDK) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GiftizSDK.onResumeMainActivity(this);

        cleanUI();

        setText(R.id.tvScore, String.valueOf(Game.score));

        if (Game.state == GameState.InGame || Game.state == GameState.Paused) {

            if (Game.frenzyMode)
                setText(R.id.tvBest, String.valueOf(Game.currentFrenzyHighScore));
            else setText(R.id.tvBest, String.valueOf(Game.currentHighScore));

            input.touchDownTime = getSavedTouchDownTime();

            GameLoop.update.getGameData();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveData();

        GiftizSDK.onPauseMainActivity(this);

    }

    @Override
    public void onBackPressed() {
        if (Game.state == GameState.Themes) {
            ColorCircle.popAll = true;
        } else if (Game.state == GameState.InGame && !Game.splitScreenMode) {
            Game.Pause();
        } else if (Game.state == GameState.Paused) {
            Game.Play();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInFlow = false;
        saveAutoSignInOption(true);
        cleanUI();
        CircleCreator.createAccountButton();
        Achievements.checkAchievements();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mSignInFlow = false;
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient
                    , connectionResult,
                    RC_SIGN_IN, getString(R.string.sign_in_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        saveAutoSignInOption(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mSignInFlow = true;
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.sign_in_failed);
            }
        }
    }

    private class Pause implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (Game.state != GameState.Paused && Game.state != GameState.InGame)
                return false;

            boolean canPerformAction = true;

            Point location = new Point(imgPause.getRight(), imgPause.getBottom());

            for (Circle circle : Game.circles) {

                if (circle.intersects(location, false)) {

                    canPerformAction = false;
                    break;

                }

            }

            if (canPerformAction) {

                if (Game.state == GameState.Paused) {

                    Game.Play();

                } else {

                    Game.Pause();

                }

            }

            return false;
        }
    }

    private class Back implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                if (Game.state == GameState.Tutorial) {

                    Game.tutorial.end();

                } else if(Game.state == GameState.Paused) {

                    Game.lives = 0;

                } else if (Game.state == GameState.Themes) {

                    ColorCircle.popAll = true;
                    MyActivity.setVisible(R.id.tvExplain, View.GONE);

                }

            }

            return true;
        }
    }
}
