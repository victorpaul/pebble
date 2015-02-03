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

        if(HardwareUtils.isItOkToSensUpdateFromBackground(context.getApplicationContext())) {
            Log.i(TAG, "onReceive(Context context, Intent intent) OK to send update");
            HardwareUtils.sendUpdateToPebble(context.getApplicationContext(), "sync at:" + (new Date()).toString());
        }else{
            Log.i(TAG, "onReceive(Context context, Intent intent) IT'S NOT OK to send update");
        }
    }
}
