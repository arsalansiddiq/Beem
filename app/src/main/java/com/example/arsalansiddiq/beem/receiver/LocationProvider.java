package com.example.arsalansiddiq.beem.receiver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.activities.LoginActivity;
import com.example.arsalansiddiq.beem.activities.NavigationDrawerActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by jellani on 10/13/2018.
 */

public class LocationProvider extends LocationCallback {

    private final String LOG_TAG =LocationProvider.class.getName();
    private Context context;

    private double latitude, longitude;
    //Location Api
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    public LocationProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult == null) {
            Log.i(LOG_TAG, "Failed on Callback!");
        } else {
            Location location = locationResult.getLastLocation();
                latitude = 0;
                longitude = 0;
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                if (context.getClass().getSimpleName().equals("NavigationDrawerActivity")) {
                    ((NavigationDrawerActivity) context).endTracking(latitude, longitude);
                } else if (context.getClass().getSimpleName().equals("LoginActivity")) {
                    ((LoginActivity) context).startTracking(latitude, longitude);
                }

                Log.i(LOG_TAG, "LocCoorFrom Class  " + latitude + " " + longitude);

        }
    }

    public void initializeLocationManager() {
        Log.e(LOG_TAG, "initializeLocationManager");
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            locationRequest = new LocationRequest();
//            locationRequest.setSmallestDisplacement(100);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(context);
            com.google.android.gms.tasks.Task<LocationSettingsResponse> task = settingsClient
                    .checkLocationSettings(builder.build());

            task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i(LOG_TAG, "SettingsSatisfied!");
                    startLocationUpdates();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(LOG_TAG, "SettingsFailed!");
                    fusedLocationProviderClient = null;
                    if (context.getClass().getSimpleName().equals("NavigationDrawerActivity")) {
                        Toast.makeText(context, "Please check your location permission!", Toast.LENGTH_SHORT).show();
                    } else if (context.getClass().getSimpleName().equals("LoginActivity")) {
                        ((LoginActivity)context).alertLocation();
                    }
                }
            });
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please check Lcoation Permission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Fetching Location!", Toast.LENGTH_SHORT).show();
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, this, null);
        }
    }

    public void removeUpdateLocation() {
        fusedLocationProviderClient.removeLocationUpdates(this);
    }
}
