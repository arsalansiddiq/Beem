package com.example.arsalansiddiq.beem.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import retrofit2.Response;

public class NotesActivity extends BaseActivity {

    private EditText edtText_title, edtText_description;
    private Button btn_addNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        initProgressBar();

        edtText_title = findViewById(R.id.edtText_title);
        edtText_description = findViewById(R.id.edtText_description);
        btn_addNotes = findViewById(R.id.btn_addNotes);


        btn_addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtText_title.getText().toString()) || TextUtils.isEmpty(edtText_description.getText().toString())) {
                    Toast.makeText(NotesActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    addNotesToServer();
                }
            }
        });
    }

    void addNotesToServer () {
        NetworkUtils networkUtils = new NetworkUtils(NotesActivity.this);
        if (networkUtils.isNetworkConnected()) {

            progressShow();

            final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
            final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);

            StartMeetingRequest startMeetingRequest = new StartMeetingRequest(getMeetingStartId, null, edtText_title.getText().toString() +
            ": " + edtText_description.getText().toString());
            networkUtils.updateMeeting(startMeetingRequest, new MeetingCallBack() {
                @Override
                public void success(Response<MeetingResponseModel> responseModelResponse) {
                    progressHide();
                    if (responseModelResponse.isSuccessful()) {
                        finish();
                    }
                }

                @Override
                public void error(String error) {
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
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addNotesToServer();
                        }
                    });

            alertBuilder.show();
        }
    }

}
