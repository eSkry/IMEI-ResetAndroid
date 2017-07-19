package com.app.eskry.imeireset;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by root on 27.03.17.
 */

public class WidgetService extends IntentService {

    public WidgetService(){
        super("IMEIWidget");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction().equals("ACTION_TO_START_WORK")) {
        }
    }

}
