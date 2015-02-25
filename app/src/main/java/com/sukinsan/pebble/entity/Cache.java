package com.sukinsan.pebble.entity;

import org.json.JSONException;

/**
 * Created by victorpaul on 2/2/15.
 */
public class Cache {
    public interface CallBack{
        public void run(Cache cache);
    }

    private String lastBatteryLevel = "";
    private String lastNetwork = "";
    private String lastDateStatus = "";

    private Weather weather;

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Cache() {}

    public String getLastBatteryLevel() {
        return lastBatteryLevel;
    }

    public void setLastBatteryLevel(String lastBatteryLevel) {
        this.lastBatteryLevel = lastBatteryLevel;
    }

    public String getLastNetwork() {
        return lastNetwork;
    }

    public void setLastNetwork(String lastNetwork) {
        this.lastNetwork = lastNetwork;
    }

    public String getLastDateStatus() {
        return lastDateStatus;
    }

    public void setLastDateStatus(String lastDateStatus) {
        this.lastDateStatus = lastDateStatus;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "lastBatteryLevel='" + lastBatteryLevel + '\'' +
                ", lastNetwork='" + lastNetwork + '\'' +
                ", lastDateStatus='" + lastDateStatus + '\'' +
                ", weather=" + weather +
                '}';
    }
}