package com.custom.retap.visuals;

import android.view.View;

import com.custom.retap.R;
import com.custom.retap.additions.Achievements;
import com.custom.retap.additions.CircleCreator;
import com.custom.retap.additions.Mission;
import com.custom.retap.additions.SplitScreenManager;
import com.custom.retap.circles.AlertCircle;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.CircleButton;
import com.custom.retap.circles.FrenzyCircle;
import com.custom.retap.circles.NumberedCircle;
import com.custom.retap.circles.SSCircle;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameLoop;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;

import java.util.Random;

public class Update {

    private long startTime;

    private long startTimeForTouch = 0;

    private final int STARTING_TIME = 3200;

    private final int MIN_TIME = 800;

    private final int MAX_TIME = 1000;

    private float interval = STARTING_TIME, currentMaxTime = STARTING_TIME, currentMinTime = currentMaxTime;

    private final Input input;

    private final Random rnd = new Random();

    private long currentTime;

    private int numberedCircleChance = 0;

    private boolean declaredWinner = false;

    public Update(Input input) {

        this.input = input;

    }

    public void update() {

        checkTimeToGo();

        if (Game.tutorial != null) Game.tutorial.update();

        if (Game.state == GameState.Paused) return;

        for (Circle circle : Game.circles) {

            boolean remove = false;

            if (circle.startLocation == FrenzyCircle.TOP && !circle.isClicked) {

                if (circle.location.y > Game.screenSize.y + circle.radius) remove = true;

            } else if (circle.startLocation == FrenzyCircle.RIGHT && !circle.isClicked) {

                if (circle.location.x + circle.radius < 0) remove = true;

            } else if (circle.startLocation == FrenzyCircle.BOTTOM && !circle.isClicked) {

                if (circle.location.y + circle.radius < 0) remove = true;

            } else if (circle.startLocation == FrenzyCircle.LEFT && !circle.isClicked) {

                if (circle.location.x > Game.screenSize.x + circle.radius) remove = true;

            }

            if (remove) loseLife(circle);

            circle.update();

            if (circle.radius <= 0) popCircleWithScore(circle);
        }
    }

    private void checkTimeToGo() {

        if(CircleButton.timeToGo && Game.circles.size() == 0) {
            CircleButton.timeToGo = false;
            declaredWinner = false;
        }

    }

    private void popCircleWithScore(Circle circle) {

        Game.circles.remove(circle);

        if (!(circle instanceof CircleButton) && !Game.splitScreenMode) {

            Game.score++;

            MyActivity.setText(R.id.tvScore, String.valueOf(Game.score));

            if(Game.frenzyMode) {
                if(Game.score > Game.currentFrenzyHighScore) MyActivity.setText(R.id.tvBest, String.valueOf(Game.score));
            } else {
                if(Game.score > Game.currentHighScore) MyActivity.setText(R.id.tvBest, String.valueOf(Game.score));
            }
        }
    }

    private void loseLife(Circle circle) {

        if(Game.splitScreenMode) {
            if(circle.startLocation == FrenzyCircle.LEFT && SplitScreenManager.player1Lives > 0) {

                SplitScreenManager.player1Lives--;

                updateLives();

            } else if(SplitScreenManager.player2Lives > 0) {

                SplitScreenManager.player2Lives--;

                updateLives();
            }
        } else if (Game.lives > 0) {

            Game.lives--;

            updateLives();

        }

        Game.circles.remove(circle);

    }

    private void updateLives() {

        if(Game.splitScreenMode) {

            switch (SplitScreenManager.player1Lives) {

                case 2:
                    MyActivity.setVisible(R.id.imgLivesP13, View.GONE);
                    break;
                case 1:
                    MyActivity.setVisible(R.id.imgLivesP12, View.GONE);
                    break;
                case 0:
                    MyActivity.setVisible(R.id.imgLivesP11, View.GONE);
                    break;
            }

            switch (SplitScreenManager.player2Lives) {

                case 2:
                    MyActivity.setVisible(R.id.imgLivesP23, View.GONE);
                    break;
                case 1:
                    MyActivity.setVisible(R.id.imgLivesP22, View.GONE);
                    break;
                case 0:
                    MyActivity.setVisible(R.id.imgLivesP21, View.GONE);
                    break;
            }

        } else {

            switch (Game.lives) {

                case 2:
                    MyActivity.setVisible(R.id.life3, View.GONE);
                    break;
                case 1:
                    MyActivity.setVisible(R.id.life2, View.GONE);
                    break;
                case 0:
                    MyActivity.setVisible(R.id.life1, View.GONE);
                    break;
            }
        }
    }

