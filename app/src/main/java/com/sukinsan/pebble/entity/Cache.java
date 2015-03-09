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
    private String lastDateStatus = "";
    private Weather weather = null;

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

    public String getLastDateStatus() {
        return lastDateStatus;
    }

    public void setLastDateStatus(String lastDateStatus) {
        this.lastDateStatus = lastDateStatus;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "lastBatteryInfo=" + lastBatteryInfo +
                ", lastNetwork=" + lastNetwork +
                ", lastDateStatus='" + lastDateStatus + '\'' +
                ", weather=" + weather +
                '}';
    }
}