package com.custom.retap.circles;

import android.graphics.Color;
import android.view.View;

import com.custom.retap.R;
import com.custom.retap.additions.Achievements;
import com.custom.retap.additions.Mission;
import com.custom.retap.additions.ThemeManager;
import com.custom.retap.game.Game;
import com.custom.retap.game.MyActivity;

public class Handler {

    public void buttonTouch(String text) {

        if (text.equals(MyActivity.res.getString(R.string.retap)) || text.equals(MyActivity.res.getString(R.string.tap))) {

            Game.startGame();

            MyActivity.setText(R.id.tvBest, String.valueOf(Game.currentHighScore));

        } else if (text.equals(MyActivity.res.getString(R.string.frenzy))) {

            Game.frenzyMode = true;

            Game.startGame();

            MyActivity.setText(R.id.tvBest, String.valueOf(Game.currentFrenzyHighScore));

        } else if (text.equals((MyActivity.res.getString(R.string.tutorial)))) {

            Game.startTutorial();

        } else if (text.equals((MyActivity.res.getString(R.string.split_screen)))) {

            Game.splitScreenMode = true;

            Game.startSplitScreen();

        } else if (text.equals(MyActivity.res.getString(R.string.sign_in))) {

            Achievements.connectPlayServices();

        } else if (text.equals(MyActivity.res.getString(R.string.sign_out))) {

            Achievements.disconnectPlayServices();

            MyActivity.saveAutoSignInOption(false);
        } else if (text.equals(MyActivity.res.getString(R.string.giftiz))) {
            Mission.connectGiftiz();
        } else if (text.equals(MyActivity.res.getString(R.string.themes))) {

            MyActivity.setVisible(R.id.tvBest, View.GONE);
            MyActivity.setVisible(R.id.tvScore, View.GONE);
            MyActivity.setVisible(R.id.tvExplain, View.VISIBLE);

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.pick_a_color));
            MyActivity.setTextColor(Color.WHITE);

            ThemeManager.startOffering();

        } else if (text.equals(MyActivity.res.getString(R.string.color))) {
            ColorCircle.popAll = true;
            MyActivity.setVisible(R.id.tvExplain, View.GONE);
        }
    }

    public void checkChanged(String text) {



    }

}
