package com.example.arsalansiddiq.beem.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by jellani on 9/23/2018.
 */

public class TrackingService extends Service {

    private final String LOG_TAG = TrackingService.class.getName();
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;

    private class LocationListener implements android.location.LocationListener {

        Location mLastlocation;

        public LocationListener(String provider) {
            Log.i(LOG_TAG, "Location Listener " + provider);
            this.mLastlocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(LOG_TAG, "onLocationChanges " + location);
            mLastlocation.set(location);
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e(LOG_TAG, "onStatusChanged: " + s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.e(LOG_TAG, "onProviderEnabled: " + s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.e(LOG_TAG, "onProviderDisabled: " + s);
        }
    }

    LocationListener[] mLocationListener = new LocationListener[]{
            new LocationListener(LocationManager.NETWORK_PROVIDER),
//            new LocationListener(LocationManager.GPS_PROVIDER)
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        Log.e(LOG_TAG, "onCreate");
        initializeLocationManager();

        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListener[0]);
            }
        } catch (SecurityException ex) {
            Log.i(LOG_TAG, "fail to request location update, ignore" +  ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_TAG, "network provider does not exist, " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG , "onDestroyService");
        super.onDestroy();
        if (locationManager != null) {
            for (int i = 0; i < mLocationListener.length; i++) {
                try {
                    locationManager.removeUpdates(mLocationListener[i]);
                } catch (Exception ex) {
                    Log.i(LOG_TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    public void initializeLocationManager() {
        Log.e(LOG_TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
