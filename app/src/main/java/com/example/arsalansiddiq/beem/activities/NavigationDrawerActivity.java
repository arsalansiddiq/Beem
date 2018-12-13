package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.contractor.NavigationDrawerContractorSUP;
import com.example.arsalansiddiq.beem.activities.presenter.NavigationDrawerSUPPresenter;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapterTasks;
import com.example.arsalansiddiq.beem.adapters.MerchantTaskAdapter;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.BeemPreferencesCount;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.AttandanceInterface;
import com.example.arsalansiddiq.beem.interfaces.EndAttendanceInterface;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.databasemodels.MarkAttendance;
import com.example.arsalansiddiq.beem.models.databasemodels.SalesAndNoSales;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPEndTable;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPStartTable;
import com.example.arsalansiddiq.beem.models.databasemodels.meetingsup.MeetingSUPUpdatesTable;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.requestmodels.merchant.MeetingRequestMerchant;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.ResponseSUP;
import com.example.arsalansiddiq.beem.models.responsemodels.babreak.BreakTypeResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.babreak.Status;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.Datum;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.MerchantTaskResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.Task;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.TaskResponse;
import com.example.arsalansiddiq.beem.receiver.LocationProvider;
import com.example.arsalansiddiq.beem.services.BreakService;
import com.example.arsalansiddiq.beem.services.MyService;
import com.example.arsalansiddiq.beem.services.TrackingService;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.FabViewVisibility;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.SyncDataToServerClass;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;
import com.google.android.gms.common.api.ApiException;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

import static com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED;


