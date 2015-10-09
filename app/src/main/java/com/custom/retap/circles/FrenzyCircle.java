package com.custom.retap.circles;

import android.graphics.Point;

import com.custom.retap.game.Game;

import java.util.Random;

public class FrenzyCircle extends Circle {

    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int LEFT = 4;

    public FrenzyCircle() {

        super();

        Random rnd = new Random();
        rnd.setSeed(System.nanoTime());

        startLocation = rnd.nextInt(4) + 1;

        getLocation(rnd);

    }

    private void getLocation(Random rnd) {

        int x = 0;
        int y = 0;

        if (startLocation == TOP) {

            x = rnd.nextInt(Game.screenSize.x - radius * 2) + radius;
            y = -radius;

        } else if (startLocation == RIGHT) {

            x = Game.screenSize.x + radius;
            y = rnd.nextInt(Game.screenSize.y - radius * 2) + radius;

        } else if (startLocation == BOTTOM) {

            x = rnd.nextInt(Game.screenSize.x - radius * 2) + radius;
            y = Game.screenSize.y + radius;

        } else if (startLocation == LEFT) {

            x = -radius;
            y = rnd.nextInt(Game.screenSize.y - radius * 2) + radius;

        }

        location = new Point(x, y);
    }

}
