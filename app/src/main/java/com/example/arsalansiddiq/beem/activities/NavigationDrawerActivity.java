package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.activities.contractor.NavigationDrawerContractorSUP;
import com.example.arsalansiddiq.beem.activities.presenter.NavigationDrawerSUPPresenter;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapterTasks;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.AttandanceInterface;
import com.example.arsalansiddiq.beem.interfaces.EndAttendanceInterface;
import com.example.arsalansiddiq.beem.interfaces.MeetingCallBack;
import com.example.arsalansiddiq.beem.interfaces.RealmCallback;
import com.example.arsalansiddiq.beem.models.databasemodels.MarkAttendance;
import com.example.arsalansiddiq.beem.models.databasemodels.SalesAndNoSales;
import com.example.arsalansiddiq.beem.models.requestmodels.StartMeetingRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.Task;
import com.example.arsalansiddiq.beem.models.responsemodels.tasksresponsemodels.TaskResponse;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.FabViewVisibility;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.SyncDataToServerClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class NavigationDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, LocationListener, NavigationDrawerContractorSUP.NavigationView{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    private com.github.clans.fab.FloatingActionMenu fab_menu_ba, fab_menu_supervisor;

    private com.github.clans.fab.FloatingActionButton fab_menu_markAttendance_supervisor, fab_menu_endAttendance_supervisor,
            fab_menu_addShop_supervisor, fab_menu_startMeeting_supervisor, fab_menu_endMeeting_supervisor,
            fab_menu_takePictures_supervisor, fab_menu_addNotes_supervisor;

    private com.github.clans.fab.FloatingActionButton fab_menu_startBreak, fab_menu_endDay, fab_menu_sale, fab_menu_attandance,
            fab_menu_target, fab_menu_map;

    private LocationManager locationManager;
    private float latitude, longitude;
    private BeemDatabase beemDatabase;
    private AppUtils appUtils;

    private int meetingUpdateImage = 0;
    private boolean isStartMeetingRequest;
    private boolean isUpdateMeetingRequest;
    private boolean isEndMeetingRequest;
    private int meetingStatus = 0;
    private int supervisorMeetingStatus = 0;
    private BeemPreferences beemPreferences;

    //MVP
    private NavigationDrawerContractorSUP.NavigationDrawerPresenter navigationDrawerPresenter;
    private NetworkUtils networkUtils;

//    private Realm realm;
    private RealmCRUD realmCRUD;
    private LoginResponse loginResponseRealm;
    private TextView txtView_navigationName;
    private ListView listView_taskNav;

    private Location getlocationFromListener;
    private Location getlocationFromTaskAPI;

    NavigationView navigationView;
    FabViewVisibility fabViewVisibility;
    private List<Task> withInRadiusStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        withInRadiusStores = new ArrayList<>();

        initProgressBar();

        networkUtils = new NetworkUtils(NavigationDrawerActivity.this);

        navigationDrawerPresenter = new NavigationDrawerSUPPresenter(networkUtils, this);

        beemPreferences = new BeemPreferences(NavigationDrawerActivity.this);
        beemDatabase = new BeemDatabase(this);
        beemDatabase.getReadableDatabase();

        realmCRUD = new RealmCRUD(this);
        fabViewVisibility = new FabViewVisibility(this);

        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        appUtils = new AppUtils(this);

        listView_taskNav = findViewById(R.id.listView_taskNav);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtView_navigationName = headerView.findViewById(R.id.txtView_navigationName);

        //Fabs BA
        //Start
        fab_menu_ba = findViewById(R.id.fab_menu_ba);
        fab_menu_startBreak = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_startBreak);
        fab_menu_endDay = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_endDay);
        fab_menu_sale = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_sale);
        fab_menu_attandance = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_attandance);
        fab_menu_target = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_target);
        fab_menu_map = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_menu_map);

        fab_menu_startBreak.setOnClickListener(this);
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

