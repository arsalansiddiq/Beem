package com.example.arsalansiddiq.beem.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPUpdatesTable;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import retrofit2.Response;

public class NotesActivity extends BaseActivity {

    private EditText edtText_title, edtText_description;
    private Button btn_addNotes;

    private SharedPreferences getCustomRelationMeetingId, loginStatusPreferences;
    private int customRelationMeetingId, getMeetingStartId;
    private AppUtils appUtils;
    private RealmCRUD realmCRUD;
    String notes;
    private BeemPreferences beemPreferences;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        initProgressBar();

        appUtils = new AppUtils(this);
        beemPreferences = new BeemPreferences(NotesActivity.this);
        realmCRUD = new RealmCRUD();

        getCustomRelationMeetingId = getSharedPreferences(Constants.CUSTOM_RELATION_MEETING_ID, MODE_PRIVATE);
        customRelationMeetingId = getCustomRelationMeetingId.getInt(Constants.KEY_CUSTOM_RELATION_MEETING_ID, 0);

        loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
        getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);

        edtText_title = findViewById(R.id.edtText_title);
        edtText_description = findViewById(R.id.edtText_description);
        btn_addNotes = findViewById(R.id.btn_addNotes);


        btn_addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtText_title.getText().toString()) || TextUtils.isEmpty(edtText_description.getText().toString())) {
                    Toast.makeText(NotesActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {

                    notes = edtText_title.getText().toString() +
                            ": " + edtText_description.getText().toString();

                    addNotesToServer();
                }
            }
        });
    }

    void addNotesToServer () {
        NetworkUtils networkUtils = new NetworkUtils(NotesActivity.this);

        StartMeetingRequest startMeetingRequest = null;

        if (networkUtils.isNetworkConnected()) {

            progressShow();

            final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
            final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);

            final SharedPreferences pictureNotesCount = getSharedPreferences(Constants.NOTES_COUNT, MODE_PRIVATE);
            count = pictureNotesCount.getInt(Constants.KEY_NOTES_COUNT, 0);

            startMeetingRequest = new StartMeetingRequest(getMeetingStartId, null, notes);

            networkUtils.updateMeeting(startMeetingRequest, new MeetingCallBack() {
                @Override
                public void success(Response<MeetingResponseModel> responseModelResponse) {
                    progressHide();
                    if (responseModelResponse.isSuccessful()) {

                        insertNotesUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                notes, 0, 1, 1);

                        beemPreferences.initialize_and_createPreferences_notesCount(count++);

                        Log.i("Update Image in Meeting", "Success");
                    } else {

                        beemPreferences.initialize_and_createPreferences_notesCount(count++);
                        insertNotesUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                notes, 0, 1, 0);

                    }
                    finish();
                }

                @Override
                public void error(String error) {

                    beemPreferences.initialize_and_createPreferences_notesCount(count++);
                    insertNotesUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                            notes, 0, 1, 0);

                    progressHide();
                    Toast.makeText(NotesActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NotesActivity.this);
            alertBuilder.setTitle("Network")
                    .setMessage("Please Check your internet connection")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            addNotesToServer();
                            beemPreferences.initialize_and_createPreferences_notesCount(count++);
                            insertNotesUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                    notes, 0, 1, 0);
                            finish();
                        }
                    });

            alertBuilder.show();
        }
    }

    void insertNotesUpdateMeetingSUP(int customRelationMeetingId, int startMeetingResponseId, String notes, int isImage, int isNote, int syncStatus) {

        MeetingSUPUpdatesTable meetingSUPUpdatesTable = new MeetingSUPUpdatesTable(customRelationMeetingId,startMeetingResponseId, notes,
                isImage, isNote, syncStatus);

        realmCRUD.insertImageUpdateMeetingSUPOffline(meetingSUPUpdatesTable);

    }

}
