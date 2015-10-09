package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.custom.retap.game.Game;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

import java.util.Random;

public class CircleButton extends Circle {

    final String text;

    protected Point textLocation;
    float textSpeedX;
    float textSpeedY;
    private float textSpeed;

    float textWidth;
    float textHeightStart;
    private  float textHeightEnd;

    final Handler handler;

    int displacementAmount;

    boolean creating = true;
    private boolean reverse = false;

    final int preferredRadius;

    final float TEXT_SIZE;

    public static boolean timeToGo = false;

    private final float thetaMultiplyer = 0.8F;

    private final Random rnd = new Random();

    public CircleButton(String TEXT, Point location, int preferredRadius, float textSize) {

        this.location = location;

        this.preferredRadius = preferredRadius;

        radius = 1;

        this.TEXT_SIZE = textSize;

        final int DISPLACEMENT_DIVIDER = 6;

        displacementAmount = preferredRadius / DISPLACEMENT_DIVIDER;

        handler = new Handler();

        this.text = TEXT;

        init();

    }

    private void init() {

        initPaint();

        paint.setTextSize(TEXT_SIZE);

        textSpeedY = ((float) Game.screenSize.y) / 720.0F;

        textSpeedX = textSpeedY;

        textSpeed = (float)Math.hypot(textSpeedX, textSpeedY);

        Random rnd = new Random();

        boolean inverseX = rnd.nextBoolean();

        boolean inverseY = rnd.nextBoolean();

        if(inverseX) textSpeedX = -textSpeedX;
        if(inverseY) textSpeedY = -textSpeedY;

        getTextProperties(text);

        textLocation = new Point(location.x - Math.round(textWidth / 2), location.y + ((int)textHeightStart / 2));

    }

    private void getTextProperties(String text) {

        Rect textBounds = new Rect();

        textWidth = paint.measureText(text) - 1;
        paint.getTextBounds(text, 0, text.length() - 1, textBounds);
        textHeightStart = textBounds.height();

        paint.getTextBounds(text, text.length() - 2, text.length() - 1, textBounds);
        textHeightEnd = textBounds.height();

    }

    @Override
    public void render(Canvas canvas) {

        paint.setColor(Draw.color);

        canvas.drawCircle(location.x, location.y, radius, paint);

        drawText(canvas);
    }

    void drawText(Canvas canvas) {

        paint.setColor(Color.WHITE);

        canvas.drawText(text, textLocation.x, textLocation.y, paint);

        //drawTextBounds(canvas); // use for debugging

    }

    private void updateTopLeft() {

        float x = location.x - textLocation.x;
        float y = location.y - textLocation.y + textHeightStart;

        if(x < 0 || y < 0) return;

        float sumOfSquares = (x*x) + (y*y);

        if(sumOfSquares > radius*radius) {

            float slope = y/x;

            float theta = (float)Math.atan(slope);

            theta += Math.PI/6 + rnd.nextDouble() * thetaMultiplyer;

            if(textLocation.x > location.x) theta += Math.PI;

            textSpeedX = (float)Math.cos(theta) * textSpeed;
            textSpeedY = (float)Math.sin(theta) * textSpeed;

        }

    }

    private void updateBottomLeft() {

        float x = location.x - textLocation.x;
        float y = location.y - textLocation.y;

        if(x < 0 || y > 0) return;

        float sumOfSquares = (x*x) + (y*y);

        if(sumOfSquares > radius*radius) {

            float slope = y/x;

            float theta = (float)Math.atan(slope);

            theta -= Math.PI/6 + rnd.nextDouble() * thetaMultiplyer;

            if(textLocation.x > location.x) theta -= Math.PI;

            textSpeedX = (float)Math.cos(theta) * textSpeed;
            textSpeedY = (float)Math.sin(theta) * textSpeed;

        }

    }

    private void updateBottomRight() {

        float x = location.x - (textLocation.x + textWidth);
        float y = location.y - textLocation.y;

        if(x > 0 || y > 0) return;

        float sumOfSquares = (x*x) + (y*y);

        if(sumOfSquares > radius*radius) {

            float slope = y/x;

            float theta = (float)Math.atan(slope);

            theta += Math.PI/6 + rnd.nextDouble() * thetaMultiplyer;

            if(textLocation.x + textWidth > location.x) theta += Math.PI;

            textSpeedX = (float)Math.cos(theta) * textSpeed;
            textSpeedY = (float)Math.sin(theta) * textSpeed;

        }

    }

    private void updateTopRight() {

        float x = location.x - (textLocation.x + textWidth);
        float y = location.y - textLocation.y + textHeightEnd;

        if(x > 0 || y < 0) return;

        float sumOfSquares = (x*x) + (y*y);

        if(sumOfSquares > radius*radius) {

            float slope = y/x;

            float theta = (float)Math.atan(slope);

            theta -= Math.PI/6 + rnd.nextDouble() * thetaMultiplyer;

            if(textLocation.x + textWidth > location.x) theta -= Math.PI;

            textSpeedX = (float)Math.cos(theta) * textSpeed;
            textSpeedY = (float)Math.sin(theta) * textSpeed;

        }

    }

    private void drawTextBounds(Canvas canvas) {

        paint.setColor(Color.RED);

        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(textLocation.x, textLocation.y - textHeightStart, textLocation.x + paint.measureText(text.substring(0, 1)), textLocation.y, paint);

        paint.setColor(Color.BLUE);

        float left = textLocation.x + (textWidth - paint.measureText(text.substring(text.length() - 2, text.length() - 1)));

        canvas.drawRect(left, textLocation.y - textHeightEnd, left + paint.measureText(text.substring(text.length() - 2, text.length() - 1)), textLocation.y, paint);

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Draw.color);

    }

    void updateText() {

        updateTopLeft();
        updateBottomLeft();
        updateBottomRight();
        updateTopRight();

        textLocation.x += Math.round(textSpeedX);

        textLocation.y += Math.round(textSpeedY);

    }

    void pulseCreate() {

        if (creating && !timeToGo) {

            if (radius < preferredRadius + displacementAmount && !reverse) {

                radius += diminishingRate;

            } else if (radius >= preferredRadius + displacementAmount || reverse) {

                reverse = true;

                radius -= diminishingRate;

                if (radius <= preferredRadius) {

                    radius = preferredRadius;

                    creating = false;

                    reverse = false;

                }

            }

        }

    }

    @Override
    public void processInput(Point location) {

        if (intersects(location, true)) {

            if (Input.isDown && !isClicked) {

                pulseClick();

            } else {

                isClicked = true;

            }

        } else {

            checkRadius();
        }

    }

    void checkRadius() {

        if (isClicked || timeToGo) return;

        if (radius < preferredRadius) {

            radius += diminishingRate;

            if (radius > preferredRadius) radius = preferredRadius;

        } else if (radius > preferredRadius) {

            radius -= diminishingRate;

            if (radius < preferredRadius) radius = preferredRadius;

        }

    }

    private void pulseClick() {

        if (radius < preferredRadius + displacementAmount) {

            radius += diminishingRate;

        }

    }

    @Override
    public void update() {

        pulseCreate();

        updateText();

        if (isClicked) {

            if(!(this instanceof AlertCircle))
                timeToGo = true;

            creating = false;

            radius -= diminishingRate;

            if (radius <= 0) {

                handler.buttonTouch(this.text);

            }

        }

        timeToGoMethod();

    }

    void timeToGoMethod() {
        if (timeToGo && !isClicked) {

            creating = false;

            radius -= diminishingRate;

            if (radius <= 0) {

                radius = 0;

            }
        }
    }
}