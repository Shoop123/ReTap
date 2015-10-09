package com.custom.retap.additions;

import android.graphics.Point;

import com.custom.retap.R;
import com.custom.retap.circles.AlertCircle;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.CircleButton;
import com.custom.retap.circles.ExpandingCircleButton;
import com.custom.retap.circles.ImageCircleButton;
import com.custom.retap.circles.StaticCircleButton;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;

public abstract class CircleCreator {

    private static CircleButton btnTutorial;

    private static CircleButton btnPlay;

    private static CircleButton btnFrenzy;

    private static CircleButton btnSplitScreen;

    private static StaticCircleButton btnAccount;

    private static StaticCircleButton btnGiftiz;

    public static ExpandingCircleButton btnThemes;

    private static AlertCircle alertCircle;

    private static final int retapBtnSize = Game.screenSize.y / 4;

    private static final int primaryBtnSize = Game.screenSize.y / 6;

    private static final int secondaryBtnSize = Game.screenSize.y / 8;

    private static final int alertBtnSize = Game.screenSize.y / 3;

    public static void createSplitScreenButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnSplitScreen)) Game.circles.remove(btnSplitScreen);

        int radius = primaryBtnSize;

        Point location = new Point(Game.screenSize.x - Game.screenSize.x / 5, Game.screenSize.y / 2);

        btnSplitScreen = new CircleButton(MyActivity.res.getString(R.string.split_screen), location, radius, Math.round(radius / 3.2f));

        Game.circles.offer(btnSplitScreen);

    }

    public static void createPlayButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnPlay)) Game.circles.remove(btnPlay);

        int radius = retapBtnSize;

        String text = MyActivity.res.getString(R.string.retap);

        if (Game.state == GameState.None)
            text = MyActivity.res.getString(R.string.tap);

        btnPlay = new CircleButton(text, new Point(Game.screenSize.x / 2, Game.screenSize.y / 2), radius, Math.round(radius / Circle.ReTap_TEXT_SIZE_DIVIDER));

        Game.circles.offer(btnPlay);

    }

    public static void createFrenzyButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnFrenzy)) Game.circles.remove(btnFrenzy);

        int radius = primaryBtnSize;

        btnFrenzy = new CircleButton(MyActivity.res.getString(R.string.frenzy), new Point(Game.screenSize.x / 5, Game.screenSize.y / 2), radius, Math.round(radius / Circle.ReTap_TEXT_SIZE_DIVIDER));

        Game.circles.offer(btnFrenzy);

    }

    public static void createTutorialButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnTutorial)) Game.circles.remove(btnTutorial);

        int radius = secondaryBtnSize;

        Point location = new Point(Game.screenSize.x / 4 + radius, Game.screenSize.y - radius);

        btnTutorial = new CircleButton(MyActivity.res.getString(R.string.tutorial), location, radius, Math.round(radius / 2.0f));

        Game.circles.offer(btnTutorial);

    }

    public static void createAccountButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnAccount)) Game.circles.remove(btnAccount);

        int radius = secondaryBtnSize;

        String text = MyActivity.res.getString(R.string.sign_in);

        float textSize = Math.round(radius / Circle.ReTap_TEXT_SIZE_DIVIDER);

        if (MyActivity.activity.mGoogleApiClient.isConnected()) {
            text = MyActivity.res.getString(R.string.sign_out);
            textSize = Math.round(radius / 2.4f);
        }

        Point location = new Point((Game.screenSize.x - (Game.screenSize.x / 4)) - radius, Game.screenSize.y - radius);

        btnAccount = new StaticCircleButton(text, location, radius, textSize);

        Game.circles.offer(btnAccount);

    }

    public static void createGiftizButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnGiftiz)) Game.circles.remove(btnGiftiz);

        int radius = secondaryBtnSize;

        btnGiftiz = new ImageCircleButton(MyActivity.res.getString(R.string.giftiz), new Point(Game.screenSize.x - radius, Game.screenSize.y - radius), radius, R.drawable.giftiz_logo);

        Game.circles.offer(btnGiftiz);

    }

    public static void createThemesButton() {

        if (Game.state != GameState.None && Game.state != GameState.Over) return;

        if (Game.circles.contains(btnThemes)) Game.circles.remove(btnThemes);

        int radius = secondaryBtnSize;

        btnThemes = new ExpandingCircleButton(MyActivity.res.getString(R.string.themes), new Point(radius, Game.screenSize.y - radius), radius, Math.round(radius / 2.2f));

        Game.circles.offer(btnThemes);

    }

    public static void createAlertCircle(String alert) {

        if (Game.circles.contains(alertCircle)) Game.circles.remove(alertCircle);

        int radius = alertBtnSize;

        alertCircle = new AlertCircle(alert, new Point(Game.screenSize.x / 2, Game.screenSize.y / 2), radius, Math.round(radius / 3f), false);

        Game.circles.offer(alertCircle);
    }

}
