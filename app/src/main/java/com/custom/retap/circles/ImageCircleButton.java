package com.custom.retap.circles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.custom.retap.game.MyActivity;
import com.custom.retap.visuals.Draw;

public class ImageCircleButton extends StaticCircleButton {

    private final Bitmap image;

    private float imageWidth;
    private float imageHeight;

    private float imageX;
    private float imageY;

    private float imageSpeedX;
    private float imageSpeedY;

    public ImageCircleButton(String name, Point location, int preferredRadius, int imgID) {
        super(name, location, preferredRadius, 0);

        image = BitmapFactory.decodeResource(MyActivity.res, imgID);

        try {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        imageX = location.x - imageWidth / 2;
        imageY = location.y - imageHeight / 2;

        imageSpeedX = Math.abs(textSpeedX) + 0.5f;
        imageSpeedY = Math.abs(textSpeedY) - 0.5f;
    }

    @Override
    public void render(Canvas canvas) {

        paint.setColor(Draw.color);

        canvas.drawCircle(location.x, location.y, radius, paint);

        if (radius >= preferredRadius && image != null) {
            canvas.drawBitmap(image, imageX, imageY, paint);
        }
    }

    private void updateImageLocation() {
        imageX += imageSpeedX;
        imageY += imageSpeedY;
    }

    private void checkTextBounds() {

        if (imageX <= location.x - radius) imageSpeedX = Math.abs(imageSpeedX);

        if (imageX + imageWidth >= location.x + radius) imageSpeedX = -Math.abs(imageSpeedX);

        if (imageY <= location.y - radius) imageSpeedY = Math.abs(imageSpeedY);

        if (imageY + imageHeight >= location.y + radius) imageSpeedY = -Math.abs(imageSpeedY);

    }

    @Override
    public void update() {

        updateImageLocation();

        checkTextBounds();

        super.pulseCreate();

        if (isClicked) {

            isClicked = false;

            handler.buttonTouch(this.text);

        }

        super.timeToGoMethod();

    }
}
