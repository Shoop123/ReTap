package com.custom.retap.additions;

import android.graphics.Point;
import android.os.Build;

import com.custom.retap.R;
import com.custom.retap.circles.Circle;
import com.custom.retap.circles.ColorCircle;
import com.custom.retap.circles.RainbowCircle;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;
import com.custom.retap.visuals.Draw;

public abstract class ThemeManager {

    private static final int AMOUNT_OF_COLORS = 7;

    public static final int[] colors = new int[AMOUNT_OF_COLORS];

    private static final boolean[] colorsAdded = new boolean[AMOUNT_OF_COLORS];

    public static boolean[] unlocked = new boolean[AMOUNT_OF_COLORS + 1];

    private static int amountOfColorCircles = 0;

    private static final int RADIUS = Game.screenSize.y / 6;

    private static final int AMOUNT_PER_ROW = 4;

    private static final int SPACE = Game.screenSize.x / 16;

    private static final int SPACE_FOR_OLD_SDK = Game.screenSize.x / 18;

    private static boolean rainbowAdded = false;

    public static void retrieve() {

        colors[0] = MyActivity.res.getColor(R.color.black);
        colors[1] = MyActivity.res.getColor(R.color.blue);
        colors[2] = MyActivity.res.getColor(R.color.red);
        colors[3] = MyActivity.res.getColor(R.color.green);
        colors[4] = MyActivity.res.getColor(R.color.orange);
        colors[5] = MyActivity.res.getColor(R.color.purple);
        colors[6] = MyActivity.res.getColor(R.color.pink);

        unlocked = MyActivity.getUnlockedThemes();

    }

    public static void update() {

        if (ColorCircle.addNext)
            for (int i = 0; i < colorsAdded.length; i++) {

                if (!colorsAdded[i]) {

                    Game.circles.offer(new ColorCircle(getLocation(), RADIUS, colors[i], unlocked[i]));

                    colorsAdded[i] = true;

                    amountOfColorCircles++;

                    ColorCircle.addNext = false;

                    break;
                }

                if (i == colorsAdded.length - 1 && !rainbowAdded) {

                    Game.circles.offer(new RainbowCircle(getLocation(), RADIUS, unlocked[AMOUNT_OF_COLORS]));
                    rainbowAdded = true;

                }
            }

        checkColors();
    }

    private static void checkColors() {

        if (Game.state == GameState.Themes) {

            for (Circle circle : Game.circles) {

                if (circle instanceof ColorCircle)
                    return;

            }

            CircleCreator.btnThemes.goBack = true;
            ColorCircle.popAll = false;

        }
    }

    public static void changeTheme(int color) {

        Draw.color = color;
        MyActivity.setTextColor(color);
        MyActivity.saveTheme();

    }

    public static void startOffering() {

        ColorCircle colorCircle = new ColorCircle(getLocation(), RADIUS, colors[0], unlocked[0]);
        colorCircle.unlocked = true;

        Game.circles.offer(colorCircle);

        colorsAdded[0] = true;

        amountOfColorCircles++;

    }

    private static Point getLocation() {

        int space = SPACE;

        if (Build.VERSION.SDK_INT < MyActivity.IMMERSIVE_MODE_MIN_SDK) space = SPACE_FOR_OLD_SDK;

        int initX = RADIUS + (space / 2);

        int yDisplacement = 0;

        int indexFromLeft = amountOfColorCircles;

        if (amountOfColorCircles >= AMOUNT_PER_ROW) {
            indexFromLeft -= AMOUNT_PER_ROW;
            yDisplacement = (RADIUS * 2) + SPACE;
        }

        int x = initX + (indexFromLeft * (RADIUS * 2)) + (space * indexFromLeft);

        int y = (RADIUS + SPACE) + yDisplacement;

        int xInt = Math.round(x);
        int yInt = Math.round(y);

        return new Point(xInt, yInt);

    }

    public static void resetAmountOfColorCircles() {
        ThemeManager.amountOfColorCircles = 0;
        rainbowAdded = false;
        for (int i = 0; i < colorsAdded.length; i++) colorsAdded[i] = false;
    }
}
