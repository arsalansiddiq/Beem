package com.example.arsalansiddiq.beem.models.databasemodels.meetingsup;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 9/28/2018.
 */

public class MeetingSUPStartTable extends RealmObject{


    @PrimaryKey
    private int id;

    private int task_id;
    private String StartTime;
    private byte[] img1;
    private double lat;
    private double lng;
    private int emp_id;
    private int startMeetingResponseId;
    private int syncStatus;

    public MeetingSUPStartTable() {
    }

    public MeetingSUPStartTable(int task_id, String startTime, byte[] img1, double lat, double lng, int emp_id,
                                int startMeetingResponseId, int syncStatus) {
        this.task_id = task_id;
        StartTime = startTime;
        this.img1 = img1;
        this.lat = lat;
        this.lng = lng;
        this.emp_id = emp_id;
        this.startMeetingResponseId = startMeetingResponseId;
        this.syncStatus = syncStatus;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public byte[] getImg1() {
        return img1;
    }

    public void setImg1(byte[] img1) {
        this.img1 = img1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getStartMeetingResponseId() {
        return startMeetingResponseId;
    }

    public void setStartMeetingResponseId(int startMeetingResponseId) {
        this.startMeetingResponseId = startMeetingResponseId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }
}
