package com.sukinsan.pebble.application;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * Created by victorpaul on 14/1/15.
 */
public class PebbleApplication extends Application {
    private static final String PROPERTY_ID = "UA-28082070-2";
    private static final String TAG = PebbleApplication.class.getSimpleName();
    private Tracker tracker = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(PROPERTY_ID);
        }
        return tracker;
    }

    public void eventCLickInstallUpdate(){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("install/update").setAction("click").setLabel("Open dialog to install/update app on watch").build());
    }

    public void eventCLickInstallUpdateFailed(String error){
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("install/update")
                .setAction("click failed")
                .setLabel("Failed to open dialog to install/update app on watch")
                .set("error", error)
                .build());
    }

    public void eventCLickShare(){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("share").setAction("click").setLabel("Click share").build());
    }

    public void eventCLickMenuFeedback(){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("feedback").setAction("click").setLabel("Open feedback dialog").build());
    }

    public void eventSentFeedback(){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("feedback").setAction("sent").setLabel("Feedback has been sent").build());
    }

    public void eventSentFeedbackFailed(String error){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("feedback").setAction("sending failed").setLabel("Feedback has been failed").set("error", error).build());
    }

    public void eventOpenApp(){
        getTracker().send(new HitBuilders.EventBuilder().setCategory("app").setAction("open").setLabel("Application was open").build());
        Log.i(TAG, "eventOpenApp()");
    }

    public void eventSwitchWiFi(boolean switcher){
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("settings")
                .setAction("wifi")
                .setLabel(switcher?"Enable wifi saver":"Disable wifi saver")
                .build());
        Log.i(TAG, "eventOpenApp()");
    }

    public void eventSwitchReadMe(boolean switcher){
        getTracker().send(new HitBuilders.EventBuilder()
            .setCategory("settings")
            .setAction("readme")
            .setLabel(switcher ? "Enable readme" : "Disable readme")
            .build());
        Log.i(TAG, "eventOpenApp()");
    }

}