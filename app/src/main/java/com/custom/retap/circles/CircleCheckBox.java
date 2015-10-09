package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;

import com.custom.retap.game.Game;
import com.custom.retap.visuals.Draw;

public class CircleCheckBox extends StaticCircleButton {

    private boolean isChecked = true;

    private long lastChangeTime;

    private static final String CHECK_MARK = "✓";

    public CircleCheckBox(String text, Point location, int preferredRadius, long textSize) {
        super(text, location, preferredRadius, textSize);

        init();

    }

    private void init() {

        initPaint();

        diminishingRate = preferredRadius / 12;

        displacementAmount = Game.screenSize.y / 100;

        paint.setTextSize(TEXT_SIZE);

        textHeightStart = Game.screenSize.y / 15.88235f;
        textWidth = Game.screenSize.y / 27;

    }

    @Override
    public void render(Canvas canvas) {

        paint.setColor(Draw.color);

        canvas.drawCircle(location.x, location.y, radius, paint);

        renderText(canvas);

    }

    private void renderCheckMark(Canvas canvas) {

        if (!isChecked) return;

        paint.setColor(Color.WHITE);

        canvas.drawText(CHECK_MARK, textLocation.x, textLocation.y, paint);

    }

    private void renderText(Canvas canvas) {

        Rect textBounds = new Rect();

        final int TEXT_OFFSET = 10;

        paint.getTextBounds(text, 0, text.length() - 1, textBounds);

        float xPos = location.x + radius + TEXT_OFFSET;

        float yPos = location.y + (textBounds.height() / 2);

        canvas.drawText(text, xPos, yPos, paint);

        renderCheckMark(canvas);

    }

    @Override
    public void update() {

        super.pulseCreate();

        super.updateText();

        if (isClicked) {

            long currentChangeTime = System.currentTimeMillis();
            long elapsedTime = currentChangeTime - lastChangeTime;
            final int PREFERRED_CHANGE_TIME = 500;

            if (elapsedTime >= PREFERRED_CHANGE_TIME)
                setChecked(!isChecked);

            isClicked = false;

            lastChangeTime = currentChangeTime;

        }

        super.timeToGoMethod();

    }

    private void setChecked(boolean isChecked) {

        this.isChecked = isChecked;

        handler.checkChanged(this.text);
    }

    public boolean isChecked() {
        return isChecked;
    }

}