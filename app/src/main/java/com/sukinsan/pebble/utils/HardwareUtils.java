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
import java.util.Date;
import java.util.UUID;

/**
 * Created by viktor_2 on 1/9/15.
 */
public class HardwareUtils {
    public final static String TAG = HardwareUtils.class.getSimpleName();

    public final static String PEBBLE_APP_ID = "7b7c495e-1c45-48b6-85f9-7568adf74ec6";

    public final static int UPDATE_WEATHER_INTERVAL = 1000 * 60 * 20;
    public final static int UPDATE_WEATHER_INTERVAL_PEBBLE = 1000 * 60 * 5;
    public final static int UPDATE_INTERVAL = 1000 * 60 * 2;

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
                    return "mob(wifi)";
                }else{
                    return "mobile";
                }
            }
        }else{
            return "O_o";
        }
    }

    public static void sendUpdateToPebble(final Context context, final String message){
        final PebbleDictionary data = new PebbleDictionary();
        final String batteryLevel = HardwareUtils.getBatteryStatus(context);
        final String networkStatus = getNetworkStatus(context);
        final String date = (new SimpleDateFormat("EEE d, MMM")).format(new Date()).toString();

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

                if(cache.getWeather() == null || cache.getWeather().getLastUpdate() + UPDATE_WEATHER_INTERVAL < System.currentTimeMillis()){ // update weather every 20 minutes
                    new GetWeatherTask(context).execute();
                }

                if(cache.getWeather() != null && cache.getWeather().getLastUpdate() + UPDATE_WEATHER_INTERVAL_PEBBLE < System.currentTimeMillis()){
                    data.addString(KEY_WEATHER,cache.getWeather().getDescription());
                }

                data.addString(KEY_DATA, message);

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