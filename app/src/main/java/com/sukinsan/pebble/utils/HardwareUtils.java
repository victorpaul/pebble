package com.sukinsan.pebble.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by viktor_2 on 1/9/15.
 */
public class HardwareUtils {

    public final static int KEY_DATE = 1;
    public final static int KEY_NETWORK = 2;
    public final static int KEY_BATTERY = 3;
    public final static int KEY_WEATHER = 4;
    public final static int KEY_DATA = 5;

    public final static UUID PEBBLE_APP_UUID = UUID.fromString("7b7c495e-1c45-48b6-85f9-7568adf74ec6");

    public static String getBatteryStatus(Context context){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        StringBuilder statusString = new StringBuilder(batteryPct + "%");

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

    public static void sendUpdateToPebble(Context context, String message){
        PebbleDictionary data = new PebbleDictionary();
        data.addString(KEY_DATE, (new SimpleDateFormat("dd/MMM/yyyy")).format(new Date()).toString());
        data.addString(KEY_NETWORK, getNetworkStatus(context));
        data.addString(KEY_BATTERY, HardwareUtils.getBatteryStatus(context));
        data.addString(KEY_WEATHER, "winter");
        data.addString(KEY_DATA, message);
        PebbleKit.sendDataToPebble(context, PEBBLE_APP_UUID, data);
    }

}
