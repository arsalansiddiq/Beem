package com.example.arsalansiddiq.beem.utils;

import android.app.Activity;
import android.view.View;

import com.example.arsalansiddiq.beem.R;

/**
 * Created by jellani on 9/16/2018.
 */

public class FabViewVisibility {

    private Activity activity;
    private com.github.clans.fab.FloatingActionButton fab_menu_markAttendance_supervisor, fab_menu_endAttendance_supervisor,
            fab_menu_addShop_supervisor, fab_menu_startMeeting_supervisor, fab_menu_endMeeting_supervisor,
            fab_menu_takePictures_supervisor, fab_menu_addNotes_supervisor;

    public FabViewVisibility(Activity activity) {
        this.activity = activity;

        fab_menu_markAttendance_supervisor = activity.findViewById(R.id.fab_menu_markAttendance_supervisor);
        fab_menu_endAttendance_supervisor = activity.findViewById(R.id.fab_menu_endAttendance_supervisor);
        fab_menu_addShop_supervisor = activity.findViewById(R.id.fab_menu_addShop_supervisor);
        fab_menu_startMeeting_supervisor = activity.findViewById(R.id.fab_menu_startMeeting_supervisor);
        fab_menu_endMeeting_supervisor = activity.findViewById(R.id.fab_menu_endMeeting_supervisor);
        fab_menu_takePictures_supervisor = activity.findViewById(R.id.fab_menu_takePictures_supervisor);
        fab_menu_addNotes_supervisor = activity.findViewById(R.id.fab_menu_addNotes_supervisor);
    }

    public void fabMarkAttendance(boolean status) {
        if (status) {
            fab_menu_markAttendance_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_markAttendance_supervisor.setVisibility(View.GONE);
        }
    }

    public void fabEndAttendance(boolean status) {
        if (status) {
            fab_menu_endAttendance_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_endAttendance_supervisor.setVisibility(View.GONE);
        }
    }

    public void fabStartMeeting(boolean status) {
        if (status) {
            fab_menu_startMeeting_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_startMeeting_supervisor.setVisibility(View.GONE);
        }
    }

    public void fabEndMeeting(boolean status) {
        if (status) {
            fab_menu_endMeeting_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_endMeeting_supervisor.setVisibility(View.GONE);
        }
    }

    public void fabsAfterStartMeeting(boolean status) {
        if (status) {
            fab_menu_takePictures_supervisor.setVisibility(View.VISIBLE);
            fab_menu_addNotes_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_takePictures_supervisor.setVisibility(View.GONE);
            fab_menu_addNotes_supervisor.setVisibility(View.GONE);
        }
    }

    public void fabAfterEndMeeting(boolean status) {
    }

    public void fabMerchant() {

        fab_menu_addShop_supervisor.setVisibility(View.GONE);
        fab_menu_endMeeting_supervisor.setVisibility(View.GONE);
        fab_menu_takePictures_supervisor.setVisibility(View.GONE);
        fab_menu_addNotes_supervisor.setVisibility(View.GONE);

    }

    public void fabMerchantAttendance(boolean status) {
        if (status) {
            fab_menu_markAttendance_supervisor.setVisibility(View.GONE);
            fab_menu_endAttendance_supervisor.setVisibility(View.VISIBLE);
            fab_menu_startMeeting_supervisor.setVisibility(View.VISIBLE);
        } else {
            fab_menu_markAttendance_supervisor.setVisibility(View.VISIBLE);
            fab_menu_endAttendance_supervisor.setVisibility(View.GONE);
            fab_menu_startMeeting_supervisor.setVisibility(View.GONE);
        }
    }
}
