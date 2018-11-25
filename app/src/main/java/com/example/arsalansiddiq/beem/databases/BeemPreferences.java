package com.example.arsalansiddiq.beem.databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arsalansiddiq.beem.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

public class BeemPreferences {

    private Context context;
    SharedPreferences.Editor sharedPreEditor;
    public static final String BA_ATTENDANCE_ID = "id";

    public BeemPreferences(Context context) {
        this.context = context;
    }

    public void initialize_and_createPreferences (int id) {
        sharedPreEditor = context.getSharedPreferences(BA_ATTENDANCE_ID, MODE_PRIVATE).edit();
        sharedPreEditor.putInt("baAttendanceID", id);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_forStatus (int status) {
        sharedPreEditor = context.getSharedPreferences(Constants.SALE_STATUS, MODE_PRIVATE).edit();
        status += status;
        sharedPreEditor.putInt(Constants.KEY_SALE_STATUS, status);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_forAttendanceStatus (int attendanceStatus) {
        sharedPreEditor = context.getSharedPreferences(Constants.ATTENDANCE_STATUS, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_ATTENDANCE_STATUS, attendanceStatus);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_forLoginSession (int loginStatus) {
        sharedPreEditor = context.getSharedPreferences(Constants.LOGIN_STATUS, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_LOGIN_STATUS, loginStatus);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_forBrand (String brand) {
        sharedPreEditor = context.getSharedPreferences(Constants.USER_BRAND, MODE_PRIVATE).edit();
        sharedPreEditor.putString(Constants.KEY_USER_BRAND, brand);
        sharedPreEditor.apply();
    }


    public void initialize_and_createPreferences_loginUserDesignation (boolean isSupervisor) {
        sharedPreEditor = context.getSharedPreferences(Constants.LOGIN_USER_DESIGNATION, MODE_PRIVATE).edit();
        sharedPreEditor.putBoolean(Constants.KEY_LOGIN_USER_DESIGNATION, isSupervisor);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_meetingStatusSupervisor (boolean isSupervisorInMeeting) {
        sharedPreEditor = context.getSharedPreferences(Constants.SUPERVISOR_MEETING_STATUS, MODE_PRIVATE).edit();
        sharedPreEditor.putBoolean(Constants.KEY_SUPERVISOR_MEETING_STATUS, isSupervisorInMeeting);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_startMeetingSupervisorID (int startMeetingId) {
        sharedPreEditor = context.getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_MEETING_START_ID, startMeetingId);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_customRelationMeetingId (int customRelationMeetingId) {
        sharedPreEditor = context.getSharedPreferences(Constants.CUSTOM_RELATION_MEETING_ID, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_CUSTOM_RELATION_MEETING_ID, customRelationMeetingId);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_breakId(int startBreakId) {
        sharedPreEditor = context.getSharedPreferences(Constants.BREAK_START_ID, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_BREAK_START_ID, startBreakId);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_breakStatus(int customRelationMeetingId) {
        sharedPreEditor = context.getSharedPreferences(Constants.BREAK_STATUS, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_BREAK_STATUS, customRelationMeetingId);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_breakType(String breakType) {
        sharedPreEditor = context.getSharedPreferences(Constants.BREAK_TYPE, MODE_PRIVATE).edit();
        sharedPreEditor.putString(Constants.KEY_BREAK_TYPE, breakType);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_breakTime(String breakTime) {
        sharedPreEditor = context.getSharedPreferences(Constants.BREAK_TIME, MODE_PRIVATE).edit();
        sharedPreEditor.putString(Constants.KEY_BREAK_TIME, breakTime);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_notesCount(int notesCount) {
        sharedPreEditor = context.getSharedPreferences(Constants.NOTES_COUNT, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_NOTES_COUNT, notesCount);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_picturesCount(int picturesCount) {
        sharedPreEditor = context.getSharedPreferences(Constants.PICTURE_COUNT, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_PICTURE_COUNT, picturesCount);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_trackingStatus(int trackingStatus) {
        sharedPreEditor = context.getSharedPreferences(Constants.TRACKING_STATUS, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_TRACKING_STATUS, trackingStatus);
        sharedPreEditor.apply();
    }

    public void initialize_and_createPreferences_trackingStartId(int trackingStartId) {
        sharedPreEditor = context.getSharedPreferences(Constants.TRACKING_START_ID, MODE_PRIVATE).edit();
        sharedPreEditor.putInt(Constants.KEY_TRACKING_START_ID, trackingStartId);
        sharedPreEditor.apply();
    }
}
