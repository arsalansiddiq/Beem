package com.example.arsalansiddiq.beem.models.databasemodels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 9/1/2018.
 */

public class MarkAttendance extends RealmObject{

    @PrimaryKey
    private int id;
    private String date;
    private int empid;
    private String name;
    private byte[] StartImage;
    private String startTime;
    private float latitude;
    private float longitude;
    private int status;
    private int markAttendanceResponseID;
    private int syncStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getStartImage() {
        return StartImage;
    }

    public void setStartImage(byte[] startImage) {
        StartImage = startImage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMarkAttendanceResponseID() {
        return markAttendanceResponseID;
    }

    public void setMarkAttendanceResponseID(int markAttendanceResponseID) {
        this.markAttendanceResponseID = markAttendanceResponseID;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
