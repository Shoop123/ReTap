package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

import java.util.Random;

public class Circle {

    //Stores the speed of the circle
    public float speedX = 0, speedY = 0;

    //Stores the location of the circle
    public Point location;

    //Stores the radius of the circle
    public int radius;

    //Stores whether or not the user has already tapped this circle
    public boolean isClicked = false;

    //How fast each circle disappears after getting tapped
    float diminishingRate;

    Paint paint;

    public static final float ReTap_TEXT_SIZE_DIVIDER = 1.8f;

    public int startLocation;

    private boolean stopForTutorial = false;

    //Constructor
    public Circle() {
        startLocation = FrenzyCircle.TOP;

        //Generates random numbers
        Random rnd = new Random();

        init();

        initPaint();

        int xLocation = rnd.nextInt(Game.screenSize.x - radius * 2) + radius;
        int yLocation = -radius;

        location = new Point(xLocation, yLocation);
    }

    private void init() {

        //Calculate the circles radius
        radius = Game.screenSize.y / 6;

        diminishingRate = (radius / 6);

    }

    void initPaint() {

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

    }

    public void render(Canvas canvas) {

        paint.setColor(Draw.color);

        canvas.drawCircle(location.x, location.y, radius, paint);

    }

    public void processInput(Point location) {

        if (intersects(location, true)) {

            if (Game.state == GameState.Tutorial) {
                if (stopForTutorial) isClicked = true;

            } else {

                isClicked = true;

            }

        }

    }

    public boolean intersects(Point point, boolean useHitBox) {

        int hitBoxEnlarger = 10;

        if (!useHitBox) hitBoxEnlarger = 0;

        //Checks if the player tapped within the bounds of the x coordinate of the button
        if (point.x >= ((location.x - radius) - hitBoxEnlarger) && point.x <= (location.x + radius + hitBoxEnlarger)) {

            //Checks if the player tapped within the bounds of the y coordinate of the button
            if (point.y >= (location.y - radius - hitBoxEnlarger) && point.y <= (location.y + radius + hitBoxEnlarger)) {

                return true;

            }

        }

        return false;

    }

    public void update() {

        if (isClicked) {

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

    void move() {

        if (Game.state == GameState.Tutorial) {

            if (startLocation == FrenzyCircle.RIGHT && location.x <= Game.screenSize.x / 2)
                stopForTutorial = true;
            else if (startLocation == FrenzyCircle.LEFT && location.x >= Game.screenSize.x / 2)
                stopForTutorial = true;
            else if (startLocation == FrenzyCircle.TOP && location.y >= Game.screenSize.y / 2)
                stopForTutorial = true;
            else if (startLocation == FrenzyCircle.BOTTOM && location.y <= Game.screenSize.y / 2)
                stopForTutorial = true;
            else {
                location.x += speedX;
                location.y += speedY;
            }

        } else {

            location.x += speedX;
            location.y += speedY;

        }

    }

}
