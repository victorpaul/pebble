package com.sukinsan.pebble.activity;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sukinsan.pebble.R;
import com.sukinsan.pebble.adapter.LogAdapter;
import com.sukinsan.pebble.application.PebbleApplication;
import com.sukinsan.pebble.entity.HardwareLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private CursorLoader cursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        /*
        List<HardwareLog> hwls = PebbleApplication.dbHandler.getQM().querySelectFrom("ORDER BY `id` DESC LIMIT 600", HardwareLog.class);
        ListView logsView = (ListView)findViewById(R.id.listview_logs);
        logsView.setAdapter(new LogAdapter(this,hwls));//*/
        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        cursorLoader = new CursorLoader(this, Uri.parse("content://com.sukinsan.log.provider/cte"), null, null, null, "id DESC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<HardwareLog> hwls = new ArrayList<HardwareLog>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                hwls.add(PebbleApplication.dbHandler.getQM().getSchemaManager().createEntityFromCursor(cursor,HardwareLog.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }

        ListView logsView = (ListView)findViewById(R.id.listview_logs);
        logsView.setAdapter(new LogAdapter(this,hwls));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
