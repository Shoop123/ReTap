package com.custom.retap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.custom.retap.additions.ThemeManager;
import com.custom.retap.circles.AlertCircle;
import com.custom.retap.visuals.Draw;
import com.custom.retap.visuals.Input;
import com.custom.retap.visuals.Update;

public class GameLoop implements Runnable {

    //Whether the game is running or not
    private boolean isRunning = false;

    //Stores the holder on which everything will be drawn
    private final SurfaceHolder surfaceHolder;

    private static int elapsed = 1;

    private Draw draw;

    public static Update update;

    private final Input input;

    private final int RESTART_TIME = 0;

    private long twoSeconds = RESTART_TIME;

    private long started5Seconds = RESTART_TIME;

    private long startedZeroPointFiveSeconds = RESTART_TIME;

    public static float fps;

    public static final float TARGET_FPS = 60;

    public GameLoop(SurfaceHolder surfaceHolder, Input input) {

        this.surfaceHolder = surfaceHolder;

        this.input = input;

        isRunning = true;

    }

    @Override
    public void run() {

        if(draw == null) draw = new Draw(input);

        if(update == null) update = new Update(input);

        if (Game.state != GameState.InGame && Game.state != GameState.Paused && Game.state != GameState.Tutorial) Game.showPlayScreen();
        else if(Game.state == GameState.InGame) update.getGameData();

        long last = System.currentTimeMillis();

        final int FPS = 1000 / 60;

        while (isRunning) {

            long current = System.currentTimeMillis();
            elapsed = (int) (current - last);

            if (elapsed >= FPS) {

                processInput();
                update();
                render();

                last = current;

            }

        }

    }

    private void render() {

        Canvas canvas = null;

        try {

            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            draw.renderTouchDownTime(canvas);

            draw.render(canvas);

        } catch (NullPointerException npe) {

            npe.printStackTrace();

        } finally {

            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);

        }

    }

    private void processInput() {

        input.processInput();

    }

    private void update() {

        fps = 1000.0F / elapsed;

        update.update();

        ThemeManager.update();

        update.generateCircle();

        update.checkScore();

        update.updateTouchDownTime();

        updateTime();

    }

    private void updateTime() {

        if (startedZeroPointFiveSeconds == RESTART_TIME && Draw.rainbow)
            startedZeroPointFiveSeconds = System.currentTimeMillis();

        if (System.currentTimeMillis() - startedZeroPointFiveSeconds >= Draw.COLOR_CHANGE_TIME & Draw.rainbow) {

            Draw.color = Draw.changeColor(Draw.color);

            if (Game.state != GameState.Themes) {
                MyActivity.setTextColor(Draw.color);

                MyActivity.setImageColor(Draw.color);
            }

            if(Game.state == GameState.Alert) AlertCircle.changeColor = true;

            startedZeroPointFiveSeconds = RESTART_TIME;

        }

        if (Game.state != GameState.InGame) {

            twoSeconds = RESTART_TIME;

            started5Seconds = RESTART_TIME;

            return;

        }

        if (twoSeconds == RESTART_TIME) twoSeconds = System.currentTimeMillis();

        if (started5Seconds == RESTART_TIME) started5Seconds = System.currentTimeMillis();

        final int FIVE_SECONDS = 5000;

        final int TWO_SECONDS = 2000;

        if (System.currentTimeMillis() - twoSeconds >= TWO_SECONDS) {

            update.twoSeconds();

            twoSeconds = RESTART_TIME;

        }

        if (System.currentTimeMillis() - started5Seconds >= FIVE_SECONDS) {

            update.fiveSeconds();

            started5Seconds = RESTART_TIME;

        }

    }

    public void stop() {

        isRunning = false;

    }

}
