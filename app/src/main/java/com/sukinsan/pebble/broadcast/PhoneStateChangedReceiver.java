package com.sukinsan.pebble.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.task.GetWeatherTask;
import com.sukinsan.pebble.utils.HardwareUtils;
import com.sukinsan.pebble.utils.SystemUtils;

/**
 * Created by victorpaul on 23/1/15.
 */
public class PhoneStateChangedReceiver extends BroadcastReceiver {
    public final static String TAG = PhoneStateChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(intent.getAction() != null){
            Log.i(TAG,intent.getAction());
        }else{
            Log.i(TAG,"no action");
        }

        SystemUtils.getCache(context,new Cache.CallBack() {
            @Override
            public boolean run(Cache cache) {

                if(cache.getLastCronJob() + HardwareUtils.UPDATE_INTERVAL_MINIMAL > System.currentTimeMillis()){ // prevent sending data to watch too frequently

                    /**
                     * check if user didn't change time on the phone manually,
                     * if user set time next friday, and set it back - there is a possibility that cache.getLastCronJob() would return time in future
                     * so  previous condition would bew true until next friday
                     */
                    if(cache.getLastCronJob() > System.currentTimeMillis()){
                        cache.setLastCronJob(System.currentTimeMillis());
                        return true;
                    }
                    return false;
                }

                if(intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){ // NETWORK CHANGED
                    int network = HardwareUtils.getNetworkStatus(context);

                    if(cache.isShutDownWiFi() && cache.getLastNetwork() == HardwareUtils.KEY_NETWORK_WIFI && network != HardwareUtils.KEY_NETWORK_WIFI){
                        HardwareUtils.setWifiState(context,false);
                    }

                    cache.setLastNetwork(network);
                }else{ // BATTERY LEVEL/STATE, WEATHER
                    cache.setLastBatteryInfo(HardwareUtils.getBatteryInfo(context));

                    if(cache.getWeather() == null || cache.getWeather().getLastUpdate() + HardwareUtils.UPDATE_WEATHER_INTERVAL < System.currentTimeMillis()) {
                        new GetWeatherTask(context).execute();
                    }
                }

                HardwareUtils.sendUpdateToPebble(cache,context.getApplicationContext());

                cache.setLastCronJob(System.currentTimeMillis());
                return true;

            }
        });

    }
}
