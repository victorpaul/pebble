package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.utils.HardwareUtils;
import com.sukinsan.pebble.utils.SystemUtils;

/**
 * Created by victor on 10/26/16.
 */

public class PebbleDataService  extends BroadcastReceiver {
    private static final String TAG = PebbleDataService.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i(TAG,"onReceive");
        //PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
        SystemUtils.getCache(context,new Cache.CallBack() {
            @Override
            public boolean run(Cache cache) {

                HardwareUtils hardwareUtils = new HardwareUtils(context.getApplicationContext());
                hardwareUtils.sendUpdateToPebble(cache);

                return true;
            }
        });
    }
}
