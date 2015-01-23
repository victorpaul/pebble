package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sukinsan.pebble.utils.HardwareUtils;

import java.util.Date;

/**
 * Created by victorpaul on 23/1/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public final static String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive(Context context, Intent intent)");
        if(HardwareUtils.isItOkToSensUpdateFromBackground(context)) {
            HardwareUtils.sendUpdateToPebble(context, "Last sync from AlarmClock was at: \n" + (new Date()).toString());
        }
    }
}
