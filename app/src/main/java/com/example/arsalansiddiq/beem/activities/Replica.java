package com.example.arsalansiddiq.beem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.services.TrackingService;

public class Replica extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replica);

        startService(new Intent(Replica.this, TrackingService.class));

    }
}