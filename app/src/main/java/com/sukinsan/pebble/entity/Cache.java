package com.sukinsan.pebble.entity;

import com.sukinsan.pebble.utils.HardwareUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by victorpaul on 2/2/15.
 */
public class Cache {
    public interface CallBack{
        public void run(Cache cache);
    }

    private List<Integer> lastBatteryInfo = new ArrayList<Integer>();
    private int lastNetwork = HardwareUtils.KEY_NETWORK_OFF;
    private Weather weather = null;

    private boolean shutDownWiFi = false;
    private boolean showReadMe = false;
    private long lastCronJob = 0;

    public Cache() {}

    public List<Integer> getLastBatteryInfo() {
        return lastBatteryInfo;
    }

    public void setLastBatteryInfo(List<Integer> lastBatteryInfo) {
        this.lastBatteryInfo = lastBatteryInfo;
    }

    public int getLastNetwork() {
        return lastNetwork;
    }

    public void setLastNetwork(int lastNetwork) {
        this.lastNetwork = lastNetwork;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public boolean isShutDownWiFi() {
        return shutDownWiFi;
    }

    public void setShutDownWiFi(boolean shutDownWiFi) {
        this.shutDownWiFi = shutDownWiFi;
    }

    public long getLastCronJob() {
        return lastCronJob;
    }

    public void setLastCronJob(long lastCronJob) {
        this.lastCronJob = lastCronJob;
    }

    public boolean isShowReadMe() {
        return showReadMe;
    }

    public void setShowReadMe(boolean showReadMe) {
        this.showReadMe = showReadMe;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "lastBatteryInfo=" + lastBatteryInfo +
                ", lastNetwork=" + lastNetwork +
                ", weather=" + weather +
                ", shutDownWiFi=" + shutDownWiFi +
                ", showReadMe=" + showReadMe +
                ", lastCronJob=" + lastCronJob +
                '}';
    }
}