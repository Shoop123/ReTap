package com.custom.retap.additions;

import com.custom.retap.R;
import com.custom.retap.game.MyActivity;
import com.purplebrain.giftiz.sdk.GiftizSDK;

public abstract class Mission {

    public static final int MISSION_COMPLETE_SCORE = 30;

    public static void completeMission() {

        GiftizSDK.missionComplete(MyActivity.activity);

    }

    public static void connectGiftiz() {

        if (MyActivity.hasConnection()) {

            GiftizSDK.Inner.buttonClicked(MyActivity.activity);

        } else {
            CircleCreator.createAlertCircle(MyActivity.res.getString(R.string.no_internet));
        }
    }
}
