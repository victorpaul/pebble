package com.sukinsan.pebble.entity;

import java.util.Date;

import anDB.abstracts.BaseTable;
import anDB.annotations.Column;
import anDB.annotations.Table;

/**
 * Created by victor on 30.03.15.
 */
@Table(name = "hardwareLog")
public class HardwareLog extends BaseTable{
    @Column(name = "description",type = "TEXT")
    private String description;

    @Column(name = "date",type = "TEXT")
    private String date;

    public HardwareLog() {
    }

    public HardwareLog(String description) {
        this.description = description;
        this.date = System.currentTimeMillis()+"";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        if(date != null) {
            return new Date(Long.parseLong(date));
        }
        return null;
    }

    @Override
    public void beforeDelete(BaseTable baseTable) {

    }

    @Override
    public String toString() {
        return "HardwareLog{" +
                "id='" + id + '\'' +
                "description='" + description + '\'' +
                ", date=" + getDate() +
                '}';
    }
}
