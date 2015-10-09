package com.custom.retap.game;

import android.graphics.Point;
import android.view.View;

import com.custom.retap.R;
import com.custom.retap.additions.Achievements;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.FrenzyCircle;
import com.custom.retap.circles.NumberedCircle;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

public class Tutorial {

    private final Input input;

    private final int STAGE_0_START = 0;

    public final int STAGE_1_NORMAL_CIRCLE = 1;

    private final int STAGE_2_SCORE = 2;

    public final int STAGE_3_NUMBERED_CIRCLES = 3;

    public final int STAGE_4_TIMER = 4;

    private final int STAGE_5_LIVES = 5;

    private final int STAGE_6_ACHIEVEMENTS = 6;

    public int stage = STAGE_0_START;

    private Circle circle;

    private final FrenzyCircle frenzyCircle;

    private NumberedCircle numberedCircle;

    private boolean circleAdded = false;

    private boolean frenzyCircleAdded = false;

    private boolean numberedCircleAdded = false;

    public boolean moveOn = false;

    private final GameState prevState;

    public Tutorial(Input input) {

        this.input = input;

        circle = new Circle();
        frenzyCircle = new FrenzyCircle();
        numberedCircle = new NumberedCircle();

        prevState = Game.state;

        Game.state = GameState.Tutorial;

        Game.score = 0;

        MyActivity.setText(R.id.tvScore, String.valueOf(Game.score));
        MyActivity.setTextColor(Draw.color);

        MyActivity.setImageColor(Draw.color);
    }

    public void update() {

        if (Game.state != GameState.Tutorial) return;

        if (stage1()) {

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.firstText));

        } else if (stage2()) {

            stage = STAGE_2_SCORE;

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.secondText));

        } else if (stage3()) {

            stage = STAGE_3_NUMBERED_CIRCLES;

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.thirdText));

        } else if (stage4()) {

            stage = STAGE_4_TIMER;

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.fourthText));

        } else if (stage5()) {

            stage = STAGE_5_LIVES;

            MyActivity.setVisible(R.id.life1, View.VISIBLE);
            MyActivity.setVisible(R.id.life2, View.VISIBLE);
            MyActivity.setVisible(R.id.life3, View.VISIBLE);

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.fifthText));

        } else if (stage6()) {

            stage = STAGE_6_ACHIEVEMENTS;

            MyActivity.setText(R.id.tvExplain, MyActivity.res.getString(R.string.sixthText));

        } else if (stage == STAGE_6_ACHIEVEMENTS && moveOn) {

            end();

        }

        moveOn = false;

    }

    public void showTutorialView() {

        MyActivity.setVisible(R.id.tvBest, View.GONE);
        MyActivity.setVisible(R.id.tvScore, View.VISIBLE);
        MyActivity.setVisible(R.id.life1, View.GONE);
        MyActivity.setVisible(R.id.life3, View.GONE);
        MyActivity.setVisible(R.id.life2, View.GONE);
        MyActivity.setVisible(R.id.imgPause, View.GONE);
        MyActivity.setVisible(R.id.tvExplain, View.VISIBLE);
        MyActivity.setVisible(R.id.imgBack, View.VISIBLE);

    }

    public void end() {
        stage = STAGE_0_START;

        circleAdded = false;
        numberedCircleAdded = false;

        circle = new Circle();
        numberedCircle = new NumberedCircle();

        Achievements.completeTutorialAchievement();

        Game.reset(prevState);
    }

    private void setupFrenzyCircle() {

        if (frenzyCircle.startLocation == FrenzyCircle.TOP) {

            frenzyCircle.startLocation = FrenzyCircle.BOTTOM;

            frenzyCircle.location = new Point(frenzyCircle.location.x, Game.screenSize.y + frenzyCircle.radius);

            frenzyCircle.speedY = -(Game.screenSize.y / 2.0F / GameLoop.TARGET_FPS);

        } else if (frenzyCircle.startLocation == FrenzyCircle.BOTTOM)
            frenzyCircle.speedY = -(Game.screenSize.y / 2.0F / GameLoop.TARGET_FPS);
        else if (frenzyCircle.startLocation == FrenzyCircle.RIGHT)
            frenzyCircle.speedX = -(Game.screenSize.x / 2.0F / GameLoop.TARGET_FPS);
        else if (frenzyCircle.startLocation == FrenzyCircle.LEFT)
            frenzyCircle.speedX = Game.screenSize.x / 2.0F / GameLoop.TARGET_FPS;

    }

    private boolean stage1() {

        if (stage != STAGE_0_START || (circleAdded && frenzyCircleAdded)) return false;

        if (!circleAdded) {

            Game.circles.clear();

            circleAdded = true;

            Game.circles.offer(circle);

            circle.speedY = Game.screenSize.y / 2.0F / GameLoop.TARGET_FPS;

        } else if (!frenzyCircleAdded && Game.score > 0) {

            Game.circles.clear();

            frenzyCircleAdded = true;

            stage = STAGE_1_NORMAL_CIRCLE;

            setupFrenzyCircle();

            Game.circles.offer(frenzyCircle);

        }

        return true;

    }

    private boolean stage2() {

        return stage == STAGE_1_NORMAL_CIRCLE && Game.score > 1 && moveOn;

    }

    private boolean stage3() {

        if (stage != STAGE_2_SCORE || !moveOn) return false;

        if (!numberedCircleAdded) {

            numberedCircleAdded = true;

            Game.circles.offer(numberedCircle);

        }

        numberedCircle.speedY = (Game.screenSize.y / 2.0F / GameLoop.TARGET_FPS) / 2.0F;

        return true;
    }

    private boolean stage4() {

        if (stage != STAGE_3_NUMBERED_CIRCLES || !moveOn) return false;

        input.touchDownTime = input.MAX_TOUCH_DOWN_TIME;

        return true;

    }

    private boolean stage5() {

        return stage == STAGE_4_TIMER && moveOn;

    }

    private boolean stage6() {

        return stage == STAGE_5_LIVES && moveOn;

    }

    public String getCurrent() {

        if (stage == STAGE_1_NORMAL_CIRCLE) return MyActivity.res.getString(R.string.firstText);
        else if (stage == STAGE_2_SCORE) return MyActivity.res.getString(R.string.secondText);
        else if (stage == STAGE_3_NUMBERED_CIRCLES) return MyActivity.res.getString(R.string.thirdText);
        else if (stage == STAGE_4_TIMER) return MyActivity.res.getString(R.string.fourthText);
        else if (stage == STAGE_5_LIVES) return MyActivity.res.getString(R.string.fifthText);

        return " ";

    }

}
