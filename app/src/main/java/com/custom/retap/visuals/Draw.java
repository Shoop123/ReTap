package com.custom.retap.visuals;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.custom.retap.additions.SplitScreenManager;
import com.custom.retap.additions.ThemeManager;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.NumberedCircle;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;

public class Draw {

    private final Paint paint;

    private final Input input;

    public static final int LINE_WIDTH = Game.screenSize.y / 72;

    public static boolean rainbow = false;

    public static int color = MyActivity.getColorTheme();

    public static final int COLOR_CHANGE_TIME = 500;

    public Draw(Input input) {

        paint = new Paint();
        paint.setColor(color);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        MyActivity.setTextColor(color);

        this.input = input;

    }

    public void render(Canvas canvas) {

        for (Circle circle : Game.circles) {

            if (!(circle instanceof NumberedCircle))
                circle.render(canvas);

        }

        for (Circle circle : Game.circles) {

            if (circle instanceof NumberedCircle)
                circle.render(canvas);

        }

        SplitScreenManager.renderCenterForSplitScreen(canvas, paint);

    }

    public void renderTouchDownTime(Canvas canvas) {

        int currentMaxTouchDownTime = input.MAX_TOUCH_DOWN_TIME;

        if (Game.frenzyMode) currentMaxTouchDownTime = input.MAX_TOUCH_DOWN_TIME_FRENZY;

        if (Game.state != GameState.Paused && Game.state != GameState.InGame && Game.state != GameState.Tutorial)
            return;
        if (Game.state == GameState.Tutorial && Game.tutorial.stage < Game.tutorial.STAGE_4_TIMER)
            return;

        if(Game.splitScreenMode) {
            SplitScreenManager.renderProgressCircles(canvas, paint);
            return;
        }

        if (input.touchDownTime >= currentMaxTouchDownTime) {
            return;
        }

        final float FULL_PERCENTAGE = 100.0F;

        final float HALF_PERCENTAGE = 50.0F;

        final float TOUCH_DOWN_CIRCLE_RADIUS_DIVIDER = 10.8F;

        paint.setStrokeWidth(LINE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);

        float radius = Game.screenSize.y / TOUCH_DOWN_CIRCLE_RADIUS_DIVIDER;

        float realDiameter = (radius + LINE_WIDTH) * 2;

        canvas.drawCircle(radius + (LINE_WIDTH / 2), Game.screenSize.y - radius - (LINE_WIDTH / 2), radius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);

        float xPos = realDiameter / 2;
        float yPos = Game.screenSize.y - realDiameter;

        float height = yPos;

        float touchDownTime = input.touchDownTime;

        float percent = (touchDownTime / currentMaxTouchDownTime) * FULL_PERCENTAGE;

        if (percent > FULL_PERCENTAGE) {

            percent = FULL_PERCENTAGE;

        }

        float increaseInHeight = (realDiameter / HALF_PERCENTAGE) * percent;

        height += increaseInHeight;

        Rect right = new Rect();
        right.set((int) xPos, (int) yPos, (int) xPos * 2, (int) height);

        height = yPos = Game.screenSize.y;

        if (percent >= HALF_PERCENTAGE) {

            height += realDiameter - increaseInHeight;

        }

        Rect left = new Rect();
        left.set(0, (int) height, (int) xPos, (int) yPos);

        canvas.drawRect(right, paint);
        canvas.drawRect(left, paint);

        paint.setColor(color);

    }

    public static int changeColor(int currentColor) {

        for (int i = 0; i < ThemeManager.colors.length; i++) {

            if (ThemeManager.colors[i] == currentColor) {

                if (i < ThemeManager.colors.length - 1) {

                    return ThemeManager.colors[i + 1];

                }
                else return ThemeManager.colors[0];

            }

        }

        return currentColor;

    }

}
