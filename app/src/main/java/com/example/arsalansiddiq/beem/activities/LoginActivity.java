package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azimolabs.maskformatter.MaskFormatter;
import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.MeetingResponseModel;
import com.example.arsalansiddiq.beem.receiver.LocationProvider;
import com.example.arsalansiddiq.beem.services.TrackingService;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import retrofit2.Response;

public class LoginActivity extends Activity implements UpdateCallback{

    private final String LOG_TAG = LoginActivity.class.getName();

    private NetworkUtils networkUtils;
    private EditText edtText_username, edtText_password;
    String username, password;
    private Button btn_login;
    private Intent intent;
    private BeemDatabase beemDatabase;
    private BeemPreferences beemPreferences;
    private TextView txtView_validationResponse, txtView_beem, txtView_descriptions;
    private RelativeLayout relativeLogin;
    private AppUtils appUtils;
    private LocationProvider locationProvider;
    private int empId;
//    private ImageView imgView_beemLogo
// ;

//    private Boolean isAppInUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appUtils = new AppUtils(this);
        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);

        locationProvider = new LocationProvider(LoginActivity.this);
//        locationProvider.initializeLocationManager();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                isAppInUse = dataSnapshot.child("fir-eleven").child("isAppInUse").getValue(Boolean.class);
//                //do what you want with the email
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if (isAppInUse) {
            beemDatabase = new BeemDatabase(this);
            beemDatabase.getWritableDatabase();
            beemPreferences = new BeemPreferences(this);

            networkUtils = new NetworkUtils(LoginActivity.this);

            edtText_username = (EditText) findViewById(R.id.edtText_username);
            edtText_password = (EditText) findViewById(R.id.edtText_password);
            txtView_validationResponse = findViewById(R.id.txtView_validationResponse);
            relativeLogin = (RelativeLayout) findViewById(R.id.relativeLogin);
//        imgView_beemLogo = findViewById(R.id.imgView_beemLogo);
            txtView_beem = findViewById(R.id.txtView_beem);
            btn_login = findViewById(R.id.btn_login);

            username = String.valueOf(edtText_username.getText());
            password = String.valueOf(edtText_password.getText());


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
            }


            hideLogin();


            final SharedPreferences loginStatusPreferences = getSharedPreferences(Constants.LOGIN_STATUS, MODE_PRIVATE);
            final int status = loginStatusPreferences.getInt(Constants.KEY_LOGIN_STATUS, 0);

            final Handler handler = new Handler();
            try {
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {

                        Intent intent = null;

                        if (status == 0) {
//                        intent = new Intent(LoginActivity.this, LoginActivity.class);
                            viewLogin();
                        } else if (status == 1) {
                            intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                        } else if (status == 2) {
                            intent = new Intent(LoginActivity.this, MerchantActivity.class);
                            startActivity(intent);

                        }

                    }
                }, 2000);
            } catch (Exception e) {
                e.printStackTrace();
            }


            btn_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    txtView_validationResponse.setVisibility(View.GONE);

                    final RealmCRUD realmCRUD = new RealmCRUD(LoginActivity.this);

                    if (TextUtils.isEmpty(edtText_username.getText().toString()) || TextUtils.isEmpty(edtText_password.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Please insert Valid credentials!", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginRequest loginRequest = new LoginRequest(edtText_username.getText().toString(), edtText_password.getText().toString());
                        if (networkUtils.isNetworkConnected()) {
                            networkUtils.userLogin(loginRequest, new LoginInterface() {
                                @Override
                                public void success(final Response<LoginResponse> loginResponse) {
                                    if (loginResponse.body().getStatus() == 1) {

                                        beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_ON);
                                        beemPreferences.initialize_and_createPreferences_forBrand(loginResponse.body().getBrand());
                                        //User Desingnation
                                        //Start

                                        if (loginResponse.body().getuT().equals("SUP") || loginResponse.body().getuT().equals("MCD")) {
                                            empId = loginResponse.body().getUserId();
                                            locationProvider.initializeLocationManager();
                                            beemPreferences.initialize_and_createPreferences_loginUserDesignation(true);
                                        } else if (loginResponse.body().getuT().equals("BA")) {
                                            beemPreferences.initialize_and_createPreferences_loginUserDesignation(false);
                                            startActivity(intent);
                                        }
                                        //End

                                        realmCRUD.addUserLoginInformation(loginResponse.body(), 1);

//                                    if (beemDatabase.checkUserExist(loginResponse.body().getUserId()) &&
//                                            realmCRUD.checkLoginIdExist(loginResponse.body().getUserId())) {
//                                        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
//                                        startActivity(intent);
//                                    } else {
                                        beemDatabase.insertBAInfo(loginResponse.body().getUserId(),
                                                loginResponse.body().getName(), loginResponse.body().getBrand(), loginResponse.body().getuT(),
                                                loginResponse.body().getStoreId(), loginResponse.body().getStatus());
//                                        startActivity(intent);
//                                    }
                                    } else {
                                        beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_OFF);
                                        txtView_validationResponse.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void failed(String error) {
                                    beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_OFF);
                                    txtView_validationResponse.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

//        }
    }


    //Number Validation
    private void setupEditTextWithDashes(int layoutId, String mask) {
        EditText field = (EditText) findViewById(layoutId);
        field.addTextChangedListener(new MaskFormatter(mask, field));
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void viewLogin () {

        relativeLogin.setBackgroundResource(R.mipmap.login_bg);

        edtText_username.setVisibility(View.VISIBLE);
        edtText_password.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.VISIBLE);

    }

    void hideLogin () {

        edtText_username.setVisibility(View.GONE);
        edtText_password.setVisibility(View.GONE);
        btn_login.setVisibility(View.GONE);

        relativeLogin.setBackgroundResource(R.mipmap.splash);

    }

    public void startTracking(double latitude, double longitude) {
        locationProvider.removeUpdateLocation();
        if (empId != 0 && latitude != 0 && longitude != 0) {
            networkUtils.startTracking(empId, appUtils.getDate() + " " +
                    appUtils.getTime(), latitude, longitude, this);
        } else {
            Toast.makeText(this, "Server response malfuncioned!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void UpdateSuccess(Response response) {
        MeetingResponseModel meetingResponseModel = (MeetingResponseModel) response.body();
        beemPreferences.initialize_and_createPreferences_trackingStartId(meetingResponseModel.getEmp_track_id());
        startService(new Intent(this, TrackingService.class));
        startActivity(intent);
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        Log.i(LOG_TAG, "server " + baseResponse.getError());
    }

    public void alertLocation(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertBuilder.setTitle("Location!")
                .setMessage("Please Check your Location Permission!")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        locationProvider.initializeLocationManager();
                    }
                });

        alertBuilder.show();
    }
}


