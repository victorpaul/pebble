package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sukinsan.pebble.utils.HardwareUtils;

/**
 * Created by victorpaul on 23/1/15.
 */
public class BootReceiver extends BroadcastReceiver {
    public final static String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            new HardwareUtils(context).runCron();
        }

    }
}