    public void updateTouchDownTime() {

        if(Game.splitScreenMode) {

            //SplitScreenManager.updateTouchDownTime();

            return;

        }

        currentTime = System.currentTimeMillis();

        int elapsedTime = 0;

        if (Game.state != GameState.Paused) elapsedTime = (int) (currentTime - startTimeForTouch);

        if (input.touchDown) {

            input.touchDownTime += elapsedTime;

        } else {

            if (input.touchDownTime > 0) {

                input.touchDownTime -= elapsedTime;

            } else {

                input.touchDownTime = 0;

            }

        }

        startTimeForTouch = currentTime;

    }

    private void make(float time, float time2) {

        if(CircleButton.timeToGo) return;

        if (Game.frenzyMode) makeFrenzy(time);
        else if(Game.splitScreenMode) makeSplitScreen(time, time2);
        else makeNormal(time);

    }

    private void makeFrenzy(float time) {

        float distancePerRefreshY = Game.screenSize.y / GameLoop.TARGET_FPS;
        float distancePerRefreshX = Game.screenSize.x / GameLoop.TARGET_FPS;

        float speedY = distancePerRefreshY / (time / 1000.0F);
        float speedX = distancePerRefreshX / (time / 1000.0F);

        FrenzyCircle circle = new FrenzyCircle();

        if (circle.startLocation == FrenzyCircle.TOP) {

            circle.speedY = speedY;

        } else if (circle.startLocation == FrenzyCircle.RIGHT) {

            circle.speedX = -speedX;

        } else if (circle.startLocation == FrenzyCircle.BOTTOM) {

            circle.speedY = -speedY;

        } else if (circle.startLocation == FrenzyCircle.LEFT) {

            circle.speedX = speedX;

        }

        Game.circles.offer(circle);

    }

    private void makeNormal(float time) {

        float distancePerRefreshY = Game.screenSize.y / GameLoop.TARGET_FPS;

        float speedY = distancePerRefreshY / (time / 1000.0F);

        final int MAX_TOTAL_TIME = MAX_TIME + MIN_TIME;

        final int CIRCLE_POSSIBILITY = 10;

        Circle circle;

        rnd.setSeed(System.nanoTime());

        int chance = rnd.nextInt(CIRCLE_POSSIBILITY);

        if (chance < numberedCircleChance && time >= (MAX_TOTAL_TIME / 2)) {

            circle = new NumberedCircle();

        } else {

            circle = new Circle();

        }

        circle.speedY = speedY;

        Game.circles.offer(circle);

    }

    private void makeSplitScreen(float time, float time2) {

        float distancePerRefreshX = Game.screenSize.x / GameLoop.TARGET_FPS / 2.0f;

        float speedXOne = distancePerRefreshX / (time / 1000.0F);
        float speedXTwo = distancePerRefreshX / (time2 / 1000.0F);

        SSCircle circle = new SSCircle();
        circle.startLocation = FrenzyCircle.LEFT;

        SSCircle circle2 = new SSCircle();
        circle2.startLocation = FrenzyCircle.RIGHT;

        circle.speedX = speedXOne;
        circle2.speedX = speedXTwo;

        Game.circles.offer(circle);
        Game.circles.offer(circle2);

    }

