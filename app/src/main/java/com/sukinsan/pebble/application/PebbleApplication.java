package com.sukinsan.pebble.application;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.sukinsan.pebble.entity.HardwareLog;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import anDB.DBHandler;

/**
 * Created by victorpaul on 14/1/15.
 */
public class PebbleApplication extends Application {
    public static final int SEND_UPDATE = 1000;
    public static final int DELAY_SEND_UPDATE = 1000 * 60 * 3;

    public static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void onCreate() {
        super.onCreate();
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        dbHandler.create(HardwareLog.class);

    }
}