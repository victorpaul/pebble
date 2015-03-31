package com.sukinsan.pebble.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rightutils.rightutils.widgets.TypefaceTextView;
import com.sukinsan.pebble.R;
import com.sukinsan.pebble.entity.HardwareLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 31.03.15.
 */
public class LogAdapter extends ArrayAdapter<HardwareLog> {
    private Context context;
    private LayoutInflater inflater;

    public LogAdapter(Context context, List<HardwareLog> objects) {
        super(context, R.layout.adapter_log, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_log,null);
        }
        HardwareLog hwLog = getItem(position);

        ((TypefaceTextView)convertView.findViewById(R.id.txt_log)).setText(hwLog.getDescription());
        ((TypefaceTextView)convertView.findViewById(R.id.txt_date)).setText(hwLog.getDate().toString());

        return convertView;
    }
}
