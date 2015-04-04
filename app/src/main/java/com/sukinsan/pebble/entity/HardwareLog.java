package com.sukinsan.pebble.entity;

import com.sukinsan.anDB.anDB.abstracts.BaseEntity;
import com.sukinsan.anDB.anDB.annotations.Column;
import com.sukinsan.anDB.anDB.annotations.Table;

import java.util.Date;


/**
 * Created by victor on 30.03.15.
 */
@Table(name = "hardwareLog")
public class HardwareLog extends BaseEntity{
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

    public HardwareLog(String description,String date) {
        this.description = description;
        this.date = date;
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
    public String toString() {
        return "HardwareLog{" +

                "description='" + description + '\'' +
                ", date=" + getDate() +
                '}';
    }

    @Override
    public void beforeDelete(BaseEntity baseEntity) {

    }
}
