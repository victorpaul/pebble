package com.sukinsan.pebble.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sukinsan.pebble.broadcast.AlarmReceiver;
import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.task.GetWeatherTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by viktor_2 on 1/9/15.
 */
public class HardwareUtils {
    public final static String TAG = HardwareUtils.class.getSimpleName();

    public final static String PEBBLE_APP_ID = "7b7c495e-1c45-48b6-85f9-7568adf74ec6";

    public final static int UPDATE_WEATHER_INTERVAL = 1000 * 60 * 20;
    public final static int UPDATE_WEATHER_INTERVAL_PEBBLE = 1000 * 60 * 1;
    public final static int UPDATE_INTERVAL = 1000 * 60 * 2;

    public final static int BATTERY_LEVEL_MIN = 1;
    public final static int BATTERY_LEVEL_MAX = 100;
    public final static int BATTERY_CHARGING_NONE = 101;
    public final static int BATTERY_CHARGING_USB = 102;
    public final static int BATTERY_CHARGING_SET = 103;

    public final static int KEY_NETWORK_WIFI = 201;
    public final static int KEY_NETWORK_MOBILE = 202;
    public final static int KEY_NETWORK_WIFI_MOBILE = 203;
    public final static int KEY_NETWORK_OFF = 204;

    public final static int KEY_WEATHER = 300;
    public final static int KEY_DATE = 400;

    public final static UUID PEBBLE_APP_UUID = UUID.fromString(PEBBLE_APP_ID);

    public static List<Integer> getBatteryInfo(Context context){
        List<Integer> response = new ArrayList<Integer>(); // we are expecting only two parameters

        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if(level <= BATTERY_LEVEL_MAX || level >= BATTERY_LEVEL_MIN){
            response.add(level);
        }

        if(chargePlug == BatteryManager.BATTERY_PLUGGED_USB){
            response.add(BATTERY_CHARGING_USB);
        }else if(chargePlug == BatteryManager.BATTERY_PLUGGED_AC){
            response.add(BATTERY_CHARGING_SET);
        }else{
            response.add(BATTERY_CHARGING_NONE);
        }

        return response;
    }

    public static boolean isItOkToSensUpdateFromBackground(Context applicationContext){
        return PebbleKit.isWatchConnected(applicationContext);
    }

    public static int getNetworkStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return KEY_NETWORK_WIFI;
            }else{
                WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()){
                    return KEY_NETWORK_WIFI_MOBILE;
                }else{
                    return KEY_NETWORK_MOBILE;
                }
            }
        }else{
            return KEY_NETWORK_OFF;
        }
    }

    public static void sendUpdateToPebble(final Context context){
        final PebbleDictionary data = new PebbleDictionary();
        final List<Integer> batteryInfo = HardwareUtils.getBatteryInfo(context);
        final int networkStatus = getNetworkStatus(context);
        final String date = (new SimpleDateFormat("EEE d, MMM")).format(new Date()).toString();

        SystemUtils.getCache(context,new Cache.CallBack() {
            @Override
            public void run(Cache cache) {

                if(!batteryInfo.equals(cache.getLastBatteryInfo())) {
                    cache.setLastBatteryInfo(batteryInfo);
                    for (int i = 0; i < batteryInfo.size(); i++) {
                        data.addUint8(batteryInfo.get(i), (byte) 0);
                    }
                }

                if(networkStatus != cache.getLastNetwork()) {
                    cache.setLastNetwork(networkStatus);
                    data.addUint8(networkStatus, (byte) 0);
                }

                if(!cache.getLastDateStatus().equals(date)){
                    cache.setLastDateStatus(date);
                    data.addString(KEY_DATE,date);
                }

                if(cache.getWeather() == null || cache.getWeather().getLastUpdate() + UPDATE_WEATHER_INTERVAL < System.currentTimeMillis()){ // update weather every 20 minutes
                    new GetWeatherTask(context).execute();
                }

                if(cache.getWeather() != null && cache.getWeather().getLastUpdate() + UPDATE_WEATHER_INTERVAL_PEBBLE < System.currentTimeMillis()){
                    data.addString(KEY_WEATHER,cache.getWeather().getDescription());
                }

            }
        },false);

        if(data.size() > 0 ) {
            SystemUtils.saveCache(context);
            PebbleKit.sendDataToPebble(context, PEBBLE_APP_UUID, data);
            //PebbleKit.sendDataToPebbleWithTransactionId(context, PEBBLE_APP_UUID, data);
        }
    }

    public static void runCron(Context context){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),UPDATE_INTERVAL,alarmIntent);
    }

}