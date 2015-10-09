package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

import java.util.Random;

public class NumberedCircle extends Circle {

    private int lives;

    private Point textSize;

    public NumberedCircle() {
        super();

        init();

        final int MAX_START_LIVES = 9; // + 2 - 1 so this is really 10

        final int MIN_START_LIVES = 2;

        Random rnd = new Random();

        lives = rnd.nextInt(MAX_START_LIVES) + MIN_START_LIVES;

    }

    private void init() {

        paint.setTextSize(radius);

        getTextSize();

    }

    public void render(Canvas canvas) {

        super.render(canvas);

        renderLives(canvas);

    }

    private void renderLives(Canvas canvas) {

        paint.setColor(Color.WHITE);

        canvas.drawText(String.valueOf(lives), location.x - (textSize.x / 2), location.y + (textSize.y / 2), paint);

        paint.setColor(Draw.color);

    }

    private void getTextSize() {

        String stringLives = String.valueOf(lives);

        Rect textBounds = new Rect();

        paint.getTextBounds(stringLives, 0, stringLives.length() - 1, textBounds);
        float width = paint.measureText(stringLives);

        textSize = new Point(Math.round(width), textBounds.height());

    }

    @Override
    public void update() {

        getTextSize();

        if (isClicked) {

            if (lives > 0)
                lives--;

            isClicked = false;

        }

        if (lives <= 0) {

            radius -= diminishingRate;

            if (radius <= 0) {

                radius = 0;

                if (Game.state == GameState.Tutorial && !Input.isDown) {

                    Game.tutorial.moveOn = true;

                } else if (Input.isDown) radius = 1;

            }

        }

        move();
    }
}
