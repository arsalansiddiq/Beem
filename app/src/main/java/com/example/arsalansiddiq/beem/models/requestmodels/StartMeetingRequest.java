package com.example.arsalansiddiq.beem.models.requestmodels;

import java.io.File;

/**
 * Created by jellani on 9/21/2018.
 */

public class StartMeetingRequest {

    private int task_id;
    private String dateTime;
    private File file;
    private String notes;

    public StartMeetingRequest(String dateTime, File file) {
        this.dateTime = dateTime;
        this.file = file;
    }

    public StartMeetingRequest(int task_id, String dateTime, File file) {
        this.task_id = task_id;
        this.dateTime = dateTime;
        this.file = file;
    }

    public StartMeetingRequest(int task_id, File file, String notes) {
        this.task_id = task_id;
        this.file = file;
        this.notes = notes;
    }

    public int getTask_id() {
        return task_id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public File getFile() {
        return file;
    }

    public String getNotes() {
        return notes;
    }
}
