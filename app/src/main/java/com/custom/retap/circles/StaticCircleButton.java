package com.custom.retap.circles;

import android.graphics.Point;

import com.custom.retap.visuals.Input;

public class StaticCircleButton extends CircleButton {

    public StaticCircleButton(String text, Point location, int preferredRadius, float textSize) {
        super(text, location, preferredRadius, textSize);
    }

    @Override
    public void processInput(Point location) {

        if (intersects(location, true)) {

            if (Input.isDown) {

                pulseUp();

            } else {

                isClicked = true;

                checkRadius();

            }

        } else {

            checkRadius();
        }

    }

    private void pulseUp() {

        if (radius < preferredRadius + displacementAmount) {

            radius += diminishingRate;

            if (radius > preferredRadius + displacementAmount) {

                radius = preferredRadius + displacementAmount;

            }

        }

    }

    @Override
    protected void checkRadius() {

        if (timeToGo || creating) return;

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

        super.pulseCreate();

        super.updateText();

        if (isClicked) {

            isClicked = false;

            handler.buttonTouch(this.text);

        }

        super.timeToGoMethod();

    }
}
