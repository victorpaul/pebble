package com.sukinsan.pebble.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sukinsan.pebble.R;
import com.sukinsan.pebble.broadcast.BootReceiver;
import com.sukinsan.pebble.utils.HardwareUtils;

import java.util.UUID;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public final static String TAG = MainActivity.class.getSimpleName();

    private boolean isPebbleConnected = false;

    private View statusConnected;
    private View statusDisconnected;
    private View btnSend;
    private EditText userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        //startActivity(browserIntent);

        HardwareUtils.runCron(getApplicationContext());

        statusConnected = findViewById(R.id.txt_pebble_is_connected);
        statusDisconnected = findViewById(R.id.txt_pebble_is_not_connected);
        btnSend = findViewById(R.id.btn_send_msg);
        userMessage = (EditText)findViewById(R.id.f_message);

        btnSend.setOnClickListener(this);

        setPebbleStatus(PebbleKit.isWatchConnected(getApplicationContext()));
        registerPebbleBroadcasts();
    }

    public void registerPebbleBroadcasts(){
        try {
            PebbleKit.registerPebbleConnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                setPebbleStatus(true);
                }
            });

            PebbleKit.registerPebbleDisconnectedReceiver(getApplicationContext(), new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                setPebbleStatus(false);
                }
            });

            PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(HardwareUtils.PEBBLE_APP_UUID) {
                @Override
                public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                    Log.i(getLocalClassName(), "Received value=" + data.getString(0) + " for key: 0");
                    PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
                }
            });
        }catch(Exception e){

        }
    }

    public void setPebbleStatus(boolean isConnected){
        if(isConnected){
            isPebbleConnected = true;
            statusConnected.setVisibility(View.VISIBLE);
            statusDisconnected.setVisibility(View.GONE);
        }else{
            isPebbleConnected = false;
            statusConnected.setVisibility(View.GONE);
            statusDisconnected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_msg:
                String message = userMessage.getText().toString().trim();
                if(!isPebbleConnected){
                    Toast.makeText(getApplicationContext(),"Pebble is not connected",Toast.LENGTH_LONG).show();
                    return;
                }

                if(message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Message is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                HardwareUtils.sendUpdateToPebble(this,message);

                userMessage.setText("");
                Toast.makeText(getApplicationContext(), "Message has been sent", Toast.LENGTH_LONG).show();
                break;

        }
    }
}