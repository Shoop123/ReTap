package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Point;

import com.custom.retap.R;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;
import com.custom.retap.visuals.Draw;

public class AlertCircle extends CircleButton {

    private final GameState prevState;

    private int prevColor = 0;

    private boolean priority = false;

    public static boolean isInAlert = false;

    public static boolean changeColor = false;

    public AlertCircle(String text, Point location, int preferredRadius, float textSize, boolean priority) {
        super(text, location, preferredRadius, textSize);

        this.priority = priority;

        int inNotificationColor = MyActivity.res.getColor(R.color.gray);

        prevState = Game.state;
        Game.state = GameState.Alert;

        if(priority) {
            timeToGo = true;
            Game.checkVisibility();
        } else {
            prevColor = Draw.color;
            Draw.color = inNotificationColor;
        }

        isInAlert = true;
    }

    @Override
    public void render(Canvas canvas) {
        if(priority && !Draw.rainbow) paint.setColor(Draw.color);
        else if (!Draw.rainbow) paint.setColor(prevColor);
        else if(Draw.rainbow) {
            int currentColor = prevColor;
            if(changeColor) currentColor = Draw.changeColor(prevColor);
            changeColor = false;
            paint.setColor(currentColor);
            prevColor = currentColor;
        }

        canvas.drawCircle(location.x, location.y, radius, paint);

        if(priority && Game.circles.size() == 1) drawText(canvas);
        else if (!priority && radius >= preferredRadius) drawText(canvas);
    }

    @Override
    public void update() {
        super.update();

        if(priority) {
            if (timeToGo && Game.circles.size() > 1) radius = 1;
            else if (timeToGo && Game.circles.size() == 1) {
                radius = 1;
                creating = true;
                timeToGo = false;
            }

            if(isClicked && !timeToGo && radius <= 0) {
                Game.reset(prevState);
                isInAlert = false;
            }
        } else if (!priority && isClicked && radius <= 0) {

            Draw.color = prevColor;
            isInAlert = false;
            Game.state = prevState;

        }
    }
}
