package com.custom.retap.circles;

import android.graphics.Point;

import com.custom.retap.additions.ThemeManager;
import com.custom.retap.visuals.Draw;

import java.util.Random;

public class RainbowCircle extends ColorCircle {

    private long timePassed = 0;

    private long previousTime = 0;

    public RainbowCircle(Point location, int preferredRadius, boolean unlocked) {
        super(location, preferredRadius, ThemeManager.colors[new Random().nextInt(ThemeManager.colors.length)], unlocked);
    }

    @Override
    public void update() {
        super.update();

        if(!Draw.rainbow) {
            final int CHANGE_TIME = 500;

            long currentTime = System.currentTimeMillis();

            timePassed += currentTime - previousTime;

            if (timePassed >= CHANGE_TIME) {
                color = Draw.changeColor(color);
                timePassed = 0;
            }

            previousTime = currentTime;
        } else color = Draw.color;
    }

    @Override
    protected void handleTouch() {
        Draw.rainbow = true;
        super.handleTouch();
    }
}
