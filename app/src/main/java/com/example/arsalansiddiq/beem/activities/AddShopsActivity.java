package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azimolabs.maskformatter.MaskFormatter;
import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.models.requestmodels.AddShopRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import retrofit2.Response;

public class AddShopsActivity extends BaseActivity implements UpdateCallback {

    private EditText edtText_shopName, edtText_owner, edtText_contactPerson, edtText_shopContactNumber;
    private Button btn_addShop;
    private LoginResponse loginResponse;
    private RealmCRUD realmCRUD;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shops);

        initProgressBar();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        realmCRUD = new RealmCRUD();
        loginResponse = realmCRUD.getLoginInformationDetails();

        edtText_shopName = findViewById(R.id.edtText_shopName);
        edtText_owner = findViewById(R.id.edtText_owner);
        edtText_contactPerson = findViewById(R.id.edtText_contactPerson);
        edtText_shopContactNumber = findViewById(R.id.edtText_shopContactNumber);
        btn_addShop = findViewById(R.id.btn_addShop);

        setupEditTextWithDashes(R.id.edtText_shopContactNumber, Constants.NUMBERS_DASHED_MASK);

        btn_addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtText_shopName.getText().toString()) || TextUtils.isEmpty(edtText_owner.getText().toString()) ||
                        TextUtils.isEmpty(edtText_contactPerson.getText().toString()) ||
                        TextUtils.isEmpty(edtText_shopContactNumber.getText().toString())) {
                    Toast.makeText(AddShopsActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtText_shopContactNumber.getText().toString().length() < 13) {
                        edtText_shopContactNumber.setTextColor(Color.parseColor("#ff0000"));
                    } else {
                        addShopCall();
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

    void addShopCall() {
        Location location;
        NetworkUtils networkUtils = new NetworkUtils(AddShopsActivity.this);
        if (networkUtils.isNetworkConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (networkUtils.isNetworkConnected()) {

                progressShow();

                AddShopRequest addShopRequest = new AddShopRequest(loginResponse.getUserId(), edtText_shopName.getText().toString(),
                        edtText_owner.getText().toString(), edtText_contactPerson.getText().toString(), edtText_shopContactNumber.getText().toString(),
                        location.getLatitude(), location.getLongitude());

                networkUtils.addShop(addShopRequest, this);
            } else {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddShopsActivity.this);
                alertBuilder.setTitle("Network")
                        .setMessage("Please Check your internet connection")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addShopCall();
                            }
                        });

                alertBuilder.show();
            }
        }
    }

    @Override
    public void UpdateSuccess(Response response) {
        progressHide();
        if (response.isSuccessful()) {
            Log.i("AddShop", "shopadded");
            finish();
        }
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        progressHide();
        Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
        finish();
    }
}
