package com.example.arsalansiddiq.beem.models.databasemodels.meetingsup;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 9/28/2018.
 */

public class MeetingSUPEndTable extends RealmObject{

    @PrimaryKey
    private int id;

    private int customRelationMeetingId;
    private int startMeetingResponseId;
    private String EndTime;
    private byte[] img4;
    private int syncStatus;

    public MeetingSUPEndTable() {
    }

    public MeetingSUPEndTable(int customRelationMeetingId, int startMeetingResponseId, String endTime, byte[] img4, int syncStatus) {
        this.customRelationMeetingId = customRelationMeetingId;
        this.startMeetingResponseId = startMeetingResponseId;
        EndTime = endTime;
        this.img4 = img4;
        this.syncStatus = syncStatus;
    }

    public int getCustomRelationMeetingId() {
        return customRelationMeetingId;
    }

    public void setCustomRelationMeetingId(int customRelationMeetingId) {
        this.customRelationMeetingId = customRelationMeetingId;
    }

    public int getStartMeetingResponseId() {
        return startMeetingResponseId;
    }

    public void setStartMeetingResponseId(int startMeetingResponseId) {
        this.startMeetingResponseId = startMeetingResponseId;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public byte[] getImg4() {
        return img4;
    }

    public void setImg4(byte[] img4) {
        this.img4 = img4;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
