package com.sukinsan.pebble.entity;

/**
 * Created by victorpaul on 25/2/15.
 */
public class Weather {
    private final static String IMG_ENDPOINT = "http://openweathermap.org/img/w/%s.png";

    private double tempDay;
    private double tempMin;
    private double tempMax;
    private double tempNight;
    private double tempEvening;
    private double tempMorning;

    private String country;
    private String city;
    private String status;
    private String description;
    private String ico;

    private double lon;
    private double lat;

    private long lastUpdate;

    public Weather() {}

    public double getTempDay() {
        return tempDay;
    }

    public void setTempDay(double tempDay) {
        this.tempDay = tempDay;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempNight() {
        return tempNight;
    }

    public void setTempNight(double tempNight) {
        this.tempNight = tempNight;
    }

    public double getTempEvening() {
        return tempEvening;
    }

    public void setTempEvening(double tempEvening) {
        this.tempEvening = tempEvening;
    }

    public double getTempMorning() {
        return tempMorning;
    }

    public void setTempMorning(double tempMorning) {
        this.tempMorning = tempMorning;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIco() {
        return ico;
    }

    public String getIcoURL(){
        return String.format(IMG_ENDPOINT,this.ico);
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "tempDay=" + tempDay +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", tempNight=" + tempNight +
                ", tempEvening=" + tempEvening +
                ", tempMorning=" + tempMorning +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", ico='" + ico + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}