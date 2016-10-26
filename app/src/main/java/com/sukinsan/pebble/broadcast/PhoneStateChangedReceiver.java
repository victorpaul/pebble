package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.utils.HardwareUtils;
import com.sukinsan.pebble.utils.SystemUtils;

/**
 * Created by victorpaul on 23/1/15.
 */
public class PhoneStateChangedReceiver extends BroadcastReceiver {
    private final static String TAG = PhoneStateChangedReceiver.class.getSimpleName();
    private HardwareUtils hardwareUtils;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        hardwareUtils = new HardwareUtils(context.getApplicationContext());

        if(intent.getAction() != null){
            Log.i(TAG,intent.getAction());
        }else{
            Log.i(TAG,"no action");
        }

        SystemUtils.getCache(context,new Cache.CallBack() {
            @Override
            public boolean run(Cache cache) {

            if(intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){ // NETWORK CHANGED
                int network = hardwareUtils.getNetworkStatus();

                if(cache.isShutDownWiFi() && cache.getLastNetwork() == HardwareUtils.KEY_NETWORK_WIFI && network != HardwareUtils.KEY_NETWORK_WIFI){
                    hardwareUtils.setWifiState(false);
                }
                cache.setLastNetwork(network);
            }else{ // BATTERY LEVEL/STATE
                cache.setLastBatteryInfo(hardwareUtils.getBatteryInfo());

                String battery = "";
                for(Integer status : cache.getLastBatteryInfo()){
                    battery += hardwareUtils.generateLog(status) + " ";
                }
            }

            hardwareUtils.sendUpdateToPebble(cache);

            cache.setLastCronJob(System.currentTimeMillis());
            return true;
            }
        });

    }
}
