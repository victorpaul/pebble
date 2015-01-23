package com.sukinsan.pebble.application;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.sukinsan.pebble.service.WatchUpdaterService;

/**
 * Created by victorpaul on 14/1/15.
 */
public class PebbleApplication extends Application {
    public static final int SEND_UPDATE = 1000;
    public static final int DELAY_SEND_UPDATE = 1000 * 60 * 3;

    public Handler serviceHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case SEND_UPDATE:
                    startService(new Intent(getApplicationContext(), WatchUpdaterService.class));
                    sendEmptyMessageDelayed(SEND_UPDATE,DELAY_SEND_UPDATE);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        //serviceHandler.removeMessages(SEND_UPDATE);
        //serviceHandler.sendEmptyMessageDelayed(SEND_UPDATE,DELAY_SEND_UPDATE);// run with delay
        //startService(new Intent(getApplicationContext(), WatchUpdaterService.class)); // run now
    }
}