package com.sukinsan.pebble.service;

import android.app.IntentService;
import android.content.Intent;

import com.sukinsan.pebble.utils.HardwareUtils;

import java.util.Date;

/**
 * Created by victorpaul on 14/1/15.
 */
public class WatchUpdaterService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WatchUpdaterService() {
        super("pebble-updater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(HardwareUtils.isItOkToSensUpdateFromBackground(getApplicationContext())) {
            HardwareUtils.sendUpdateToPebble(getApplicationContext(), "Service sync: \n" + (new Date()).toString());
        }
    }
}
