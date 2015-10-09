package com.custom.retap.circles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.custom.retap.R;
import com.custom.retap.additions.ThemeManager;
import com.custom.retap.game.MyActivity;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

public class ColorCircle extends StaticCircleButton {

    int color;

    public boolean unlocked = true;

    public static boolean addNext = false;

    private boolean did = false;

    private static final Bitmap lock = BitmapFactory.decodeResource(MyActivity.res, R.drawable.lock);

    private final int GRAY = MyActivity.res.getColor(R.color.gray);

    public static boolean popAll = false;

    public ColorCircle(Point location, int preferredRadius, int color, boolean unlocked) {
        super(MyActivity.res.getString(R.string.color), location, preferredRadius, 0);

        this.color = color;

        this.unlocked = unlocked;

        paint.setStrokeWidth(Draw.LINE_WIDTH);
    }

    @Override
    public void processInput(Point location) {

        if (!Input.isDown && isClicked && unlocked && !popAll) {
            Draw.rainbow = false;
            handleTouch();
        }

        isClicked = false;

        if (intersects(location, true)) {

            if (Input.isDown) {

                isClicked = true;

            }
        }
    }

    @Override
    public void render(Canvas canvas) {

        drawCircle(canvas);
        drawBorder(canvas);
        drawLock(canvas);
    }

    private void drawLock(Canvas canvas) {

        if (!unlocked && !creating && !popAll) {

            float left = location.x - lock.getWidth() / 1.9f;
            float top = location.y - lock.getHeight() / 1.9f;

            canvas.drawBitmap(lock, left, top, paint);

        }

    }

    void handleTouch() {
        ThemeManager.changeTheme(color);
        handler.buttonTouch(text);
    }

    private void drawCircle(Canvas canvas) {

        if (unlocked) {
            paint.setColor(color);
            canvas.drawCircle(location.x, location.y, radius, paint);
        } else {
            paint.setColor(GRAY);
            canvas.drawCircle(location.x, location.y, radius, paint);
        }

    }

    private void drawBorder(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);

        if (!isClicked && unlocked) {

            paint.setColor(Color.WHITE);

            canvas.drawCircle(location.x, location.y, radius - (paint.getStrokeWidth() / 2 - 1), paint);

        } else if (!unlocked) {
            paint.setColor(color);
            canvas.drawCircle(location.x, location.y, radius - (paint.getStrokeWidth() / 2 - 1), paint);
        }

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void pulseCreate() {
        if (!popAll)
            super.pulseCreate();
    }

    @Override
    public void update() {

        pulseCreate();

        if (radius >= preferredRadius && !did) {
            addNext = true;
            did = true;
        }

        if (popAll) {

            radius -= diminishingRate;

            if (radius <= 0)
                radius = 0;

        }
    }
}
