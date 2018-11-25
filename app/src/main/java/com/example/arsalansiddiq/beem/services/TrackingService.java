package com.example.arsalansiddiq.beem.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.models.databasemodels.UpdateTrackingModel;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;
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

import java.util.List;

import retrofit2.Response;

/**
 * Created by jellani on 9/23/2018.
 */

public class TrackingService extends Service implements UpdateCallback{

    private final String LOG_TAG = TrackingService.class.getName();

//    private double latitude, longitude;
    //Location Api
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    
    private List<UpdateTrackingModel> updateTrackingModelList;
    private NetworkUtils networkUtils;
    private RealmCRUD realmCRUD;

    private final static String NOTIFICATION_CHANNEL_ID = "96";
    private final static int NOTIFICATION_ID = 97;
    private NotificationManager mNotificationManager;

    public TrackingService() {
        super();
    }

    @Override
    public void onCreate() {
        Log.e(LOG_TAG, "onCreate");


        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.beemlogo);
        mBuilder.setContentTitle("MyService");
        mBuilder.setContentText("Real time Tracking on!");

        startForeground(NOTIFICATION_ID, mBuilder.build());

        initializeLocationManager();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i(LOG_TAG, "Failed on Callback!");
                } else {
                    for (Location location : locationResult.getLocations()) {
                        double latitude, longitude;
//                        latitude = 0;
//                        longitude = 0;
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        updateTracking(latitude, longitude);
                        Log.i("LocCoorFromSerivceTrack", latitude + " " + longitude);
                    }
                }
            }
        };
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void initializeLocationManager() {
        Log.e(LOG_TAG, "initializeLocationManager");
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setSmallestDisplacement(100);
//            locationRequest.setInterval(10000);
//            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
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
                    notifyUserForLocationPermission();
                }
            });
        }
    }

    private void notifyUserForLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.beemlogo)
                .setContentTitle("Beem Tracking")
                .setContentText("Please turn on user location!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder1.build());
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please check Lcoation Permission", Toast.LENGTH_SHORT).show();
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateTracking(double latitude, double longitude) {

        realmCRUD = new RealmCRUD();

        SharedPreferences preferences = this.getSharedPreferences(Constants.TRACKING_START_ID, MODE_PRIVATE);
        int trackingId = preferences.getInt(Constants.KEY_TRACKING_START_ID, 0);

        networkUtils = new NetworkUtils(this);

        updateTrackingModelList = realmCRUD.getOfflineTrackingCoordinates();
        
        if (updateTrackingModelList != null) {
            updateTrackingOffline();
        }

        if (networkUtils.isNetworkConnected()) {
            networkUtils.updateTracking(trackingId, latitude, longitude, this);
        } else {
            UpdateTrackingModel updateTrackingModel = new UpdateTrackingModel(trackingId, latitude, longitude, 0);
            realmCRUD.insertUpdateTrackingOffline(updateTrackingModel);
        }
    }

    private void updateTrackingOffline() {

        if (networkUtils.isNetworkConnected()) {
            for (int i = 0; i < updateTrackingModelList.size(); i++) {
                networkUtils.updateTracking(updateTrackingModelList.get(i).getEmp_track_id(),
                        updateTrackingModelList.get(i).getLatitude(),
                        updateTrackingModelList.get(i).getLongitude(), this);

                UpdateTrackingModel updateTrackingModel = new UpdateTrackingModel();
                updateTrackingModel.setCustomPrivateKey(updateTrackingModelList.get(i).getCustomPrivateKey());
                updateTrackingModel.setLatitude(updateTrackingModelList.get(i).getLatitude());
                updateTrackingModel.setLongitude(updateTrackingModelList.get(i).getLongitude());

                networkUtils.updateTrackingOffline(updateTrackingModel.getEmp_track_id(),
                        updateTrackingModel.getLatitude(), updateTrackingModel.getLongitude(), new MeetingCallBack() {
                            @Override
                            public void success(Response<MeetingResponseModel> responseModelResponse) {
                                realmCRUD.updateTrackingOfflineSyncStatus(updateTrackingModel.getCustomPrivateKey(), 1);
                            }

                            @Override
                            public void error(String error) {

                            }
                        });
            }
        }

    }

    @Override
    public void UpdateSuccess(Response response) {
        Log.i(LOG_TAG, "suc " + response.body().toString());
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        Log.i(LOG_TAG, "fail" + baseResponse.getError());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public void removeCallback(){
        stopForeground(true);
        stopSelf();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
