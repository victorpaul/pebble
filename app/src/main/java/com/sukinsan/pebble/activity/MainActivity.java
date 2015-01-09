package com.sukinsan.pebble.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.sukinsan.pebble.utils.HardwareUtils;

import java.util.UUID;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public final static String TAG = MainActivity.class.getSimpleName();

    public final static int KEY_DATE = 1;
    public final static int KEY_NETWORK = 2;
    public final static int KEY_BATTERY = 3;
    public final static int KEY_WEATHER = 4;
    public final static int KEY_DATA = 5;

    private final static UUID PEBBLE_APP_UUID = UUID.fromString("7b7c495e-1c45-48b6-85f9-7568adf74ec6");
    private boolean isPebbleConnected = false;

    private View statusConnected;
    private View statusDisconnected;
    private View btnSend;
    private EditText userMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusConnected = findViewById(R.id.txt_pebble_is_connected);
        statusDisconnected = findViewById(R.id.txt_pebble_is_not_connected);
        btnSend = findViewById(R.id.btn_send_msg);
        userMessage = (EditText)findViewById(R.id.f_message);

        btnSend.setOnClickListener(this);

        setPebbleStatus(PebbleKit.isWatchConnected(getApplicationContext()));
        registerPebbleBroadcasts();
    }

    public void registerPebbleBroadcasts(){
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
        PebbleKit.registerReceivedDataHandler(this, new PebbleKit.PebbleDataReceiver(PEBBLE_APP_UUID) {
            @Override
            public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
                Log.i(getLocalClassName(), "Received value=" + data.getString(0) + " for key: 0");
                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);
            }

        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                PebbleDictionary data = new PebbleDictionary();


                data.addString(KEY_DATE,"friday");
                data.addString(KEY_NETWORK,"wifi");
                data.addString(KEY_BATTERY, HardwareUtils.getBatteryStatus(this));
                data.addString(KEY_WEATHER,"cold");
                data.addString(KEY_DATA,message);


                PebbleKit.sendDataToPebble(getApplicationContext(), PEBBLE_APP_UUID, data);

                userMessage.setText("");
                Toast.makeText(getApplicationContext(), "Message has been sent", Toast.LENGTH_LONG).show();
                break;
        }
    }
}