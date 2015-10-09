package com.custom.retap.additions;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.custom.retap.circles.AlertCircle;
import com.custom.retap.circles.Circle;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

public abstract class SplitScreenManager {

    public static int player1Lives = Game.STARTING_LIVES;

    public static int player2Lives = Game.STARTING_LIVES;

    public static final int MAX_TOUCH_DOWN_TIME_SS = 1500;

    public static int touchDownTimeP1 = 0;

    public static int touchDownTimeP2 = 0;

    public static final Point[] p1Taps = new Point[Input.MAX_RENDERED_TAPS];

    public static final Point[] p2Taps = new Point[Input.MAX_RENDERED_TAPS];

    public static boolean touchDownP1 = false;

    public static boolean touchDownP2 = false;

    public static final float LINE_WIDTH = Game.screenSize.y / 8f;

    private static long startTimeForTouch = 0;

    public static void renderProgressCircles(Canvas canvas, Paint paint) {

        //renderP1ProgressCircle(canvas, paint);

        //renderP2ProgressCircle(canvas, paint);
    }

    private static void renderP1ProgressCircle(Canvas canvas, Paint paint) {

        paint.setStrokeWidth(Draw.LINE_WIDTH / 1.5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Draw.color);

        float radius = Game.screenSize.y / 15f;

        float realDiameter = radius * 2 + (Draw.LINE_WIDTH / 1.5f);

        canvas.drawCircle(Game.screenSize.x - radius, Game.screenSize.y - radius, radius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        float percent = ((float)touchDownTimeP1 / MAX_TOUCH_DOWN_TIME_SS) * 100.0f;

        if(percent > 100) percent = 100;

        float realPercent = percent * 2f;

        float newSize = (realDiameter / 100f) * realPercent;

        float rightOne = realDiameter - newSize;

        float rightTwo = 0;

        boolean drawNext = false;

        if(realPercent > 100) {

            realPercent -= 100;

            drawNext = true;

            rightOne = 0;

        }

        if(drawNext) {

            newSize = (realDiameter / 100f) * realPercent;

            rightTwo = newSize;

        }

        canvas.drawRect(Game.screenSize.x - realDiameter, Game.screenSize.y - realDiameter, Game.screenSize.x - rightOne, Game.screenSize.y - realDiameter / 2f, paint);

        canvas.drawRect(Game.screenSize.x - rightTwo, Game.screenSize.y - realDiameter / 2f, Game.screenSize.x, Game.screenSize.y, paint);

        paint.setStrokeWidth(1);

    }

    private static void renderP2ProgressCircle(Canvas canvas, Paint paint) {

        paint.setStrokeWidth(Draw.LINE_WIDTH / 1.5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Draw.color);

        float radius = Game.screenSize.y / 15f;

        float realDiameter = radius * 2 + (Draw.LINE_WIDTH / 1.5f);

        canvas.drawCircle(radius, radius, radius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        float percent = ((float)touchDownTimeP2 / MAX_TOUCH_DOWN_TIME_SS) * 100.0f;

        if(percent > 100) percent = 100;

        float realPercent = percent * 2f;

        float newSize = (realDiameter / 100f) * realPercent;

        float leftOne = newSize;

        float leftTwo = realDiameter;

        boolean drawNext = false;

        if(realPercent > 100) {

            realPercent -= 100;

            drawNext = true;

            leftOne = realDiameter;

        }

        if(drawNext) {

            newSize = (realDiameter / 100f) * realPercent;

            leftTwo = realDiameter - newSize;

        }

        canvas.drawRect(0, realDiameter / 2f, leftOne, realDiameter, paint);

        canvas.drawRect(leftTwo, 0, realDiameter, realDiameter / 2f, paint);

        paint.setStrokeWidth(1);

    }

    public static void renderCenterForSplitScreen(Canvas canvas, Paint paint) {

        if(!Game.splitScreenMode || AlertCircle.isInAlert) return;

        paint.setColor(Color.WHITE);

        float width = LINE_WIDTH;

        canvas.drawRect(Game.screenSize.x / 2 - width, 0, Game.screenSize.x / 2 + width, Game.screenSize.y, paint);

        paint.setColor(Draw.color);

    }

    public static void updateTouchDownTime() {

        long currentTime = System.currentTimeMillis();

        int elapsedTime = 0;

        if (Game.state != GameState.Paused) elapsedTime = (int) (currentTime - startTimeForTouch);

        if (touchDownP1) {

            touchDownTimeP1 += elapsedTime;

        } else {

            if (touchDownTimeP1 > 0) {

                touchDownTimeP1 -= elapsedTime;

            } else {

                touchDownTimeP1 = 0;

            }

        }

        if (touchDownP2) {

            touchDownTimeP2 += elapsedTime;

        } else {

            if (touchDownTimeP2 > 0) {

                touchDownTimeP2 -= elapsedTime;

            } else {

                touchDownTimeP2 = 0;

            }

        }

        startTimeForTouch = currentTime;

    }

    public static void processSSInput() {

        if(p1Taps.length > 0) {

            for(int i = 0; i < p1Taps.length; i++) {

                if (p1Taps[i] == null) continue;

                for (Circle circle : Game.circles) {

                    circle.processInput(p1Taps[i]);

                }

                p1Taps[i] = null;

            }

        }

        if(p2Taps.length > 0) {

            for(int i = 0; i < p2Taps.length; i++) {

                if (p2Taps[i] == null) continue;

                for (Circle circle : Game.circles) {

                    circle.processInput(p2Taps[i]);

                }

                p2Taps[i] = null;

            }

        }

    }

    public static void recordInput(MotionEvent motionEvent) {

        Point tapLocation;

        int index = motionEvent.getActionIndex();

        int currentID = motionEvent.getPointerId(index);

        tapLocation = new Point((int) motionEvent.getX(motionEvent.findPointerIndex(currentID)), (int) motionEvent.getY(motionEvent.findPointerIndex(currentID)));

        if (tapLocation.x > Game.screenSize.x / 2 + LINE_WIDTH && touchDownTimeP1 < MAX_TOUCH_DOWN_TIME_SS) {

            touchDownP1 = true;

            for (int j = 0; j < p1Taps.length; j++) {

                if (j == p1Taps.length - 1 && p1Taps[j] != null)
                    p1Taps[0] = tapLocation;
                else if (p1Taps[j] == null) p1Taps[j] = tapLocation;

            }

        } else if(tapLocation.x < Game.screenSize.x / 2 - LINE_WIDTH && touchDownTimeP2 < MAX_TOUCH_DOWN_TIME_SS){

            touchDownP2 = true;

            for (int j = 0; j < p2Taps.length; j++) {

                if (j == p2Taps.length - 1 && p2Taps[j] != null)
                    p2Taps[0] = tapLocation;
                else if (p2Taps[j] == null) p2Taps[j] = tapLocation;

            }

        }

    }

    public static void actionUp(MotionEvent motionEvent) {

        if(motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {

            int id = motionEvent.getPointerId(0);

            int x = (int) motionEvent.getX(motionEvent.findPointerIndex(id));

            if (x > Game.screenSize.x / 2) {
                touchDownP1 = false;
            } else if (x < Game.screenSize.x / 2){
                touchDownP2 = false;
            }

        } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {

            int index = motionEvent.getActionIndex();

            int id = motionEvent.getPointerId(index);

            int x = (int) motionEvent.getX(motionEvent.findPointerIndex(id));

            if (x > Game.screenSize.x / 2) {
                touchDownP1 = false;
            } else if (x < Game.screenSize.x / 2){
                touchDownP2 = false;
            }

        }

    }

}
