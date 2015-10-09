package com.custom.retap.circles;

import com.custom.retap.additions.SplitScreenManager;
import com.custom.retap.game.Game;
import com.custom.retap.game.GameState;

import java.util.Random;

public class SSCircle extends Circle {

    public SSCircle() {

        radius = (int)SplitScreenManager.LINE_WIDTH;

        location.x = Game.screenSize.x / 2;
        location.y = new Random().nextInt(Game.screenSize.y - (2*radius)) + radius;
    }

    @Override
    void move() {

        if(Game.state != GameState.Tutorial) {

            if(startLocation == FrenzyCircle.LEFT) {
                location.x += speedX;
            } else if(startLocation == FrenzyCircle.RIGHT) {
                location.x -= speedX;
            }

        }
    }
}
