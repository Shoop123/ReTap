package com.custom.retap.visuals;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.custom.retap.R;
import com.custom.retap.additions.SplitScreenManager;
import com.custom.retap.circles.AlertCircle;
import com.custom.retap.circles.Circle;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;

public class Input implements View.OnTouchListener {

    public static final int MAX_RENDERED_TAPS = 3;

    private final Point[] taps = new Point[MAX_RENDERED_TAPS];

    public boolean touchDown = false;

    public int touchDownTime = 0;

    public final int MAX_TOUCH_DOWN_TIME = 1300;

    public final int MAX_TOUCH_DOWN_TIME_FRENZY = 1100;

    public static boolean isDown = false;

    public Input() {

        for (int i = 0; i < taps.length; i++) taps[i] = null;

    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent motionEvent) {

        Point tapLocation;

        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN)
            isDown = true;
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP)
            isDown = false;

        if (Game.state != GameState.InGame && Game.state != GameState.Paused) {

            tapLocation = new Point((int) motionEvent.getX(), (int) motionEvent.getY());

            if (taps.length < MAX_RENDERED_TAPS - 1)
                taps[taps.length + 1] = tapLocation;
            else taps[0] = tapLocation;

            if (Game.state == GameState.Tutorial) {
                touchDown = motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE;
            } else {
                touchDown = false;
                touchDownTime = 0;
            }

        } else if (Game.state != GameState.Paused) {

            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE || motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {

                int currentMaxTouchDownTime = MAX_TOUCH_DOWN_TIME;
                if (Game.frenzyMode) currentMaxTouchDownTime = MAX_TOUCH_DOWN_TIME_FRENZY;

                if (Game.splitScreenMode && motionEvent.getActionMasked() != MotionEvent.ACTION_MOVE) SplitScreenManager.recordInput(motionEvent);

                touchDown = true;

                if (touchDownTime < currentMaxTouchDownTime) {

                    int index = motionEvent.getActionIndex();

                    int id = motionEvent.getPointerId(index);

                    tapLocation = new Point((int) motionEvent.getX(motionEvent.findPointerIndex(id)), (int) motionEvent.getY(motionEvent.findPointerIndex(id)));

                    for (int j = 0; j < taps.length; j++) {

                        if (j == taps.length - 1 && taps[j] != null) taps[0] = tapLocation;
                        else if (taps[j] == null) taps[j] = tapLocation;

                    }

                }

            } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP || motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {

                if (Game.splitScreenMode) {

                    SplitScreenManager.actionUp(motionEvent);

                } else {
                    touchDown = false;
                }

            }

        }

        return true;
    }

    private void assess() {

        if (Game.state != GameState.Tutorial) return;

        if (!isDown && !Game.tutorial.moveOn) {

            if (Game.tutorial.stage != Game.tutorial.STAGE_3_NUMBERED_CIRCLES && Game.tutorial.stage != Game.tutorial.STAGE_1_NORMAL_CIRCLE) {
                Game.tutorial.moveOn = true;
                MyActivity.setText(R.id.tvExplain, "");
            }

        }
    }

    private void checkAlert() {

        for(int i = 0; i < taps.length; i++) {
            for (Circle circle : Game.circles)
                if (circle instanceof AlertCircle && taps[i] != null) circle.processInput(taps[i]);

            taps[i] = null;
        }
    }

    public void processInput() {

        if(AlertCircle.isInAlert) {

            checkAlert();

            return;
        }

        if(Game.splitScreenMode) SplitScreenManager.processSSInput();
        else {

            for (int i = 0; i < taps.length; i++) {

                if (taps[i] == null) continue;

                assess();

                for (Circle circle : Game.circles) {

                    circle.processInput(taps[i]);

                }

                taps[i] = null;

            }

        }
    }
}
