package com.sukinsan.pebble.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sukinsan.pebble.R;
import com.sukinsan.pebble.adapter.LogAdapter;
import com.sukinsan.pebble.entity.HardwareLog;

import java.util.List;

import anDB.DBHandler;

public class LogActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<HardwareLog> hwls = dbHandler.select("SELECT * FROM `hardwareLog` ORDER BY `id` DESC", HardwareLog.class);

        ListView logsView = (ListView)findViewById(R.id.listview_logs);
        logsView.setAdapter(new LogAdapter(this,hwls));

    }



}
