package com.sukinsan.pebble.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sukinsan.pebble.R;
import com.sukinsan.pebble.entity.Cache;
import com.sukinsan.pebble.utils.HardwareUtils;
import com.sukinsan.pebble.utils.SystemUtils;


public class MainActivity extends Activity implements View.OnClickListener {
    public final static String TAG = MainActivity.class.getSimpleName();

    private boolean isPebbleConnected = false;

    private BroadcastReceiver pebbleConnectedReceiver = null;
    private BroadcastReceiver pebbleDataReceiver = null;
    private BroadcastReceiver pebbleDisconnectedReceiver = null;

    private TextView pebbleStatus;
    private View buttonInstall;
    private CheckBox checkBoxShutDownWiFi;
    private CheckBox checkShowReadme;
    private HardwareUtils hardwareUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hardwareUtils = new HardwareUtils(this);
        hardwareUtils.runCron();

        // pebble connecting status
        pebbleStatus = (TextView)findViewById(R.id.txt_pebble_status);

        // to install pebble app on pebble watch
        buttonInstall = findViewById(R.id.btn_install_pebble_app);
        buttonInstall.setOnClickListener(this);

        // set up wifi shutting down
        checkBoxShutDownWiFi = (CheckBox)findViewById(R.id.checkbox_shutdown_wifi);
        checkBoxShutDownWiFi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                SystemUtils.getCache(MainActivity.this,new Cache.CallBack() {
                    @Override
                    public boolean run(Cache cache) {
                        cache.setShutDownWiFi(isChecked);
                        return true;
                    }
                });
            }
        });

        // set up showing of reead me
        checkShowReadme = (CheckBox)findViewById(R.id.checkbox_show_show_readme);
        checkShowReadme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                SystemUtils.getCache(MainActivity.this,new Cache.CallBack() {
                    @Override
                    public boolean run(Cache cache) {
                        cache.setShowReadMe(isChecked);
                        return true;
                    }
                });
            }
        });

        // set up default values for checkboxes
        SystemUtils.getCache(MainActivity.this,new Cache.CallBack() {
            @Override
            public boolean run(Cache cache) {
                checkBoxShutDownWiFi.setChecked(cache.isShutDownWiFi());
                checkShowReadme.setChecked(cache.isShowReadMe());

                if(cache.isShowReadMe() == false){
                    findViewById(R.id.txt_read_me).setVisibility(View.GONE);
                }
                return false;
            }
        });

        setPebbleStatus(PebbleKit.isWatchConnected(getApplicationContext()));
    }

    public void registerPebbleBroadcasts(){
        try {
            pebbleConnectedReceiver = PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                setPebbleStatus(true);
                }
            });

            pebbleDisconnectedReceiver = PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                setPebbleStatus(false);
                }
            });

            pebbleDataReceiver = PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(HardwareUtils.PEBBLE_APP_UUID) {
                @Override
                public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                    Log.i(getLocalClassName(), "Received value=" + data.getString(0) + " for key: 0");
                    PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
                }
            });

        }catch(Exception e){

        }
    }

    public void unRegisterPebbleBroadcasts(){
        try {
            if (pebbleConnectedReceiver != null) {
                getApplicationContext().unregisterReceiver(pebbleConnectedReceiver);
            }
            if (pebbleDataReceiver != null) {
                getApplicationContext().unregisterReceiver(pebbleDataReceiver);
            }
            if (pebbleDisconnectedReceiver != null) {
                getApplicationContext().unregisterReceiver(pebbleDisconnectedReceiver);
            }
        }catch(Exception e){

        }
    }

    public void setPebbleStatus(boolean isConnected){
        if(isConnected){
            isPebbleConnected = true;
            pebbleStatus.setText(getResources().getString(R.string.txt_pebble_is_connected));
            buttonInstall.setVisibility(View.VISIBLE);
        }else{
            isPebbleConnected = false;
            pebbleStatus.setText(getResources().getString(R.string.txt_pebble_is_not_connected));
            buttonInstall.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_install_pebble_app:
                hardwareUtils.sendAppToWatch();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPebbleStatus(PebbleKit.isWatchConnected(getApplicationContext()));
        registerPebbleBroadcasts();
    }

    @Override
    protected void onPause() {
        unRegisterPebbleBroadcasts();
        super.onPause();
    }
}