public class NavigationDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        NavigationDrawerContractorSUP.NavigationView, AdapterView.OnItemClickListener, UpdateCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    private com.github.clans.fab.FloatingActionMenu fab_menu_ba, fab_menu_supervisor;

    private com.github.clans.fab.FloatingActionButton fab_menu_markAttendance_supervisor, fab_menu_endAttendance_supervisor,
            fab_menu_addShop_supervisor, fab_menu_startMeeting_supervisor, fab_menu_endMeeting_supervisor,
            fab_menu_takePictures_supervisor, fab_menu_addNotes_supervisor;

    private com.github.clans.fab.FloatingActionButton fab_menu_startBreak, fab_menu_endDay, fab_menu_sale, fab_menu_attandance,
            fab_menu_target, fab_menu_map, fab_menu_endBreak;

    private TextView txtView_minutes, txtView_seconds;

    private double latitude;
    private double longitude;
    private Boolean isSUP;
    private BeemDatabase beemDatabase;
    private AppUtils appUtils;

    private int meetingStatus = 0;
    private String callTag;
    private BeemPreferences beemPreferences;

    //MVP
    private NavigationDrawerContractorSUP.NavigationDrawerPresenter navigationDrawerPresenter;
    private NetworkUtils networkUtils;

    //    private Realm realm;
    private RealmCRUD realmCRUD;
    private LoginResponse loginResponseRealm;
    private TextView txtView_navigationName, txtView_shopNameBA;
    private ListView listView_taskNav;
    private ConstraintLayout constraintLayout_NavigationBeemLogo;

    private Location getlocationFromTaskAPI;

    NavigationView navigationView;
    FabViewVisibility fabViewVisibility;
    private List<Task> withInRadiusStores;

    private String meetingTag;
    private int count;
    private SharedPreferences pictureNotesCount;
    private List<Task> taskList;
    private List<Datum> datumMerchantList;


    //Location Api
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private LocationProvider locationProvider;

    private Datum datum;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        withInRadiusStores = new ArrayList<>();

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i("LocationCallBack", "Failed!");
                    Toast.makeText(NavigationDrawerActivity.this, "Failed to get Lcoation", Toast.LENGTH_SHORT).show();
                } else {
//                    for (Location location : locationResult.getLastLocation())) {

                    Location location = locationResult.getLastLocation();
                    latitude = 0;
                    longitude = 0;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Log.i("LocCoor", latitude + " " + longitude);

                    fusedLocationProviderClient.removeLocationUpdates(this);

                    hideProgress();
                    if (isSUP == null) {
                        meetingMerhcant();
                    } else if (isSUP) {
                        attendanceSupervisor();
                    } else {
                        dispatchTakePictureIntent();
                    }

                }
            }
        };

        initProgressBar();

        pictureNotesCount = getSharedPreferences(Constants.PICTURE_COUNT, MODE_PRIVATE);
        count = pictureNotesCount.getInt(Constants.KEY_PICTURE_COUNT, 0);

        networkUtils = new NetworkUtils(NavigationDrawerActivity.this);

        navigationDrawerPresenter = new NavigationDrawerSUPPresenter(networkUtils, this);

        beemPreferences = new BeemPreferences(NavigationDrawerActivity.this);
        beemDatabase = new BeemDatabase(this);
        beemDatabase.getReadableDatabase();

        realmCRUD = new RealmCRUD(this);
        realmCRUD.getSampleUpdate();
        fabViewVisibility = new FabViewVisibility(this);

        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        appUtils = new AppUtils(this);

        listView_taskNav = findViewById(R.id.listView_taskNav);
        constraintLayout_NavigationBeemLogo = findViewById(R.id.constraintLayout_NavigationBeemLogo);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtView_navigationName = headerView.findViewById(R.id.txtView_navigationName);
        txtView_shopNameBA = headerView.findViewById(R.id.txtView_shopNameBA);

        txtView_minutes = (TextView) findViewById(R.id.txtView_minutes);
        txtView_seconds = (TextView) findViewById(R.id.txtView_seconds);

        //Fabs BA
        //Start
        fab_menu_ba = findViewById(R.id.fab_menu_ba);
        fab_menu_startBreak = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_startBreak);
        fab_menu_endBreak = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_endBreak);
        fab_menu_endDay = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_endDay);
        fab_menu_sale = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_sale);
        fab_menu_attandance = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_attandance);
        fab_menu_target = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_target);
        fab_menu_map = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_map);

        fab_menu_startBreak.setOnClickListener(this);
        fab_menu_endBreak.setOnClickListener(this);
        fab_menu_endDay.setOnClickListener(this);
        fab_menu_sale.setOnClickListener(this);
        fab_menu_attandance.setOnClickListener(this);
        fab_menu_target.setOnClickListener(this);
        fab_menu_map.setOnClickListener(this);
        //End

        //Fabs Supervisor
        //Start
        fab_menu_supervisor = findViewById(R.id.fab_menu_supervisor);
        fab_menu_markAttendance_supervisor = findViewById(R.id.fab_menu_markAttendance_supervisor);
        fab_menu_endAttendance_supervisor = findViewById(R.id.fab_menu_endAttendance_supervisor);
        fab_menu_addShop_supervisor = findViewById(R.id.fab_menu_addShop_supervisor);
        fab_menu_startMeeting_supervisor = findViewById(R.id.fab_menu_startMeeting_supervisor);
        fab_menu_endMeeting_supervisor = findViewById(R.id.fab_menu_endMeeting_supervisor);
        fab_menu_takePictures_supervisor = findViewById(R.id.fab_menu_takePictures_supervisor);
        fab_menu_addNotes_supervisor = findViewById(R.id.fab_menu_addNotes_supervisor);

        fab_menu_markAttendance_supervisor.setOnClickListener(this);
        fab_menu_endAttendance_supervisor.setOnClickListener(this);
        fab_menu_addShop_supervisor.setOnClickListener(this);
        fab_menu_startMeeting_supervisor.setOnClickListener(this);
        fab_menu_endMeeting_supervisor.setOnClickListener(this);
        fab_menu_takePictures_supervisor.setOnClickListener(this);
        fab_menu_addNotes_supervisor.setOnClickListener(this);
        //End

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = this.getSharedPreferences(Constants.ATTENDANCE_STATUS, MODE_PRIVATE);
        int id = preferences.getInt(Constants.KEY_ATTENDANCE_STATUS, 0);

        SharedPreferences preferencesMeetingSupervisor = this.getSharedPreferences(Constants.SUPERVISOR_MEETING_STATUS, MODE_PRIVATE);
        boolean meetingStatusSupervisor = preferencesMeetingSupervisor.getBoolean(Constants.KEY_SUPERVISOR_MEETING_STATUS, false);

        if (loginResponseRealm.getuT().toLowerCase().equals("sup")) {

            fab_menu_supervisor.setVisibility(View.VISIBLE);

            if (id == 1 && meetingStatusSupervisor) {
                fabViewVisibility.fabMarkAttendance(false);
                fabViewVisibility.fabEndAttendance(false);
                fabViewVisibility.fabStartMeeting(false);
                fabViewVisibility.fabEndMeeting(true);
                fabViewVisibility.fabsAfterStartMeeting(true);
            } else if (id == 1 && !meetingStatusSupervisor) {
                fabViewVisibility.fabMarkAttendance(false);
                fabViewVisibility.fabEndAttendance(true);
                fabViewVisibility.fabStartMeeting(true);
                fabViewVisibility.fabEndMeeting(false);
                fabViewVisibility.fabsAfterStartMeeting(false);
            } else if (id == 0) {
                fabViewVisibility.fabMarkAttendance(true);
                fabViewVisibility.fabEndAttendance(false);
                fabViewVisibility.fabStartMeeting(false);
                fabViewVisibility.fabEndMeeting(false);
                fabViewVisibility.fabsAfterStartMeeting(false);
            }

        } else {

            fab_menu_ba.setVisibility(View.VISIBLE);

            hideTopFabs();

            if (id == 1) {
                hideBottomFabs();
            } else {
                hideTopFabs();
            }
        }

        txtView_navigationName.setText(loginResponseRealm.getName().toUpperCase());
        if (loginResponseRealm.getuT().equals("BA")){
            txtView_shopNameBA.setVisibility(View.VISIBLE);
            txtView_shopNameBA.setText("Shop: " + loginResponseRealm.getStoreName());
        } else if (loginResponseRealm.getuT().equals("SUP")) {
            listView_taskNav.setVisibility(View.VISIBLE);
            constraintLayout_NavigationBeemLogo.setBackground(null);
            navigationDrawerPresenter.getTaskOnActivityLaunch("GetTask", loginResponseRealm.getUserId());

            startService(new Intent(NavigationDrawerActivity.this, BreakService.class));
        } else if (loginResponseRealm.getuT().equals("MCD")) {

            fab_menu_supervisor.setVisibility(View.VISIBLE);
            fab_menu_ba.setVisibility(View.GONE);
            fabViewVisibility.fabMerchant();

            if (id == 1) {
                fabViewVisibility.fabMerchantAttendance(true);
            } else if (id == 0) {
                fabViewVisibility.fabMerchantAttendance(false);
            }

            listView_taskNav.setVisibility(View.VISIBLE);
            constraintLayout_NavigationBeemLogo.setBackground(null);
            navigationDrawerPresenter.getMerchantTasks("GetMerchantTasks", loginResponseRealm.getUserId());

        }

        SharedPreferences preferencesMeeting = this.getSharedPreferences(Constants.BREAK_STATUS, MODE_PRIVATE);
        int breakStatus = preferencesMeeting.getInt(Constants.KEY_BREAK_STATUS, 0);

        if (breakStatus == 1) {
            fab_menu_startBreak.setVisibility(View.GONE);
            fab_menu_endBreak.setVisibility(View.VISIBLE);

            txtView_minutes.setVisibility(View.VISIBLE);
            txtView_seconds.setVisibility(View.VISIBLE);
        }

        getlocationFromTaskAPI = new Location("");

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            File userImageFile = appUtils.getImageFile(imageBitmap);
            final LoginResponse loginResponse = beemDatabase.getUserDetail();

            if (latitude == 0 || longitude == 0) {
                Toast.makeText(this, "Unable to get location, please check your location", Toast.LENGTH_SHORT).show();
            } else {

                if (loginResponseRealm.getuT().toLowerCase().equals("sup")) {

                    if (meetingTag.equals("StartMeeting")) {
                        withInRadiusStores.clear();

                        if (taskList != null) {

                            for (int i = 0; i < taskList.size(); i++) {
                                getlocationFromTaskAPI.setLatitude(taskList.get(i).getShopLat());
                                getlocationFromTaskAPI.setLongitude(taskList.get(i).getShopLng());

                                Location locationFromListener = new Location("");
                                locationFromListener.setLatitude(latitude);
                                locationFromListener.setLongitude(longitude);
                                float distanceInMeters = locationFromListener.distanceTo(getlocationFromTaskAPI);
                                boolean isWithinRadius = distanceInMeters < 100;

                                Log.i("RadiusIn", String.valueOf(isWithinRadius));

                                if (isWithinRadius) {
                                    withInRadiusStores.add(taskList.get(i));
                                }
                            }

                            if (withInRadiusStores.size() > 1) {

                                String[] storesWithinCircle = null;
                                storesWithinCircle = new String[withInRadiusStores.size()];
                                for (int j = 0; j < withInRadiusStores.size(); j++) {
                                    storesWithinCircle[j] = withInRadiusStores.get(j).getShopName();
                                }

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this)
                                        .setTitle("Please Click on Desired A Store")
                                        .setSingleChoiceItems(storesWithinCircle, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (i >= 0) {
                                                    StartMeetingRequest startMeetingRequest = new StartMeetingRequest(withInRadiusStores.get(i).getId(),
                                                            appUtils.getDate() + " " + appUtils.getTime(), userImageFile,
                                                            latitude, longitude, loginResponseRealm.getUserId());

                                                    if (networkUtils.isNetworkConnected()) {
                                                        showProgress();
                                                        networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                                                            @Override
                                                            public void success(Response<MeetingResponseModel> responseModelResponse) {

                                                                if (responseModelResponse.isSuccessful()) {

                                                                    beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(responseModelResponse.body().getTaskId()));

                                                                    insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                                            startMeetingRequest.getDateTime(),
                                                                            appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                                            latitude, longitude, loginResponseRealm.getUserId(),
                                                                            Integer.parseInt(responseModelResponse.body().getTaskId()), 1);
                                                                } else {

                                                                    insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                                            startMeetingRequest.getDateTime(),
                                                                            appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                                            latitude, longitude, loginResponseRealm.getUserId(),
                                                                            0, 0);
                                                                }

                                                                beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                                                hideProgress();
                                                                startMeetingFabs();
                                                            }

                                                            @Override
                                                            public void error(String error) {

                                                                beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);

                                                                insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                                        startMeetingRequest.getDateTime(),
                                                                        appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                                        latitude, longitude, loginResponseRealm.getUserId(),
                                                                        0, 0);
                                                                hideProgress();
                                                                Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);

                                                        insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                                startMeetingRequest.getDateTime(),
                                                                appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                                latitude, longitude, loginResponseRealm.getUserId(),
                                                                0, 0);

                                                        alterDialog(true);
                                                    }
                                                }
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog1 = alertDialogBuilder.create();
                                alertDialog1.show();


                            } else if (withInRadiusStores.size() == 1) {

                                StartMeetingRequest startMeetingRequest = new StartMeetingRequest(withInRadiusStores.get(0).getId(),
                                        appUtils.getDate() + " " + appUtils.getTime(), userImageFile, latitude, longitude,
                                        loginResponseRealm.getUserId());

                                if (networkUtils.isNetworkConnected()) {
                                    showProgress();
                                    networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                                        @Override
                                        public void success(Response<MeetingResponseModel> responseModelResponse) {

                                            if (responseModelResponse.isSuccessful()) {

                                                beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(responseModelResponse.body().getTaskId()));

                                                insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                        startMeetingRequest.getDateTime(),
                                                        appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                        latitude, longitude, loginResponseRealm.getUserId(),
                                                        Integer.parseInt(responseModelResponse.body().getTaskId()), 1);
                                            } else {
                                                insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                        startMeetingRequest.getDateTime(),
                                                        appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                        latitude, longitude, loginResponseRealm.getUserId(),
                                                        0, 0);
                                            }

                                            hideProgress();
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                            startMeetingFabs();
                                        }

                                        @Override
                                        public void error(String error) {
                                            hideProgress();
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);

                                            insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                                    startMeetingRequest.getDateTime(),
                                                    appUtils.imageBitmapToBiteConversion(imageBitmap),
                                                    latitude, longitude, loginResponseRealm.getUserId(),
                                                    0, 0);

                                            startMeetingFabs();
                                            Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);

                                    insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                            startMeetingRequest.getDateTime(),
                                            appUtils.imageBitmapToBiteConversion(imageBitmap),
                                            latitude, longitude, loginResponseRealm.getUserId(),
                                            0, 0);

                                    startMeetingFabs();

                                    alterDialog(true);
                                }

                            } else if (withInRadiusStores.size() == 0) {
                                meetingOnNullTask(userImageFile);
                            }
                        } else {
                            meetingOnNullTask(userImageFile);
                        }

                    } else if (meetingTag.equals("ImageUpdateMeeting")) {
                        final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
                        final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);

                        final SharedPreferences getCustomRelationMeetingId = getSharedPreferences(Constants.CUSTOM_RELATION_MEETING_ID, MODE_PRIVATE);
                        final int customRelationMeetingId = getCustomRelationMeetingId.getInt(Constants.KEY_CUSTOM_RELATION_MEETING_ID, 0);

                        StartMeetingRequest startMeetingRequest = new StartMeetingRequest(getMeetingStartId, userImageFile, null);
                        if (networkUtils.isNetworkConnected()) {
                            showProgress();
                            networkUtils.updateMeeting(startMeetingRequest, new MeetingCallBack() {
                                @Override
                                public void success(Response<MeetingResponseModel> responseModelResponse) {
                                    if (responseModelResponse.isSuccessful()) {

                                        beemPreferences.initialize_and_createPreferences_picturesCount(count++);
                                        insertImageUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), 1, 0, 1);

                                        Log.i("Update Image in Meeting", "Success");
                                    } else {

                                        beemPreferences.initialize_and_createPreferences_picturesCount(count++);
                                        insertImageUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), 1, 0, 0);

                                    }
                                    hideProgress();
                                }

                                @Override
                                public void error(String error) {

                                    beemPreferences.initialize_and_createPreferences_picturesCount(count++);
                                    insertImageUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                            appUtils.imageBitmapToBiteConversion(imageBitmap), 1, 0, 0);
                                    hideProgress();
                                    Toast.makeText(NavigationDrawerActivity.this, error + " Image not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {

                            beemPreferences.initialize_and_createPreferences_picturesCount(count++);
                            insertImageUpdateMeetingSUP(customRelationMeetingId, getMeetingStartId,
                                    appUtils.imageBitmapToBiteConversion(imageBitmap), 1, 0, 0);
                            hideProgress();
                            alterDialog(true);
                        }
                    } else if (meetingTag.equals("EndMeeting")) {

                        final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
                        final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);

                        final SharedPreferences getCustomRelationMeetingId = getSharedPreferences(Constants.CUSTOM_RELATION_MEETING_ID, MODE_PRIVATE);
                        final int customRelationMeetingId = getCustomRelationMeetingId.getInt(Constants.KEY_CUSTOM_RELATION_MEETING_ID, 0);

                        StartMeetingRequest startMeetingRequest = new StartMeetingRequest(getMeetingStartId,
                                appUtils.getDate() + " " + appUtils.getTime(), userImageFile);

                        if (networkUtils.isNetworkConnected()) {
                            showProgress();
                            networkUtils.endMeeting(startMeetingRequest, new MeetingCallBack() {
                                @Override
                                public void success(Response<MeetingResponseModel> responseModelResponse) {

                                    if (responseModelResponse.isSuccessful()) {
                                        insertEndMeetingSUP(customRelationMeetingId, getMeetingStartId, appUtils.getTime(),
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), 1);
                                    } else {
                                        insertEndMeetingSUP(customRelationMeetingId, getMeetingStartId, appUtils.getTime(),
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), 0);
                                    }

                                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                                    fabViewVisibility.fabStartMeeting(true);
                                    fabViewVisibility.fabEndMeeting(false);
                                    fabViewVisibility.fabEndAttendance(true);
                                    fabViewVisibility.fabMarkAttendance(false);
                                    fabViewVisibility.fabsAfterStartMeeting(false);
                                    hideProgress();
                                }

                                @Override
                                public void error(String error) {

                                    insertEndMeetingSUP(customRelationMeetingId, getMeetingStartId, appUtils.getTime(),
                                            appUtils.imageBitmapToBiteConversion(imageBitmap), 0);

                                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                                    hideProgress();

                                    fabViewVisibility.fabStartMeeting(true);
                                    fabViewVisibility.fabEndMeeting(false);
                                    fabViewVisibility.fabEndAttendance(true);
                                    fabViewVisibility.fabMarkAttendance(false);
                                    fabViewVisibility.fabsAfterStartMeeting(false);
                                    Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                            insertEndMeetingSUP(customRelationMeetingId, getMeetingStartId, appUtils.getTime(),
                                    appUtils.imageBitmapToBiteConversion(imageBitmap), 0);

                            fabViewVisibility.fabStartMeeting(true);
                            fabViewVisibility.fabEndMeeting(false);
                            fabViewVisibility.fabEndAttendance(true);
                            fabViewVisibility.fabMarkAttendance(false);
                            fabViewVisibility.fabsAfterStartMeeting(false);
                            alterDialog(true);
                        }
                    }
                } else if (loginResponseRealm.getuT().toLowerCase().equals("ba")) {

                    if (meetingStatus == 1) {

                        if (networkUtils.isNetworkConnected()) {

                            final int status = 1;

                            networkUtils.attandanceBA(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(), userImageFile,
                                    appUtils.getTime(), latitude, longitude, status, new AttandanceInterface() {
                                        @Override
                                        public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                            if (attandanceResponseResponse.body().getStatus() == 1) {
                                                beemPreferences.initialize_and_createPreferences(attandanceResponseResponse.body().getId());
                                                beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);

                                                hideBottomFabs();
                                            } else {
                                                Toast.makeText(NavigationDrawerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void failed(String error) {

                                            Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {

                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this);
                            alertBuilder.setTitle("Network")
                                    .setMessage("Please Check your internet connection")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });

                            alertBuilder.show();

                        }
                    } else if (meetingStatus == 0) {

                        SharedPreferences preferences = this.getSharedPreferences(Constants.BA_ATTENDANCE_ID, MODE_PRIVATE);
                        final int id = preferences.getInt(Constants.KEY_BA_ATTENDANCE_ID, 0);

                        if (networkUtils.isNetworkConnected()) {

                            Log.i("thisClass", "token" + id);

                            networkUtils.endAttandenceBA(id, appUtils.getTime(), latitude, longitude, userImageFile,
                                    new EndAttendanceInterface() {
                                        @Override
                                        public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                            if (attandanceResponseResponse.body().getStatus() == 1) {
                                                beemPreferences.initialize_and_createPreferences_forAttendanceStatus(0);

                                                hideTopFabs();

                                            } else {
                                                Toast.makeText(NavigationDrawerActivity.this, "Something Went Wrong Please Try Again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void failed(String error) {
                                            Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this);
                            alertBuilder.setTitle("Network")
                                    .setMessage("Please Check your internet connection")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });

                            alertBuilder.show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        SalesAndNoSales salesAndNoSales = realmCRUD.getSaleAndNoSales();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notifications) {
            // Handle the camera action
        } else if (id == R.id.nav_sync) {

            SyncDataToServerClass syncDataToServerClass = new SyncDataToServerClass(NavigationDrawerActivity.this);

            if (loginResponseRealm.getuT().toLowerCase().equals("ba")) {
                if (salesAndNoSales != null) {
                    if (salesAndNoSales.getTotal_nosales() > 0) {
                        syncDataToServerClass.updateSalesData();
                    } else {
                        Toast.makeText(this, "No Pending Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No Pending Data", Toast.LENGTH_SHORT).show();
                }
            } else if (loginResponseRealm.getuT().toLowerCase().equals("sup")) {
                syncDataToServerClass.startMeetingUpdate();
            }
        } else if (id == R.id.nav_logout) {

            SharedPreferences preferences = this.getSharedPreferences(Constants.ATTENDANCE_STATUS, MODE_PRIVATE);
            int endDayStatus = preferences.getInt(Constants.KEY_ATTENDANCE_STATUS, 0);


            if (salesAndNoSales.getTotal_nosales() > 0) {
                Toast.makeText(this, "please sync data before logout", Toast.LENGTH_SHORT).show();
            } else if (endDayStatus == 1) {
                Toast.makeText(this, "please end day before logout", Toast.LENGTH_SHORT).show();
            } else {
                if (loginResponseRealm.getuT().equals("BA")) {
                    logout();
                } else if (loginResponseRealm.getuT().equals("SUP")){
                    locationProvider = new LocationProvider(this);
                    locationProvider.initializeLocationManager();
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_OFF);
        beemPreferences.initialize_and_createPreferences_forAttendanceStatus(Constants.STATUS_OFF);
        beemPreferences.initialize_and_createPreferences_forBrand("");
        realmCRUD.removeUserBrandsSKUCategories(loginResponseRealm.getUserId());
        Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void endTracking(double latitude, double longitude) {
        locationProvider.removeUpdateLocation();
        callTag = "EndTracking";
        SharedPreferences preferences = this.getSharedPreferences(Constants.TRACKING_START_ID, MODE_PRIVATE);
        int trackingId = preferences.getInt(Constants.KEY_TRACKING_START_ID, 0);
        if (networkUtils.isNetworkConnected()) {
            showProgress();
            networkUtils.endTracking(trackingId, latitude, longitude, 0, appUtils.getDate() + " " +
                    appUtils.getTime(), this);
        } else {
            alterDialog(true);
        }
    }

    @Override
    public void onClick(View v) {

        SalesAndNoSales salesAndNoSales = realmCRUD.getSaleAndNoSales();

        switch (v.getId()) {
            case R.id.fab_menu_attandance:

                meetingStatus = 1;

                getLocation(false);

                break;
            case R.id.fab_menu_endDay:

                if (salesAndNoSales.getTotal_nosales() > 0) {
                    Toast.makeText(this, "please sync data before end day", Toast.LENGTH_SHORT).show();
                } else {
                    realmCRUD.clearRecordsAtEndDay();
                    meetingStatus = 0;
                    getLocation(false);

                }

                break;
            case R.id.fab_menu_map:
                break;
            case R.id.fab_menu_sale:
                Intent intent = new Intent(NavigationDrawerActivity.this, SalesActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_menu_startBreak:

                if (networkUtils.isNetworkConnected()) {
                    callTag = "BreakType";
                    networkUtils.getBreakTypes(this);
                    showProgress();
                } else {
                    alterDialog(true);
                }

                break;

            case R.id.fab_menu_endBreak:

                if (networkUtils.isNetworkConnected()) {
                    callTag = "EndBreak";

                    final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.BREAK_START_ID, MODE_PRIVATE);
                    final int getBreakId = loginStatusPreferences.getInt(Constants.KEY_BREAK_START_ID, 0);

                    networkUtils.endBreak(getBreakId, appUtils.getDate() + " " + appUtils.getTime(), 0, this);
                    showProgress();
                } else {
                    alterDialog(true);
                }

                break;
            case R.id.fab_menu_target:
                Intent intent2 = new Intent(NavigationDrawerActivity.this, TargetsAndAchievementsActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;

            case R.id.fab_menu_markAttendance_supervisor:

                meetingStatus = 1;

                getLocation(true);

                break;
            case R.id.fab_menu_endAttendance_supervisor:

                meetingStatus = 0;
                getLocation(true);

                break;

            case R.id.fab_menu_startMeeting_supervisor:

                if (loginResponseRealm.getuT().equals("MCD")) {
                    getLocation(null);
                } else {
                    meetingTag = "StartMeeting";
                    meetingConfirmationDialog(true);
                }

                break;
            case R.id.fab_menu_endMeeting_supervisor:

                meetingTag = "EndMeeting";
                meetingConfirmationDialog(false);

                break;

            case R.id.fab_menu_addShop_supervisor:

                intent = new Intent(NavigationDrawerActivity.this, AddShopsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.fab_menu_addNotes_supervisor:

                final SharedPreferences getNotesCount = getSharedPreferences(Constants.NOTES_COUNT, MODE_PRIVATE);
                int notesCounts = getNotesCount.getInt(Constants.KEY_NOTES_COUNT, 0);

                if (notesCounts >= 4) {
                    Toast.makeText(this, "you have added 4 notes!", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(NavigationDrawerActivity.this, NotesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;

            case R.id.fab_menu_takePictures_supervisor:

                int counts = pictureNotesCount.getInt(Constants.KEY_PICTURE_COUNT, 0);

                if (counts >= 4) {
                    Toast.makeText(this, "you have captured 4 images!", Toast.LENGTH_SHORT).show();
                } else {
                    meetingTag = "ImageUpdateMeeting";
                    dispatchTakePictureIntent();
                }

                break;

        }
    }

    void hideTopFabs() {
        fab_menu_startBreak.setVisibility(View.GONE);
        fab_menu_endDay.setVisibility(View.GONE);

        //**Temp
        //**Default Mode: GONE
        fab_menu_sale.setVisibility(View.GONE);
        //***

        fab_menu_attandance.setVisibility(View.VISIBLE);
        fab_menu_map.setVisibility(View.VISIBLE);
    }

    void hideBottomFabs() {
        fab_menu_startBreak.setVisibility(View.VISIBLE);
        fab_menu_endDay.setVisibility(View.VISIBLE);

        //***temp
        //**Default Mode: VISIBLE
        fab_menu_sale.setVisibility(View.VISIBLE);
        //*****

        fab_menu_attandance.setVisibility(View.GONE);
        fab_menu_map.setVisibility(View.GONE);
    }

    void getLocation(Boolean isSUP) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1000);
//        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(NavigationDrawerActivity.this);
        com.google.android.gms.tasks.Task<LocationSettingsResponse> task = settingsClient
                .checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i("LocationClient", "SettingsSatisfied!");

                startLocationUpdates(isSUP);
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ApiException apiException = (ApiException) e;

                if (apiException.getStatusCode() == RESOLUTION_REQUIRED){
                    Log.i("exception on Failure", e.getMessage().toString());
                    startLocationUpdates(isSUP);
                }
                Toast.makeText(NavigationDrawerActivity.this, "Please check your Location settings!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return false;
    }

    void insertDataMarkAttendance(String date, int userId, String name, byte[] startImage, String startTime,
                                  double latitude, double longitude, int status, int markAttendanceResponseID) {
        MarkAttendance markAttendance = new MarkAttendance();
        markAttendance.setEmpid(userId);
        markAttendance.setDate(date);
        markAttendance.setName(name);
        markAttendance.setStartImage(startImage);
        markAttendance.setStartTime(startTime);
        markAttendance.setLongitude(latitude);
        markAttendance.setLongitude(longitude);
        markAttendance.setStatus(status);

        realmCRUD.addMarkAttendanceDetails(markAttendance, markAttendanceResponseID);
    }

    void insertDataEndMarkAttendance(String date, int userId, String name, byte[] startImage, String startTime,
                                     float latitude, float longitude, int status, int markAttendanceResponseID) {
        MarkAttendance markAttendance = new MarkAttendance();
        markAttendance.setEmpid(userId);
        markAttendance.setDate(date);
        markAttendance.setName(name);
        markAttendance.setStartImage(startImage);
        markAttendance.setStartTime(startTime);
        markAttendance.setLongitude(latitude);
        markAttendance.setLongitude(longitude);
        markAttendance.setStatus(status);

        realmCRUD.addMarkAttendanceDetails(markAttendance, markAttendanceResponseID);
    }

    void attendanceSupervisor() {

        if (latitude == 0 && longitude == 0) {
            Toast.makeText(this, "Unable to get location, is your location on?", Toast.LENGTH_SHORT).show();
        } else {
            if (meetingStatus == 1) {

                if (networkUtils.isNetworkConnected()) {

                    final int status = 1;

                    networkUtils.attandanceSUP(loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                            appUtils.getTime(), latitude, longitude, status, new AttandanceInterface() {
                                @Override
                                public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                    if (attandanceResponseResponse.body().getStatus() == 1) {
                                        beemPreferences.initialize_and_createPreferences(attandanceResponseResponse.body().getId());
                                        beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);

                                        fabViewVisibility.fabMarkAttendance(false);
                                        fabViewVisibility.fabEndAttendance(true);
                                        fabViewVisibility.fabStartMeeting(true);
                                        fabViewVisibility.fabEndMeeting(false);
                                        fabViewVisibility.fabsAfterStartMeeting(false);

                                    } else {

                                        Toast.makeText(NavigationDrawerActivity.this, "Something Went Wrong Please Try Again!", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void failed(String error) {

                                    Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    alterDialog(true);
                }

            } else if (meetingStatus == 0) {

                SharedPreferences preferences = this.getSharedPreferences(Constants.BA_ATTENDANCE_ID, MODE_PRIVATE);
                final int id = preferences.getInt(Constants.KEY_BA_ATTENDANCE_ID, 0);

                if (networkUtils.isNetworkConnected()) {

                    Log.i("thisClass", "token" + id);

                    networkUtils.endAttandenceSUP(id, appUtils.getTime(), latitude, longitude, 0,
                            new EndAttendanceInterface() {
                                @Override
                                public void success(Response<AttandanceResponse> attandanceResponseResponse) {
                                    if (attandanceResponseResponse.body().getStatus() == 1) {
                                        beemPreferences.initialize_and_createPreferences_forAttendanceStatus(0);

                                        fabViewVisibility.fabMarkAttendance(true);
                                        fabViewVisibility.fabEndAttendance(false);
                                        fabViewVisibility.fabStartMeeting(false);
                                        fabViewVisibility.fabEndMeeting(false);
                                        fabViewVisibility.fabsAfterStartMeeting(false);

                                    } else {

                                    }
                                }

                                @Override
                                public void failed(String error) {
                                    Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    alterDialog(true);
                }
            }
        }
    }

    void alterDialog(boolean isSUP) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this);
        alertBuilder.setTitle("Network")
                .setMessage("Please Check your internet connection")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!isSUP) {
                            insertDataMarkAttendance(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                                    appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(), latitude, longitude,
                                    0, 0);
                            beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);

                            hideBottomFabs();

                            Toast.makeText(NavigationDrawerActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertBuilder.show();
    }


    void meetingConfirmationDialog(boolean isMeetingStart) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this);

        String message = "";
        String status = "";

        if (isMeetingStart) {
            message = "Do you want to start meeting?";
            status = "START";
        } else {
            message = "Do you want to end meeting?";
            status = "END";
        }

        alertBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getLocation(false);

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NavigationDrawerActivity.this, "Meeting Not Started", Toast.LENGTH_SHORT).show();
                    }
                });

        alertBuilder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();

        if (loginResponseRealm.getuT().equals("SUP")) {
            listView_taskNav.setVisibility(View.VISIBLE);
            constraintLayout_NavigationBeemLogo.setBackground(null);
            navigationDrawerPresenter.getTaskOnActivityLaunch("GetTask", loginResponseRealm.getUserId());

            stopService(new Intent(NavigationDrawerActivity.this, BreakService.class));
            startService(new Intent(NavigationDrawerActivity.this, BreakService.class));

        }
        registerReceiver(broadcastReceiver, new IntentFilter(BreakService.COUNTDOWN_BR));
        Log.i("register", " broadcast");
    }

    private void startLocationUpdates(Boolean isSUP) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Please check Lcoation Permission", Toast.LENGTH_SHORT).show();
        } else {
            showProgress();
            this.isSUP = isSUP;

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, this.locationCallback, null);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        Log.i("register", " unregister broadcast");
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
            Log.i("stop", " stop broadcast");
        } catch (Exception e) {
            Log.i("stop", e.getLocalizedMessage().toString());
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("Destroy", "service");
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressShow();
    }

    @Override
    public void hideProgress() {
        progressHide();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccesofState_getTaskList(Response<TaskResponse> taskResponse) {
        showTaskList(taskResponse.body());
    }

    @Override
    public void showTaskList(TaskResponse taskResponse) {
        if (taskList != null) {
            taskList.clear();
        }
        taskList = taskResponse.getTasks();
        CustomListAdapterTasks customListAdapterTasks =
                new CustomListAdapterTasks(NavigationDrawerActivity.this, 0, taskList);

        listView_taskNav.setVisibility(View.VISIBLE);
        listView_taskNav.setAdapter(customListAdapterTasks);
    }

    @Override
    public void showSuccesofState_getTaskListMerchant(Response<MerchantTaskResponse> taskResponseResponse) {
        showTaskListMerchant(taskResponseResponse.body());
    }

    @Override
    public void showTaskListMerchant(MerchantTaskResponse taskResponse) {
            listView_taskNav.setOnItemClickListener(this);
            if (datumMerchantList != null) {
                datumMerchantList.clear();
            }

            datumMerchantList = taskResponse.getData();

            MerchantTaskAdapter merchantTaskAdapter = new
                    MerchantTaskAdapter(NavigationDrawerActivity.this, 0, datumMerchantList);

        listView_taskNav.setVisibility(View.VISIBLE);
        listView_taskNav.setAdapter(merchantTaskAdapter);
    }

    @Override
    public void showSuccesofState_startMeeting(Response<MeetingResponseModel> response, String states) {
    }

    void insertStartMeetingSUP(int task_id, String StartTime, byte[] img1, double latitude, double longitude, int emp_id,
                               int startMeetingResponseId, int syncStatus) {

        MeetingSUPStartTable meetingSUPStartTable = new MeetingSUPStartTable(task_id, StartTime, img1, latitude, longitude, emp_id,
                startMeetingResponseId, syncStatus);
        int customRelationMeetingId = realmCRUD.insertStartMeetingSUPOffline(meetingSUPStartTable);

        beemPreferences.initialize_and_createPreferences_customRelationMeetingId(customRelationMeetingId);
    }

    void insertImageUpdateMeetingSUP(int customRelationMeetingId, int startMeetingResponseId, byte[] img2, int isImage, int isNote, int syncStatus) {

        MeetingSUPUpdatesTable meetingSUPUpdatesTable = new MeetingSUPUpdatesTable(customRelationMeetingId, startMeetingResponseId, img2,
                isImage, isNote, syncStatus);

        realmCRUD.insertImageUpdateMeetingSUPOffline(meetingSUPUpdatesTable);

    }

    void insertEndMeetingSUP(int customRelationMeetingId, int startMeetingResponseId, String endTime, byte[] img4, int syncStatus) {

        MeetingSUPEndTable meetingSUPEndTable = new MeetingSUPEndTable(customRelationMeetingId, startMeetingResponseId, endTime, img4, syncStatus);

        realmCRUD.insertEndMeetingSUPOffline(meetingSUPEndTable);
    }

    @Override
    public void UpdateSuccess(Response response) {

        Response<ResponseSUP> responseSUPResponse;

        if (callTag.equals("BreakType")) {
            Response<BreakTypeResponseModel> breakTypeResponseModel = response;
            List<Status> statusList = breakTypeResponseModel.body().getStatus();

            String[] storesWithinCircle = new String[statusList.size()];
            for (int j = 0; j < statusList.size(); j++) {
                storesWithinCircle[j] = statusList.get(j).getBreakType();
            }

            hideProgress();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this)
                    .setTitle("Choose Break Type")
                    .setSingleChoiceItems(storesWithinCircle, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (networkUtils.isNetworkConnected()) {
                                callTag = "StartBreak";
                                networkUtils.startBreak(loginResponseRealm.getUserId(), statusList.get(i).getId(),
                                        appUtils.getDate(), appUtils.getTime(), NavigationDrawerActivity.this);

                                beemPreferences.initialize_and_createPreferences_breakTime(statusList.get(i).getDuration());
                                beemPreferences.initialize_and_createPreferences_breakType(statusList.get(i).getBreakType());

                                showProgress();

                            } else {
                                dialogInterface.cancel();
                                Toast.makeText(NavigationDrawerActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                            }

                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog1 = alertDialogBuilder.create();
            alertDialog1.show();

        } else if (callTag.equals("StartBreak")) {
            responseSUPResponse = response;
            if (responseSUPResponse.body().getStatus() == 1) {
                beemPreferences.initialize_and_createPreferences_breakId(responseSUPResponse.body().getId());
                beemPreferences.initialize_and_createPreferences_breakStatus(responseSUPResponse.body().getStatus());
                fab_menu_endBreak.setVisibility(View.VISIBLE);
                fab_menu_startBreak.setVisibility(View.GONE);

                final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.BREAK_TIME, MODE_PRIVATE);
                final String getBreakTime = loginStatusPreferences.getString(Constants.KEY_BREAK_TIME, "");


                long timeMinute = 0, timeSeconds = 0, totalMillis = 0;

                String time = getBreakTime;
                String[] getString = time.split(":");

                timeMinute = Long.parseLong(getString[1]);
                timeSeconds = Long.parseLong(getString[2]);

                totalMillis = timeMinute + timeSeconds;

                totalMillis = TimeUnit.MINUTES.toMillis(totalMillis);

                MyService.TIME = totalMillis;

                startService(new Intent(this, MyService.class));

                txtView_minutes.setVisibility(View.VISIBLE);
                txtView_seconds.setVisibility(View.VISIBLE);

                Log.i("Service from Navigation", "Started");

                hideProgress();
            } else {
                Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else if (callTag.equals("EndBreak")) {
            responseSUPResponse = response;
            if (response.isSuccessful()) {
                if (responseSUPResponse.body().getStatus() == 1) {
                    beemPreferences.initialize_and_createPreferences_breakStatus(0);
                }
                fab_menu_endBreak.setVisibility(View.GONE);
                fab_menu_startBreak.setVisibility(View.VISIBLE);

                Intent intent = new Intent(this, MyService.class);
                intent.putExtra("isStop", true);
                MyService.TIME = 0;
                stopService(intent);
                txtView_minutes.setVisibility(View.GONE);
                txtView_seconds.setVisibility(View.GONE);

                hideProgress();
            } else {
                Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        } else if (callTag.equals("EndTracking")) {
            hideProgress();
//            responseSUPResponse = response;
//            if (responseSUPResponse.body().getStatus() == 1) {
            stopService(new Intent(NavigationDrawerActivity.this, TrackingService.class));
            beemPreferences.initialize_and_createPreferences_trackingStartId(0);
            beemPreferences.initialize_and_createPreferences_trackingStatus(0);
            locationProvider.removeUpdateLocation();
            logout();
//            }
        }
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        hideProgress();
        Toast.makeText(this, baseResponse.getError(), Toast.LENGTH_SHORT).show();
    }

    void startMeetingFabs() {
        fabViewVisibility.fabStartMeeting(false);
        fabViewVisibility.fabEndMeeting(true);
        fabViewVisibility.fabEndAttendance(false);
        fabViewVisibility.fabMarkAttendance(false);
        fabViewVisibility.fabsAfterStartMeeting(true);
    }

    void meetingOnNullTask(File userImageFile) {
        StartMeetingRequest startMeetingRequest = new StartMeetingRequest(appUtils.getDate() + " " + appUtils.getTime(),
                userImageFile, latitude, longitude, loginResponseRealm.getUserId());
        if (networkUtils.isNetworkConnected()) {
            showProgress();

            networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                @Override
                public void success(Response<MeetingResponseModel> responseModelResponse) {

                    if (responseModelResponse.isSuccessful()) {

                        beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(String.valueOf(responseModelResponse.body().getTaskId())));
                        insertStartMeetingSUP(0,
                                startMeetingRequest.getDateTime(),
                                appUtils.imageBitmapToBiteConversion(imageBitmap),
                                latitude, longitude, loginResponseRealm.getUserId(),
                                Integer.parseInt(responseModelResponse.body().getTaskId()), 1);
                    } else {

                        insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                                startMeetingRequest.getDateTime(),
                                appUtils.imageBitmapToBiteConversion(imageBitmap),
                                latitude, longitude, loginResponseRealm.getUserId(),
                                0, 0);
                    }

                    hideProgress();
                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                    startMeetingFabs();

                }

                @Override
                public void error(String error) {

                    insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                            startMeetingRequest.getDateTime(),
                            appUtils.imageBitmapToBiteConversion(imageBitmap),
                            latitude, longitude, loginResponseRealm.getUserId(),
                            0, 0);
                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                    hideProgress();
                    startMeetingFabs();

                    Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            insertStartMeetingSUP(startMeetingRequest.getTask_id(),
                    startMeetingRequest.getDateTime(),
                    appUtils.imageBitmapToBiteConversion(imageBitmap),
                    latitude, longitude, loginResponseRealm.getUserId(),
                    0, 0);
            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);

            startMeetingFabs();

            alterDialog(true);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
            Log.i("BR", "on");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateUI(Intent intent) {
        if (intent.getExtras() != null) {
            boolean getSerivceStatus = intent.getBooleanExtra("GetTaskList", false);
            if (getSerivceStatus) {
                listView_taskNav.setVisibility(View.VISIBLE);
                constraintLayout_NavigationBeemLogo.setBackground(null);
                if (loginResponseRealm.getuT().equals("SUP")) {
                    navigationDrawerPresenter.getTaskOnActivityLaunch("GetTask", loginResponseRealm.getUserId());
                } else if (loginResponseRealm.getuT().equals("MCD")) {
                    navigationDrawerPresenter.getMerchantTasks("GetMerchantTasks", loginResponseRealm.getUserId());
                }
                stopService(new Intent(NavigationDrawerActivity.this, BreakService.class));
                startService(new Intent(NavigationDrawerActivity.this, BreakService.class));
                Log.i("this", "Intent From BroadCast");
            } else {
                long millisUntilFinished = intent.getLongExtra("countDown", 0);
                int minutes, seconds;
                minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                seconds = (int) ((millisUntilFinished / 1000) % 60);

                if (minutes < 10) {
                    txtView_minutes.setText("0" + String.valueOf(minutes));
                } else {
                    txtView_minutes.setText(String.valueOf(minutes));
                }
                if (seconds < 10) {
                    txtView_seconds.setText("0" + String.valueOf(seconds));
                } else {
                    txtView_seconds.setText(String.valueOf(seconds));
                }

                if (minutes == 0) {
                    txtView_minutes.setText("00");
                }

                if (seconds == 0) {
                    txtView_seconds.setText("00");
                }


                Log.i("UpdateUi", "Countdown seconds remaining: " + ((millisUntilFinished / 1000) % 60));
                Log.i("UpdateUi", "Countdown seconds remaining: " + ((millisUntilFinished / (1000 * 60)) % 60));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        datum = datumMerchantList.get(position);
            getLocation(null);
    }

    void  meetingMerhcant () {

        MeetingRequestMerchant meetingRequestMerchant = new MeetingRequestMerchant();
        if (datum != null) {
            meetingRequestMerchant.setShop_id(datum.getShopId());

            if (datum.getLatitude() == 0 || datum.getLongitude() == 0) {
                meetingRequestMerchant.setLatitude(latitude);
                meetingRequestMerchant.setLongitude(longitude);
            } else {

                meetingRequestMerchant.setLatitude(datum.getLatitude());
                meetingRequestMerchant.setLongitude(datum.getLongitude());

            }

        } else {
            meetingRequestMerchant.setLatitude(latitude);
            meetingRequestMerchant.setLongitude(longitude);
        }

        Intent intent = new Intent(NavigationDrawerActivity.this, MerchantActivity.class);


        if (networkUtils.isNetworkConnected()) {
            networkUtils.updateShopLatLong(meetingRequestMerchant, new BaseCallbackInterface() {
                @Override
                public void success(Response response) {
                    BeemPreferencesCount beemPreferencesCount = new BeemPreferencesCount(NavigationDrawerActivity.this);
                    beemPreferencesCount.putInt(Constants.TASK_ID, datum.getTaskId());
                    beemPreferencesCount.putInt(Constants.SHOP_ID, datum.getShopId());

                    Location locationFromListener = new Location("");
                    locationFromListener.setLatitude(latitude);
                    locationFromListener.setLongitude(longitude);

                    getlocationFromTaskAPI.setLatitude(datum.getLatitude());
                    getlocationFromTaskAPI.setLongitude(datum.getLongitude());

                    float distanceInMeters = locationFromListener.distanceTo(getlocationFromTaskAPI);
                    boolean isWithinRadius = distanceInMeters < 150;

                    if (isWithinRadius) {
                        beemPreferencesCount.putInt(Constants.RADIUS, 1);
                    } else {
                        beemPreferencesCount.putInt(Constants.RADIUS, 0);
                    }
                    startActivity(intent);

                }

                @Override
                public void failure(String error) {

                }
            });
        } else {
            alterDialog(true);
        }
    }
}