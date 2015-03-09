package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.getpebble.android.kit.PebbleKit;
import com.sukinsan.pebble.utils.HardwareUtils;

/**
 * Created by victorpaul on 23/1/15.
 */
public class PhoneStateChangedReceiver extends BroadcastReceiver {
    public final static String TAG = PhoneStateChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if( PebbleKit.isWatchConnected(context)) {
            HardwareUtils.sendUpdateToPebble(context.getApplicationContext());
        }
    }
}
