package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
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
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import retrofit2.Response;

public class LoginActivity extends Activity {

    private NetworkUtils networkUtils;
    private EditText edtText_username, edtText_password;
    String username, password;
    private Button btn_login;
    private Intent intent;
    private BeemDatabase beemDatabase;
    private BeemPreferences beemPreferences;
    private TextView txtView_validationResponse, txtView_beem, txtView_descriptions;
    private RelativeLayout relativeLogin;
//    private ImageView imgView_beemLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
            return;
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

                if (TextUtils.isEmpty(edtText_username.getText().toString()) || TextUtils.isEmpty(edtText_password.getText().toString())){
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
                                    if (loginResponse.body().getuT().toLowerCase().equals("SUP")) {
                                        beemPreferences.initialize_and_createPreferences_loginUserDesignation(true);
                                    } else if (loginResponse.body().getuT().toLowerCase().equals("BA")) {
                                        beemPreferences.initialize_and_createPreferences_loginUserDesignation(false);
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
                                        intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                        startActivity(intent);
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

}


