package com.custom.retap.additions;

import com.custom.retap.R;
import com.custom.retap.game.Game;
import com.custom.retap.game.MyActivity;
import com.google.android.gms.games.Games;

public abstract class Achievements {
    private static final int ACHIEVEMENT_RETAPPED_SCORE = 50;
    private static final int ACHIEVEMENT_GOTTA_TAP_EM_ALL_SCORE = 100;
    private static final int ACHIEVEMENT_MASTOR_HAXOR_360_NO_SCOPOR_SCORE = 200;

    private static final int ACHIEVEMENT_PEW_PEW_GOTCHA_SCORE = 50;
    private static final int ACHIEVEMENT_IZ_NICE_SCORE = 100;
    private static final int ACHIEVEMENT_I_NEED_A_LIFE_SCORE = 200;

    private static final int ACHIEVEMENT_TREEFITTY = 350;

    public static void completeTutorialAchievement() {

        if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
            Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_pop_your_cherry));

    }

    public static void checkAchievements() {

        if (Game.currentFrenzyHighScore >= ACHIEVEMENT_PEW_PEW_GOTCHA_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_pew_pew_gotcha));
            ThemeManager.unlocked[4] = true;
        }
        if (Game.currentFrenzyHighScore >= ACHIEVEMENT_IZ_NICE_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_iz_nice));
            ThemeManager.unlocked[5] = true;
        }
        if (Game.currentFrenzyHighScore >= ACHIEVEMENT_I_NEED_A_LIFE_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_i_need_a_life_));
            ThemeManager.unlocked[6] = true;
        }
        if (Game.currentHighScore >= ACHIEVEMENT_RETAPPED_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_retapped));
            ThemeManager.unlocked[1] = true;
        }
        if (Game.currentHighScore >= ACHIEVEMENT_GOTTA_TAP_EM_ALL_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_gotta_tap_em_all));
            ThemeManager.unlocked[2] = true;
        }
        if (Game.currentHighScore >= ACHIEVEMENT_MASTOR_HAXOR_360_NO_SCOPOR_SCORE) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_mastor_haxor_360_no_scopor));
            ThemeManager.unlocked[3] = true;
        }
        if (Game.currentHighScore >= ACHIEVEMENT_TREEFITTY || Game.currentFrenzyHighScore >= ACHIEVEMENT_TREEFITTY) {
            if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected())
                Games.Achievements.unlock(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.achievement_treefitty));
            ThemeManager.unlocked[7] = true;
        }
    }

    public static void postToLeaderboards() {

        if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected()) {

            if (Game.frenzyMode)
                Games.Leaderboards.submitScore(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.leaderboard_frenzy_mode), Game.currentFrenzyHighScore);
            else
                Games.Leaderboards.submitScore(MyActivity.activity.mGoogleApiClient, MyActivity.res.getString(R.string.leaderboard_normal_mode), Game.currentHighScore);
        }
    }

    public static void disconnectPlayServices() {
        if (MyActivity.activity.mGoogleApiClient != null && MyActivity.activity.mGoogleApiClient.isConnected()) {
            Games.signOut(MyActivity.activity.mGoogleApiClient);
            MyActivity.activity.mGoogleApiClient.disconnect();
            CircleCreator.createAccountButton();
        }
    }

    public static void connectPlayServices() {

        if (MyActivity.hasConnection()) {

            if (!MyActivity.mSignInFlow) {

                MyActivity.mSignInFlow = true;
                MyActivity.mSignInClicked = true;
                MyActivity.activity.mGoogleApiClient.connect();

            }

        } else {
            CircleCreator.createAlertCircle(MyActivity.res.getString(R.string.no_internet));
        }
    }
}
