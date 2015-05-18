package com.sukinsan.pebble.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sukinsan.pebble.R;

/**
 * Created by victorpaul on 18/05/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private TextView pebbleStatus;
    private View buttonInstall;
    private CheckBox checkBoxShutDownWiFi;
    private CheckBox checkShowReadme;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mainActivity = getActivity();
        pebbleStatus = (TextView)mainActivity.findViewById(R.id.txt_pebble_status);
        buttonInstall = mainActivity.findViewById(R.id.btn_install_pebble_app);
        checkBoxShutDownWiFi = (CheckBox)mainActivity.findViewById(R.id.checkbox_shutdown_wifi);
        checkShowReadme = (CheckBox)mainActivity.findViewById(R.id.checkbox_show_show_readme);

    }

    public void testPreconditions() {
        assertNotNull("mainActivity is null", mainActivity);
        assertNotNull("pebbleStatus is null", pebbleStatus);
        assertNotNull("buttonInstall is null", buttonInstall);
        assertNotNull("checkBoxShutDownWiFi is null", checkBoxShutDownWiFi);
        assertNotNull("checkShowReadme is null", checkShowReadme);
    }
}
