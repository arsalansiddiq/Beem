package com.example.arsalansiddiq.beem.models.databasemodels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jellani on 10/13/2018.
 */

public class UpdateTrackingModel extends RealmObject {

    @PrimaryKey
    private int customPrivateKey;
    private int emp_track_id;
    private double latitude;
    private double longitude;
    private int syncStatus;

    public UpdateTrackingModel() {
    }

    public UpdateTrackingModel(int emp_track_id, double latitude, double longitude, int syncStatus) {
        this.emp_track_id = emp_track_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.syncStatus = syncStatus;
    }

    public int getEmp_track_id() {
        return emp_track_id;
    }

    public void setEmp_track_id(int emp_track_id) {
        this.emp_track_id = emp_track_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getCustomPrivateKey() {
        return customPrivateKey;
    }

    public void setCustomPrivateKey(int customPrivateKey) {
        this.customPrivateKey = customPrivateKey;
    }
}