    private void checkSSScore() {

        if(SplitScreenManager.player1Lives <= 0) {

            if(!AlertCircle.isInAlert) {

                if(!declaredWinner) {
                    for(Circle circle : Game.circles)
                        if(!(circle instanceof AlertCircle)) circle.isClicked = true;

                    CircleCreator.createAlertCircle(MyActivity.res.getString(R.string.player_2_winner));

                    Game.checkVisibility();

                    declaredWinner = true;
                } else endGame();

            }

        } else if(SplitScreenManager.player2Lives <=0) {

            if(!AlertCircle.isInAlert) {

                if(!declaredWinner) {
                    for(Circle circle : Game.circles)
                        if(!(circle instanceof AlertCircle)) circle.isClicked = true;

                    CircleCreator.createAlertCircle(MyActivity.res.getString(R.string.player_1_winner));

                    Game.checkVisibility();

                    declaredWinner = true;
                } else endGame();

            }

        }

    }

    private void checkNormalScore() {

        if (Game.score > Game.currentHighScore) {

            Game.currentHighScore = Game.score;

            Achievements.checkAchievements();

            Achievements.postToLeaderboards();

            MyActivity.saveHighScore();

            MyActivity.setText(R.id.tvBest, String.valueOf(Game.currentHighScore));

        }

    }

    private void checkFrenzyScore() {

        if (Game.score > Game.currentFrenzyHighScore) {

            Game.currentFrenzyHighScore = Game.score;

            Achievements.checkAchievements();

            Achievements.postToLeaderboards();

            MyActivity.saveFrenzyHighScore();

            MyActivity.setText(R.id.tvBest, String.valueOf(Game.currentFrenzyHighScore));

        }

    }

    public void checkScore() {

        if(Game.state != GameState.InGame && Game.state != GameState.Paused) return;

        if(Game.splitScreenMode) checkSSScore();
        else if (Game.lives <= 0) {

            if (Game.score >= Mission.MISSION_COMPLETE_SCORE) Mission.completeMission();

            if(Game.frenzyMode) checkFrenzyScore();
            else checkNormalScore();

            endGame();

        }

    }

    private void endGame() {

        Game.showPlayScreen();

        currentMaxTime = STARTING_TIME;

        currentMinTime = STARTING_TIME;

        interval = STARTING_TIME;

        numberedCircleChance = 0;

    }

    public void generateCircle() {

        if (Game.state != GameState.InGame) return;

        currentTime = System.currentTimeMillis();

        long elapsedTime = currentTime - startTime;

        if (elapsedTime >= interval) {

            rnd.setSeed(System.nanoTime());

            float time = (rnd.nextFloat() * currentMaxTime) + currentMinTime;
            float time2 = (rnd.nextFloat() * currentMaxTime) + currentMinTime;

            make(time, time2);

            startTime = currentTime;

        }

    }

    public void fiveSeconds() {

        final int MAX_NUMBERED_CIRCLE_CHANCE = 3;

        if (interval <= (STARTING_TIME / 2) && numberedCircleChance < MAX_NUMBERED_CIRCLE_CHANCE) {

            numberedCircleChance++;

        }

    }

    public void twoSeconds() {

        final int MAX_TIME_DIMINISHING_RATE = 3;

        if ((currentMaxTime - currentMaxTime / MAX_TIME_DIMINISHING_RATE) < MAX_TIME) {

            currentMaxTime = MAX_TIME;

        } else {

            currentMaxTime -= currentMaxTime / MAX_TIME_DIMINISHING_RATE;

        }

        final int MIN_TIME_DIMINISHING_RATE = 4;

        if((currentMinTime - currentMinTime / MIN_TIME_DIMINISHING_RATE) < MIN_TIME) {

            currentMinTime = MIN_TIME;

        } else {

            currentMinTime -= currentMinTime / MIN_TIME_DIMINISHING_RATE;

        }

        final int MIN_INTERVAL = 250;

        final int INTERVAL_DIMINISHING_RATE = 10;

        if ((interval - interval / INTERVAL_DIMINISHING_RATE) < MIN_INTERVAL) {

            interval = MIN_INTERVAL;

        } else {

            interval -= interval / INTERVAL_DIMINISHING_RATE;

        }

    }

    public void saveGameData() {
        MyActivity.saveCurrentGameData(interval, currentMaxTime);
    }

    public void getGameData() {
        interval = MyActivity.getInterval();
        if(interval == 0) interval = STARTING_TIME;

        currentMaxTime = MyActivity.getCurrentMaxTime();
        if(currentMaxTime == 0) currentMaxTime = STARTING_TIME;
    }

}