package com.example.arsalansiddiq.beem.models.databasemodels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 9/7/2018.
 */

public class EndMarkAttendanceTable extends RealmObject {

    @PrimaryKey
    private int id;
    private int markAndEndAttendanceRelationId;
    private String getDate;
    private int meetingId;
    private String endTime;
    private float eLatitude;
    private float eLongitude;
    private byte[] endImage;
    private int endMarkAttedanceResponseStatus;
    private int endMarkAttendanceResponseId;
    private int syncStatus;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMarkAndEndAttendanceRelationId() {
        return markAndEndAttendanceRelationId;
    }

    public void setMarkAndEndAttendanceRelationId(int markAndEndAttendanceRelationId) {
        this.markAndEndAttendanceRelationId = markAndEndAttendanceRelationId;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float geteLatitude() {
        return eLatitude;
    }

    public void seteLatitude(float eLatitude) {
        this.eLatitude = eLatitude;
    }

    public float geteLongitude() {
        return eLongitude;
    }

    public void seteLongitude(float eLongitude) {
        this.eLongitude = eLongitude;
    }

    public byte[] getEndImage() {
        return endImage;
    }

    public void setEndImage(byte[] endImage) {
        this.endImage = endImage;
    }

    public int getEndMarkAttedanceResponseStatus() {
        return endMarkAttedanceResponseStatus;
    }

    public void setEndMarkAttedanceResponseStatus(int endMarkAttedanceResponseStatus) {
        this.endMarkAttedanceResponseStatus = endMarkAttedanceResponseStatus;
    }

    public int getEndMarkAttendanceResponseId() {
        return endMarkAttendanceResponseId;
    }

    public void setEndMarkAttendanceResponseId(int endMarkAttendanceResponseId) {
        this.endMarkAttendanceResponseId = endMarkAttendanceResponseId;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
