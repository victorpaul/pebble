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
import android.os.SystemClock;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sukinsan.pebble.broadcast.AlarmReceiver;
import com.sukinsan.pebble.entity.Cache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by viktor_2 on 1/9/15.
 */
public class HardwareUtils {
    public final static String TAG = HardwareUtils.class.getSimpleName();

    public final static String PEBBLE_APP_ID = "7b7c495e-1c45-48b6-85f9-7568adf74ec6";

    public final static int UPDATE_INTERVAL = 1000 * 60;

    public final static int KEY_DATE = 1;
    public final static int KEY_NETWORK = 2;
    public final static int KEY_BATTERY = 3;
    public final static int KEY_WEATHER = 4;
    public final static int KEY_DATA = 5;

    public final static UUID PEBBLE_APP_UUID = UUID.fromString(PEBBLE_APP_ID);

    public static String getBatteryStatus(Context context){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        StringBuilder statusString = new StringBuilder(level + "%");

        if(usbCharge){
            statusString.append("(usb)");
        }

        if(acCharge){
            statusString.append("(ac)");
        }

        return statusString.toString();
    }

    public static boolean isItOkToSensUpdateFromBackground(Context applicationContext){
        return PebbleKit.isWatchConnected(applicationContext);
    }

    public static String getNetworkStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return "wifi";
            }else{
                WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                if (wifi.isWifiEnabled()){
                    return "mobile(wifi)";
                }else{
                    return "mobile";
                }
            }
        }else{
            return "no connection";
        }
    }

    public static void sendUpdateToPebble(Context context, final String message){
        final PebbleDictionary data = new PebbleDictionary();

        final String batteryLevel = HardwareUtils.getBatteryStatus(context);
        final String networkStatus = getNetworkStatus(context);
        final String date = (new SimpleDateFormat("dd/MMM/yyyy")).format(new Date()).toString();
        final String weather = "winter";

        SystemUtils.getCache(context,new Cache.CallBack() {
            @Override
            public void run(Cache cache) {
                if(!cache.getLastBatteryLevel().equals(batteryLevel)) {
                    cache.setLastBatteryLevel(batteryLevel);
                    data.addString(KEY_BATTERY, batteryLevel);
                }
                if(!cache.getLastNetwork().equals(networkStatus)) {
                    cache.setLastNetwork(networkStatus);
                    data.addString(KEY_NETWORK, networkStatus);
                }
                if(!cache.getLastDateStatus().equals(date)){
                    cache.setLastDateStatus(date);
                    data.addString(KEY_DATE,date);
                }

                if(!cache.getLastDateStatus().equals(date)){
                    cache.setLastWeatherStatus(weather);
                    data.addString(KEY_WEATHER,weather);
                }

                data.addString(KEY_DATA, "data.size() == 0 " + data.size() + " " + message);

            }
        },false);

        if(data.size() > 0 ) {

            SystemUtils.saveCache(context);
            PebbleKit.sendDataToPebble(context, PEBBLE_APP_UUID, data);
        }
    }

    public static void runCron(Context context){
        Log.i(TAG, "runCron(Context context)");
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),UPDATE_INTERVAL,alarmIntent);
    }

}