package com.custom.retap.game;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.custom.retap.R;
import com.custom.retap.additions.CircleCreator;
import com.custom.retap.additions.SplitScreenManager;
import com.custom.retap.additions.ThemeManager;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.CircleButton;
import com.custom.retap.circles.ColorCircle;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Game implements SurfaceHolder.Callback {

    public static Point screenSize;

    private Thread  gameThread;

    private GameLoop gameLoop;

    public static final ConcurrentLinkedQueue<Circle> circles = new ConcurrentLinkedQueue<>();

    private SurfaceHolder surfaceHolder;

    public static final int STARTING_LIVES = 3;

    public static int score = 0;

    public static int lives = STARTING_LIVES;

    public static GameState state = GameState.None;

    public static int currentHighScore;

    public static int currentFrenzyHighScore;

    private static Input input;

    public static Tutorial tutorial;

    public static boolean frenzyMode = false;

    public static boolean splitScreenMode = false;

    public Game(Context context, Input input) {

        Game.input = input;

        currentHighScore = MyActivity.getHighScore();

        currentFrenzyHighScore = MyActivity.getFrenzyHighScore();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        screenSize = getDisplaySize(display);

        checkVisibility();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        this.surfaceHolder = surfaceHolder;

        start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        stop();

    }

    public static void checkVisibility() {

        if (Game.state == GameState.None || Game.state == GameState.Alert) {

            MyActivity.setVisible(R.id.life1, View.GONE);
            MyActivity.setVisible(R.id.life2, View.GONE);
            MyActivity.setVisible(R.id.life3, View.GONE);
            MyActivity.setVisible(R.id.tvScore, View.GONE);
            MyActivity.setVisible(R.id.tvExplain, View.GONE);
            MyActivity.setVisible(R.id.imgPause, View.GONE);
            MyActivity.setVisible(R.id.imgBack, View.GONE);
            MyActivity.setVisible(R.id.tvBest, View.GONE);

        } else if (Game.state == GameState.Over) {

            MyActivity.setVisible(R.id.tvScore, View.VISIBLE);
            MyActivity.setVisible(R.id.life1, View.GONE);
            MyActivity.setVisible(R.id.life2, View.GONE);
            MyActivity.setVisible(R.id.life3, View.GONE);
            MyActivity.setText(R.id.tvExplain, "");
            MyActivity.setVisible(R.id.tvExplain, View.GONE);
            MyActivity.setVisible(R.id.imgPause, View.GONE);
            MyActivity.setImageSource(R.id.imgPause, R.drawable.pause);
            MyActivity.setVisible(R.id.imgBack, View.GONE);
            MyActivity.setVisible(R.id.tvBest, View.VISIBLE);

        } else if (Game.state == GameState.Tutorial) {

            if(tutorial != null) tutorial.showTutorialView();

            MyActivity.setText(R.id.tvExplain, tutorial.getCurrent());

        }

        MyActivity.setVisible(R.id.imgLivesP11, View.GONE);
        MyActivity.setVisible(R.id.imgLivesP12, View.GONE);
        MyActivity.setVisible(R.id.imgLivesP13, View.GONE);
        MyActivity.setVisible(R.id.imgLivesP21, View.GONE);
        MyActivity.setVisible(R.id.imgLivesP22, View.GONE);
        MyActivity.setVisible(R.id.imgLivesP23, View.GONE);
        MyActivity.setVisible(R.id.tvIndicatorP1, View.GONE);
        MyActivity.setVisible(R.id.tvIndicatorP2, View.GONE);
    }

    private void start() {

        gameLoop = new GameLoop(surfaceHolder, input);
        gameThread = new Thread(gameLoop);
        gameThread.start();

    }

    private void stop() {

        if (gameLoop != null) {

            gameLoop.stop();

        }

        try {

            if (gameThread != null) {

                gameThread.join();

            }

        } catch (InterruptedException ie) {

            ie.printStackTrace();

        }

    }

    public static void startTutorial() {

        Game.tutorial = new Tutorial(input);

        tutorial.showTutorialView();

        tutorial.moveOn = true;
    }

    public static void startSplitScreen() {

        splitScreenMode = true;

        MyActivity.setImageColor(Draw.color);

        Game.tutorial = null;

        state = GameState.InGame;

        MyActivity.setVisible(R.id.life1, View.GONE);
        MyActivity.setVisible(R.id.life2, View.GONE);
        MyActivity.setVisible(R.id.life3, View.GONE);
        MyActivity.setVisible(R.id.tvScore, View.GONE);
        MyActivity.setVisible(R.id.tvExplain, View.GONE);
        MyActivity.setVisible(R.id.imgPause, View.GONE);
        MyActivity.setVisible(R.id.imgBack, View.GONE);
        MyActivity.setVisible(R.id.tvBest, View.GONE);

        MyActivity.setVisible(R.id.tvIndicatorP1, View.VISIBLE);
        MyActivity.setVisible(R.id.tvIndicatorP2, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP11, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP12, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP13, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP21, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP22, View.VISIBLE);
        MyActivity.setVisible(R.id.imgLivesP23, View.VISIBLE);

        SplitScreenManager.player1Lives = SplitScreenManager.player2Lives = STARTING_LIVES;

    }

    public static void startGame() {

        score = 0;

        MyActivity.setText(R.id.tvScore, String.valueOf(score));
        MyActivity.setTextColor(Draw.color);

        MyActivity.setImageColor(Draw.color);

        Game.tutorial = null;

        state = GameState.InGame;

        MyActivity.setVisible(R.id.tvScore, View.VISIBLE);
        MyActivity.setVisible(R.id.life1, View.VISIBLE);
        MyActivity.setVisible(R.id.life2, View.VISIBLE);
        MyActivity.setVisible(R.id.life3, View.VISIBLE);
        MyActivity.setVisible(R.id.imgPause, View.VISIBLE);
        MyActivity.setVisible(R.id.tvExplain, View.GONE);
        MyActivity.setVisible(R.id.tvBest, View.VISIBLE);
        MyActivity.setVisible(R.id.imgBack, View.GONE);

        input.touchDownTime = input.MAX_TOUCH_DOWN_TIME;

        lives = STARTING_LIVES;

    }

    private static void stopGame() {

        circles.clear();

        CircleButton.timeToGo = false;

        state = GameState.Over;

        frenzyMode = false;

        splitScreenMode = false;

        checkVisibility();

    }

    public static void reset(GameState gameState) {

        Game.circles.clear();

        CircleButton.timeToGo = false;

        CircleCreator.btnThemes.goBack = false;

        Game.state = gameState;

        ThemeManager.resetAmountOfColorCircles();

        ColorCircle.addNext = false;

        checkVisibility();

        showPlayScreen();

    }

    public static void showPlayScreen() {

        if (state != GameState.None && state != GameState.Alert) stopGame();

        CircleCreator.createPlayButton();

        CircleCreator.createFrenzyButton();

        CircleCreator.createTutorialButton();

        CircleCreator.createAccountButton();

        CircleCreator.createGiftizButton();

        CircleCreator.createSplitScreenButton();

        //Keep last
        CircleCreator.createThemesButton();

    }

    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();

        try {

            if (Build.VERSION.SDK_INT >= MyActivity.IMMERSIVE_MODE_MIN_SDK)
                display.getRealSize(point);
            else display.getSize(point);

        } catch (java.lang.NoSuchMethodError ignore) {

            point.x = display.getWidth();
            point.y = display.getHeight();

        }
        return point;
    }

    public static void Pause() {

        Game.state = GameState.Paused;
        MyActivity.setImageColor(Draw.color);
        MyActivity.setVisible(R.id.imgBack, 0);

    }

    public static void Play() {

        Game.state = GameState.InGame;
        MyActivity.setImageColor(Draw.color);
        MyActivity.setVisible(R.id.imgBack, 100);

    }

}