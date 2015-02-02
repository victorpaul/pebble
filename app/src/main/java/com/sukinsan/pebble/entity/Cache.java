package com.sukinsan.pebble.entity;

/**
 * Created by victorpaul on 2/2/15.
 */
public class Cache {
    public interface CallBack{
        public void run(Cache cache);
    }

    private String lastBatteryLevel = "";
    private String lastNetwork = "";
    private String lastWeatherStatus = "";
    private String lastDateStatus = "";
    private long lastDataSyncDate = 0;

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

    public String getLastWeatherStatus() {
        return lastWeatherStatus;
    }

    public void setLastWeatherStatus(String lastWeatherStatus) {
        this.lastWeatherStatus = lastWeatherStatus;
    }

    public String getLastDateStatus() {
        return lastDateStatus;
    }

    public void setLastDateStatus(String lastDateStatus) {
        this.lastDateStatus = lastDateStatus;
    }

    public long getLastDataSyncDate() {
        return lastDataSyncDate;
    }

    public void setLastDataSyncDate(long lastDataSyncDate) {
        this.lastDataSyncDate = lastDataSyncDate;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "lastBatteryLevel='" + lastBatteryLevel + '\'' +
                ", lastNetwork='" + lastNetwork + '\'' +
                ", lastWeatherStatus='" + lastWeatherStatus + '\'' +
                ", lastDateStatus='" + lastDateStatus + '\'' +
                ", lastDataSyncDate=" + lastDataSyncDate +
                '}';
    }
}