//        hideTopFabs();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences preferences = this.getSharedPreferences(Constants.ATTENDANCE_STATUS, MODE_PRIVATE);
        int id = preferences.getInt(Constants.KEY_ATTENDANCE_STATUS, 0);

        if (loginResponseRealm.getuT().toLowerCase().equals("sup")) {

            SharedPreferences preferencesMeeting = this.getSharedPreferences(Constants.SUPERVISOR_MEETING_STATUS, MODE_PRIVATE);
            boolean meetingStatus = preferencesMeeting.getBoolean(Constants.KEY_SUPERVISOR_MEETING_STATUS, false);

            fab_menu_supervisor.setVisibility(View.VISIBLE);


            if (id == 1 && meetingStatus) {
                fabViewVisibility.fabMarkAttendance(false);
                fabViewVisibility.fabEndAttendance(false);
                fabViewVisibility.fabStartMeeting(false);
                fabViewVisibility.fabEndMeeting(true);
                fabViewVisibility.fabsAfterStartMeeting(true);
            } else if (id == 1 && !meetingStatus) {
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


        if (loginResponseRealm.getuT().equals("SUP")) {
            navigationDrawerPresenter.getTaskOnActivityLaunch("GetTask", loginResponseRealm.getUserId());
        }

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

            if (loginResponseRealm.getuT().toLowerCase().equals("sup")) {

                if (isStartMeetingRequest) {
                    if (supervisorMeetingStatus == 1) {
                        List<Task> taskList = realmCRUD.getAllTasks();

                        if (taskList != null) {

                            for (int i = 0; i < taskList.size(); i++) {
                                getlocationFromTaskAPI = new Location("");
                                getlocationFromTaskAPI.setLatitude(taskList.get(i).getShopLat());
                                getlocationFromTaskAPI.setLongitude(taskList.get(i).getShopLng());

                                float distanceInMeters = getlocationFromListener.distanceTo(getlocationFromTaskAPI);
                                boolean isWithinRadius = distanceInMeters < 100000;

                                Log.i("RadiusIn", String.valueOf(isWithinRadius));

                                if (isWithinRadius) {
                                    withInRadiusStores.add(taskList.get(i));
                                }
                            }

                            if (withInRadiusStores.size() > 1) {

                                String[] storesWithinCircle = new String[withInRadiusStores.size()];
                                for (int j = 0; j < withInRadiusStores.size(); j++) {
                                    storesWithinCircle[j] = withInRadiusStores.get(j).getShopName();
                                }

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NavigationDrawerActivity.this)
                                        .setTitle("Please Click on Desired A Store")
                                        .setSingleChoiceItems(storesWithinCircle, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                selectedStore = i;
                                                if (i >= 0) {
                                                    StartMeetingRequest startMeetingRequest = new StartMeetingRequest(withInRadiusStores.get(i).getId(),
                                                            appUtils.getDate() + " " + appUtils.getTime(), userImageFile);

                                                    if (networkUtils.isNetworkConnected()) {
                                                        showProgress();
                                                        networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                                                            @Override
                                                            public void success(Response<MeetingResponseModel> responseModelResponse) {

                                                                isEndMeetingRequest = false;

                                                                hideProgress();
                                                                beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                                                beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(responseModelResponse.body().getTaskId()));
                                                                fabViewVisibility.fabStartMeeting(false);
                                                                fabViewVisibility.fabEndMeeting(true);
                                                                fabViewVisibility.fabEndAttendance(false);
                                                                fabViewVisibility.fabMarkAttendance(false);
                                                                fabViewVisibility.fabsAfterStartMeeting(true);
                                                            }

                                                            @Override
                                                            public void error(String error) {
                                                                beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                                                                hideProgress();
                                                                Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        alterDialog(true);
                                                    }
                                                }
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog1 = alertDialogBuilder.create();
                                alertDialog1.show();


                            } else if (withInRadiusStores.size() == 1) {

                                if (networkUtils.isNetworkConnected()) {
                                    showProgress();
                                    StartMeetingRequest startMeetingRequest = new StartMeetingRequest(withInRadiusStores.get(0).getId(),
                                            appUtils.getDate() + " " + appUtils.getTime(), userImageFile);
                                    networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                                        @Override
                                        public void success(Response<MeetingResponseModel> responseModelResponse) {

                                            isEndMeetingRequest = false;

                                            hideProgress();
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                            beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(responseModelResponse.body().getTaskId()));
                                            fabViewVisibility.fabStartMeeting(false);
                                            fabViewVisibility.fabEndMeeting(true);
                                            fabViewVisibility.fabEndAttendance(false);
                                            fabViewVisibility.fabMarkAttendance(false);
                                            fabViewVisibility.fabsAfterStartMeeting(true);
                                        }

                                        @Override
                                        public void error(String error) {
                                            hideProgress();
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                                            Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    alterDialog(true);
                                }


//                                navigationDrawerPresenter.onClickButtonStartMeeting("StartMeeting", startMeetingRequest);

                            } else if (withInRadiusStores.size() == 0) {


                                if (networkUtils.isNetworkConnected()) {
                                    showProgress();
                                    StartMeetingRequest startMeetingRequest = new StartMeetingRequest(appUtils.getDate() + " " + appUtils.getTime(), userImageFile);
//                                    navigationDrawerPresenter.onClickButtonStartMeeting("StartMeeting", startMeetingRequest);

                                    networkUtils.startMeeting(startMeetingRequest, new MeetingCallBack() {
                                        @Override
                                        public void success(Response<MeetingResponseModel> responseModelResponse) {

                                            isEndMeetingRequest = false;

                                            hideProgress();
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                            beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(String.valueOf(responseModelResponse.body().getId())));
                                            fabViewVisibility.fabStartMeeting(false);
                                            fabViewVisibility.fabEndMeeting(true);
                                            fabViewVisibility.fabEndAttendance(false);
                                            fabViewVisibility.fabMarkAttendance(false);
                                            fabViewVisibility.fabsAfterStartMeeting(true);
                                        }

                                        @Override
                                        public void error(String error) {
                                            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(false);
                                            Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    alterDialog(true);
                                }
                            }
                        }

                    } else if (supervisorMeetingStatus == 0) {


                        final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
                        final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);


                        StartMeetingRequest startMeetingRequest = new StartMeetingRequest(getMeetingStartId,
                                appUtils.getDate() + " " + appUtils.getTime(), userImageFile);

                        if (networkUtils.isNetworkConnected()) {
                            showProgress();
                            networkUtils.endMeeting(startMeetingRequest, new MeetingCallBack() {
                                @Override
                                public void success(Response<MeetingResponseModel> responseModelResponse) {

                                    isEndMeetingRequest = true;

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
                                    beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
                                    hideProgress();
                                    Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            alterDialog(true);
                        }
                    }
                } else {
                    if (meetingUpdateImage == 1) {

                        final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.MEETING_START_ID, MODE_PRIVATE);
                        final int getMeetingStartId = loginStatusPreferences.getInt(Constants.KEY_MEETING_START_ID, 0);
                        StartMeetingRequest startMeetingRequest = new StartMeetingRequest(getMeetingStartId, userImageFile, null);
                        if (networkUtils.isNetworkConnected()) {
                            showProgress();
                            networkUtils.updateMeeting(startMeetingRequest, new MeetingCallBack() {
                                @Override
                                public void success(Response<MeetingResponseModel> responseModelResponse) {
                                    hideProgress();
                                    Log.i("Update Image in Meeting", "Success");
                                }

                                @Override
                                public void error(String error) {
                                    hideProgress();
                                    Toast.makeText(NavigationDrawerActivity.this, error + " Image not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            alterDialog(true);
                        }
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

                                            insertDataMarkAttendance(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                                                    appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(), latitude, longitude,
                                                    attandanceResponseResponse.body().getStatus(), attandanceResponseResponse.body().getId());

                                            beemDatabase.insertMark_BA_attendanceInfo(loginResponse.getUserId(), appUtils.getDate(), loginResponse.getName(),
                                                    appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                    latitude, longitude, attandanceResponseResponse.body().getStatus(), attandanceResponseResponse.body().getId());

                                            hideBottomFabs();
                                        } else {

                                            insertDataMarkAttendance(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                                                    appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(), latitude, longitude,
                                                    attandanceResponseResponse.body().getStatus(), 0);

                                            beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);
                                            beemDatabase.insertMark_BA_attendanceInfo(loginResponse.getUserId(), appUtils.getDate(), loginResponse.getName(),
                                                    appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                    latitude, longitude, 0, 0);

                                            hideBottomFabs();

                                            Toast.makeText(NavigationDrawerActivity.this, "Something Went Wrong Please Try Again!", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void failed(String error) {

                                        beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);
                                        insertDataMarkAttendance(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(), latitude, longitude,
                                                0, 0);

                                        beemDatabase.insertMark_BA_attendanceInfo(loginResponse.getUserId(), appUtils.getDate(), loginResponse.getName(),
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                latitude, longitude, 0, 0);

                                        hideBottomFabs();

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
                                        insertDataMarkAttendance(appUtils.getDate(), loginResponseRealm.getUserId(), loginResponseRealm.getName(),
                                                appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(), latitude, longitude,
                                                0, 0);
                                        beemPreferences.initialize_and_createPreferences_forAttendanceStatus(1);

                                        hideBottomFabs();

                                        Toast.makeText(NavigationDrawerActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
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

                                            insertDataEndMarkAttendance(appUtils.getDate(), id, loginResponseRealm.getName(), appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                    latitude, longitude, attandanceResponseResponse.body().getStatus(), 0);

                                            hideTopFabs();

                                        } else {

                                            insertDataEndMarkAttendance(appUtils.getDate(), id, loginResponseRealm.getName(), appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                    latitude, longitude, 0, 0);
                                            Toast.makeText(NavigationDrawerActivity.this, "Something Went Wrong Please Try Again!", Toast.LENGTH_SHORT).show();
                                            hideTopFabs();
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
                                        insertDataEndMarkAttendance(appUtils.getDate(), id, loginResponseRealm.getName(), appUtils.imageBitmapToBiteConversion(imageBitmap), appUtils.getTime(),
                                                latitude, longitude, 0, 0);

                                        Toast.makeText(NavigationDrawerActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                                        hideTopFabs();
                                    }
                                });

                        alertBuilder.show();
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

            if (salesAndNoSales != null) {
                if (salesAndNoSales.getTotal_nosales() > 0) {
                    SyncDataToServerClass syncDataToServerClass = new SyncDataToServerClass(NavigationDrawerActivity.this);
                    syncDataToServerClass.updateSalesData();
                } else {
                    Toast.makeText(this, "No Pending Data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No Pending Data", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_logout) {

            SharedPreferences preferences = this.getSharedPreferences(Constants.ATTENDANCE_STATUS, MODE_PRIVATE);
            int endDayStatus = preferences.getInt(Constants.KEY_ATTENDANCE_STATUS, 0);


            if (salesAndNoSales.getTotal_nosales() > 0) {
                Toast.makeText(this, "please sync data before logout", Toast.LENGTH_SHORT).show();
            } else if (endDayStatus == 1) {
                Toast.makeText(this, "please end day before logout", Toast.LENGTH_SHORT).show();
            } else {
                beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_OFF);
                beemPreferences.initialize_and_createPreferences_forAttendanceStatus(Constants.STATUS_OFF);
                beemPreferences.initialize_and_createPreferences_forBrand("");
                realmCRUD.removeUserBrandsSKUCategories(loginResponseRealm.getUserId());
                Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                Intent intent1 = new Intent(NavigationDrawerActivity.this, Replica.class);
                startActivity(intent1);
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

                isStartMeetingRequest = true;
                supervisorMeetingStatus = 1;
                meetingConfirmationDialog(true);

                break;
            case R.id.fab_menu_endMeeting_supervisor:

                isStartMeetingRequest = true;
                supervisorMeetingStatus = 0;
                meetingConfirmationDialog(false);

                break;

            case R.id.fab_menu_addShop_supervisor:

                intent = new Intent(NavigationDrawerActivity.this, AddShopsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.fab_menu_addNotes_supervisor:

                intent = new Intent(NavigationDrawerActivity.this, NotesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.fab_menu_takePictures_supervisor:

                meetingUpdateImage = 1;
                dispatchTakePictureIntent();

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

    @Override
    public void onLocationChanged(Location location) {
        getlocationFromListener = location;
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();

        Log.i("LocationCoor", latitude + "  " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("onStatusChanged", provider + "  " + status + "   " + extras);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("onProviderEnabled", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("onProviderDisabled", provider);
    }

    void getLocation(boolean isSUP) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
            return;
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, NavigationDrawerActivity.this);

            if (isSUP) {
                attendanceSupervisor();
            } else {
                dispatchTakePictureIntent();
            }

        }
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

//                                    fabsAttendance(false);

                                    Toast.makeText(NavigationDrawerActivity.this, "Something Went Wrong Please Try Again!", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void failed(String error) {

//                                fabsAttendance(false);
                                Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();

                            }
                        });
            } else {
                alterDialog(true);
            }

        } else if (meetingStatus == 0){

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

//                                    fabsAttendance(true);
                                }
                            }

                            @Override
                            public void failed(String error) {
//                                fabsAttendance(true);
                                Toast.makeText(NavigationDrawerActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                alterDialog(true);
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
//                        dispatchTakePictureIntent();

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


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
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
        realmCRUD.insertTasksDetails(taskResponse.body().getTasks(), new RealmCallback() {
            @Override
            public void onSuccess() {
                showTaskList();
            }

            @Override
            public void onError() {
                Log.i("Navigation:  ", "List not added!");
            }
        });
    }

    @Override
    public void showTaskList() {
        CustomListAdapterTasks customListAdapterTasks =
                new CustomListAdapterTasks(NavigationDrawerActivity.this, 0, realmCRUD.getAllTasks());

        listView_taskNav.setAdapter(customListAdapterTasks);
    }

    @Override
    public void showSuccesofState_startMeeting(Response<MeetingResponseModel> response, String states) {
//        if (states.equals("StartMeeting")) {
//            Log.i("MeetingStatus", "1");
//            beemPreferences.initialize_and_createPreferences_meetingStatusSupervisor(true);
//            beemPreferences.initialize_and_createPreferences_startMeetingSupervisorID(Integer.parseInt(response.body().getTaskId()));
//            fabViewVisibility.fabStartMeeting(false);
//            fabViewVisibility.fabEndMeeting(true);
//            fabViewVisibility.fabEndAttendance(false);
//            fabViewVisibility.fabMarkAttendance(false);
//            fabViewVisibility.fabsAfterStartMeeting(true);
//        } else if (states.equals("UpdateMeeting")) {
//            Log.i("UpdateMeetingStatus", "1");
//        }
    }

}
