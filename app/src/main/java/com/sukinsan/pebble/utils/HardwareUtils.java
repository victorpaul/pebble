package com.sukinsan.pebble.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sukinsan.pebble.broadcast.PhoneStateChangedReceiver;
import com.sukinsan.pebble.entity.Cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by viktor_2 on 1/9/15.
 */
public class HardwareUtils {
    public final static String TAG = HardwareUtils.class.getSimpleName();

    public final static String PEBBLE_APP_ID = "7b7c495e-1c45-48b6-85f9-7568adf74ec6";
    public final static UUID PEBBLE_APP_UUID = UUID.fromString(PEBBLE_APP_ID);

    public final static double ON_BOARD_PEBBLE_APP_VERSION = 1.0;
    public final static String ON_BOARD_PEBBLE_APP_FILENAME = "Friendly_Watch.pbw";

    public final static int UPDATE_WEATHER_INTERVAL = 1000 * 60 * 60; // every hour

    public final static int UPDATE_INTERVAL_MINIMAL = 1000 * 5;
    public final static int UPDATE_INTERVAL = 1000 * 60 * 2;

    public final static int BATTERY_LEVEL_MIN = 1;
    public final static int BATTERY_LEVEL_MAX = 100;
    public final static int BATTERY_CHARGING_NONE = 101;
    public final static int BATTERY_CHARGING_USB = 102;
    public final static int BATTERY_CHARGING_SET = 103;
    public final static int BATTERY_CHARGING_WIRELESS = 104;

    public final static int KEY_NETWORK_WIFI = 201;
    public final static int KEY_NETWORK_MOBILE = 202;
    public final static int KEY_NETWORK_OFF = 204;

    public final static int KEY_WEATHER = 300;


    public static List<Integer> getBatteryInfo(Context context){
        List<Integer> response = new ArrayList<Integer>();
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if(level <= BATTERY_LEVEL_MAX || level >= BATTERY_LEVEL_MIN){
            response.add(level);
        }

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        if(chargePlug == BatteryManager.BATTERY_PLUGGED_USB){
            response.add(BATTERY_CHARGING_USB);
        }else if(chargePlug == BatteryManager.BATTERY_PLUGGED_AC){
            response.add(BATTERY_CHARGING_SET);
        }else if(chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS){
            response.add(BATTERY_CHARGING_WIRELESS);
        }else{
            response.add(BATTERY_CHARGING_NONE);
        }

        return response;
    }

    public static int getNetworkStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return KEY_NETWORK_WIFI;
            }else{
                return KEY_NETWORK_MOBILE;
            }
        }else{
            return KEY_NETWORK_OFF;
        }
    }

    public static void sendUpdateToPebble(Cache cache,Context context){
        if(!PebbleKit.isWatchConnected(context)){
            return;
        }
        PebbleDictionary data = new PebbleDictionary();

        data.addUint8(cache.getLastNetwork(), (byte) 0);
        for (int i = 0; i < cache.getLastBatteryInfo().size(); i++) {
            data.addUint8(cache.getLastBatteryInfo().get(i), (byte) 0);
        }

        if(cache.getWeather() != null){
            data.addString(KEY_WEATHER,cache.getWeather().getDescription());
        }

        PebbleKit.sendDataToPebble(context, PEBBLE_APP_UUID, data);
    }

    public static void runCron(Context context){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PhoneStateChangedReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),UPDATE_INTERVAL,alarmIntent);
    }

    public static void sendAppToWatch(Context context) {
        if(!PebbleKit.isWatchConnected(context)){
            return;
        }
        try {
            InputStream input = context.getAssets().open(HardwareUtils.ON_BOARD_PEBBLE_APP_FILENAME);
            File file = new File(Environment.getExternalStorageDirectory(), HardwareUtils.ON_BOARD_PEBBLE_APP_FILENAME);
            file.setReadable(true, false);
            OutputStream output = new FileOutputStream(file);
            try  {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
                output.flush();
            } finally {
                output.close();
            }
            input.close();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.getpebble.android", "com.getpebble.android.ui.UpdateActivity");
            intent.setDataAndType(Uri.fromFile(file), "application/octet-stream");
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Oops.....", Toast.LENGTH_LONG).show();
        }
    }

    public static void setWifiState(Context context,boolean state){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
    }

}