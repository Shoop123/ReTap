package com.custom.retap.circles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.custom.retap.R;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;
import com.custom.retap.game.MyActivity;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;

public class ExpandingCircleButton extends CircleButton {

    private final int MULTIPLIER = Game.screenSize.y / 225;

    private GameState previousState;

    private boolean displaying = false;

    private boolean isLarge = false;

    private int displacement = 0;

    private Point touchLocation;

    private int displayHeight = 0;

    private final int BAR_WIDTH = Game.screenSize.y / 200;

    private final int AUTO_SCROLL_SPEED = Game.screenSize.y / 108;

    private final float buttonTextSize;

    private float slower = 1;

    private final int largeSize = Game.screenSize.x + location.x + 8;

    public boolean goBack = false;

    private final int COLOR = Draw.color;

    public ExpandingCircleButton(String text, Point location, int preferredRadius, float buttonTextSize) {
        super(text, location, preferredRadius, buttonTextSize);

        this.buttonTextSize = buttonTextSize;

    }

    @Override
    public void render(Canvas canvas) {
        if(!Draw.rainbow && Game.state != GameState.Alert) paint.setColor(COLOR);
        else paint.setColor(Draw.color);

        canvas.drawCircle(location.x, location.y, radius, paint);

        drawText(canvas);

        drawBar(canvas);

    }

    protected void drawText(Canvas canvas) {

        if (displaying) return;

        paint.setColor(Color.WHITE);

        paint.setTextSize(buttonTextSize);

        canvas.drawText(text, textLocation.x, textLocation.y, paint);

        paint.setColor(Draw.color);

    }

    private void drawBar(Canvas canvas) {

        if (!displaying || displayHeight < Game.screenSize.y) return;

        paint.setColor(Color.WHITE);

        final float MULTIPLY = ((float) Game.screenSize.y) / ((float) displayHeight);

        float rectHeight = Game.screenSize.y * MULTIPLY;

        Rect bar = new Rect();

        int maxDisplacement = displayHeight - Game.screenSize.y;

        int pixelPerScroll = Game.screenSize.y / maxDisplacement;

        int top = displacement * pixelPerScroll;

        bar.set(Game.screenSize.x - BAR_WIDTH, top, Game.screenSize.x, (int) (top + rectHeight));

        canvas.drawRect(bar, paint);

        paint.setColor(Draw.color);

    }

    private void dissipate() {

        if (!timeToGo) return;

        radius -= diminishingRate;

        isClicked = false;

    }

    @Override
    public void processInput(Point location) {

        if (intersects(location, true)) {

            if (Input.isDown && !creating) {

                pulseDown();

                if (touchLocation != null && displaying) {

                    displacement += (touchLocation.y - location.y) / slower;

                    touchLocation = location;

                } else if (displaying) {

                    touchLocation = location;

                }

            } else {

                isClicked = true;

                touchLocation = null;

            }

        } else {

            checkRadius();

        }

    }

    private void updateScroll() {

        if (!displaying) return;

        final int DIVIDER = 10;

        if (Input.isDown) {

            if (displacement > displayHeight - (Game.screenSize.y - (Game.screenSize.y / DIVIDER))) {

                slower += 0.5;

            } else if (displacement < -Game.screenSize.y / DIVIDER) {

                slower += 0.5;

            }

        } else {

            if (displacement > displayHeight - (Game.screenSize.y - (Game.screenSize.y / DIVIDER))) {

                displacement -= AUTO_SCROLL_SPEED;

            } else if (displacement < -Game.screenSize.y / DIVIDER) {

                displacement += AUTO_SCROLL_SPEED;

            } else {

                slower = 1;

            }

        }

    }

    private void pulseDown() {

        if (isLarge || timeToGo) return;

        if (radius > preferredRadius - displacementAmount) {

            radius -= diminishingRate;

            if (radius < preferredRadius - displacementAmount) {

                radius = preferredRadius - displacementAmount;

            }

        }

    }

    @Override
    protected void checkRadius() {

        if (timeToGo || isLarge) return;

        if (radius < preferredRadius) {

            radius += diminishingRate;

            if (radius > preferredRadius) radius = preferredRadius;

        } else if (radius > preferredRadius) {

            radius -= diminishingRate;

            if (radius < preferredRadius) radius = preferredRadius;

        }

    }

    @Override
    public void update() {

        pulseCreate();

        textLocation.x += textSpeedX;

        updateText();

        dissipate();

        updateScroll();

        if (isClicked) {

            if (!isLarge) {

                if (radius < largeSize) {

                    radius += diminishingRate * MULTIPLIER;

                    displaying = true;

                } else {

                    MyActivity.setImageSource(R.id.imgBack, R.drawable.back_white);

                    MyActivity.setVisible(R.id.imgBack, View.VISIBLE);

                    isLarge = true;

                    isClicked = false;

                    radius = largeSize;

                    previousState = Game.state;

                    Game.state = GameState.Themes;

                    Game.circles.clear();

                    Game.circles.offer(this);

                    handler.buttonTouch(this.text);

                }

            }

        }

        if (goBack) {

            if (radius > preferredRadius) {

                radius -= diminishingRate * MULTIPLIER;

            } else {

                displaying = false;

                Game.reset(previousState);

            }
        }

    }

}
