package com.example.arsalansiddiq.beem.models.databasemodels.meetingsup;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 9/28/2018.
 */

public class MeetingSUPUpdatesTable extends RealmObject{

    @PrimaryKey
    private int primaryKey;

    private int customRelationMeetingId;
    private int startMeetingResponseId;
    private byte[] img2;
    private String notes;
    private int isImage;
    private int isNote;
    private int syncStatus;

    public MeetingSUPUpdatesTable() {
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    //Image Update
    public MeetingSUPUpdatesTable(int customRelationMeetingId, int startMeetingResponseId, byte[] img2, int isImage, int isNote, int syncStatus) {
        this.customRelationMeetingId = customRelationMeetingId;
        this.startMeetingResponseId = startMeetingResponseId;
        this.img2 = img2;
        this.isImage = isImage;
        this.isNote = isNote;
        this.syncStatus = syncStatus;
    }

    //Notes Update
    public MeetingSUPUpdatesTable(int customRelationMeetingId, int startMeetingResponseId, String notes, int isImage, int isNote, int syncStatus) {
        this.customRelationMeetingId = customRelationMeetingId;
        this.startMeetingResponseId = startMeetingResponseId;
        this.notes = notes;
        this.isImage = isImage;
        this.isNote = isNote;
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

    public byte[] getImg2() {
        return img2;
    }

    public void setImg2(byte[] img2) {
        this.img2 = img2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int isImage) {
        this.isImage = isImage;
    }

    public int getIsNote() {
        return isNote;
    }

    public void setIsNote(int isNote) {
        this.isNote = isNote;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }
}
