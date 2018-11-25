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
    private double lat;
    private double lng;
    private int emp_id;

    public StartMeetingRequest(String dateTime, File file) {
        this.dateTime = dateTime;
        this.file = file;
    }

    public StartMeetingRequest(String dateTime, File file, double lat, double lng, int emp_id) {
        this.dateTime = dateTime;
        this.file = file;
        this.lat = lat;
        this.lng = lng;
        this.emp_id = emp_id;
    }

    public StartMeetingRequest(int task_id, String dateTime, File file, double lat, double lng, int emp_id) {
        this.task_id = task_id;
        this.dateTime = dateTime;
        this.file = file;
        this.lat = lat;
        this.lng = lng;
        this.emp_id = emp_id;
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

    public StartMeetingRequest(int task_id, String notes) {
        this.task_id = task_id;
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

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
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
}
