package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapter;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.SampleInterface;
import com.example.arsalansiddiq.beem.models.HolderListModel;
import com.example.arsalansiddiq.beem.models.ListViewModelCheck;
import com.example.arsalansiddiq.beem.models.databasemodels.SaleApiResponseTableRealm;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.CustomAlertDialog;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class OrderActivity extends BaseActivity implements LocationListener, RadioGroup.OnCheckedChangeListener{

    private final String LOG_TAG = OrderActivity.class.getName();

//    private HolderListModel holderListModel;

    private Spinner spinner_saleStatus = null;
    private NetworkUtils networkUtils = null;
    private String cusName, contact; String email; String gender; String age; String cBrand = null;
    private String cBrandName;
    private String pBrand = null;
    private Integer sale_id = 0, saleStatus = null;
    private LocationManager locationManager = null;
    private float latitude, longitude;
    private BeemDatabase beemDatabase = null;

    private ListView listView_order = null;
    private Intent intent = null;
    private FrameLayout frameLayout_noProducts = null;
    private View view = null;
    private EditText edtText_loose = null;
    private EditText edtText_carton = null;
//    private TextView txtView_name = null;
    private LinearLayout linearLayout_bottom;

    private List<HolderListModel> holderListModelList;

    private List<SalesSKUArrayResponse> salesSKUArrayResponseArrayList = null;

    private CustomAlertDialog customAlertDialog;

    private int doubleQuantity = 0;
    private int calculatedAge = 0;
    private int nextIdSales;
    private AppUtils appUtils;

    private RealmCRUD realmCRUD;

//    protected ProgressBar progressBar;

    private CheckBox checkbox_loose, checkbox_carton;

    public static ArrayList<ListViewModelCheck> listViewModelCheckArrayList;


    @BindView(R.id.txtView_cBrandSelected)
    TextView txtView_cBrandSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        listViewModelCheckArrayList = new ArrayList<>();

        initProgressBar();

        beemDatabase = new BeemDatabase(this);
        beemDatabase.getReadableDatabase();

        customAlertDialog = new CustomAlertDialog(this);
        networkUtils = new NetworkUtils(this);
        appUtils = new AppUtils(this);

        realmCRUD = new RealmCRUD(this);

        spinner_saleStatus = findViewById(R.id.spinner_saleStatus);

        listView_order = findViewById(R.id.listView_order);
        linearLayout_bottom = findViewById(R.id.linearLayout_bottom);

        listView_order.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listView_order.setItemsCanFocus(true);

        frameLayout_noProducts = findViewById(R.id.frameLayout_noProducts);

        holderListModelList = new ArrayList<>();

        if (getIntent().getExtras() != null) intent = getIntent();
        cusName = intent.getStringExtra("name");
        contact = intent.getStringExtra("contact");
        email = intent.getStringExtra("email");
        gender = intent.getStringExtra("gender");
        age = intent.getStringExtra("age");
        pBrand = intent.getStringExtra("pBrand");
        cBrand = intent.getStringExtra("cBrand");
        cBrandName = intent.getStringExtra("cBrandName");

        txtView_cBrandSelected.setText(cBrandName);

        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(this, R.array.saleStatus_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_saleStatus.setAdapter(adapterGender);
        spinner_saleStatus.setSelection(2);

        spinner_saleStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2) {
                    saleStatus = 1;
//                    getSalesId();
//                    sale_id = 1;
                    linearLayout_bottom.setVisibility(View.VISIBLE);
                } else if (position == 1){
                    saleStatus = 0;
//                    sale_id = 0;
                    linearLayout_bottom.setVisibility(View.GONE);
                } else {
                    saleStatus = null;
//                    sale_id = null;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        frameLayout_noProducts.setVisibility(View.GONE);

        if (spinner_saleStatus.getSelectedItemPosition() == 2) {
            linearLayout_bottom.setVisibility(View.VISIBLE);
//            getSalesId();
            getBrandItems();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSubmit(View view) {

        if (spinner_saleStatus.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select sale status", Toast.LENGTH_SHORT).show();
        } else {
            if (spinner_saleStatus.getSelectedItemPosition() == 1) {
                getSalesId(saleStatus);
//                finish();
            } else {
                getLocation();
//                getSalesId(saleStatus);
                getSelectedItemAndPrice();
            }
        }

    }


    void sendOrder(final int sales_id) {

        Log.i("dates", appUtils.getDate());

        final boolean isConnected;

        if (networkUtils.isNetworkConnected()) {
            isConnected = true;

            insertorAddOrder(sales_id, isConnected);

        } else {

            isConnected = false;

//            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OrderActivity.this);
//            alertBuilder.setTitle("Network")
//            .setMessage("Please Check your internet connection")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                            insertorAddOrder(sales_id, isConnected);
                            Toast.makeText(OrderActivity.this, "Order Saved Offline!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//            alertBuilder.show();
        }

    }

    void insertorAddOrder(final int sales_id, boolean isConnected) {
        progressShow();
        for (int i = 0; i < holderListModelList.size(); i++) {

            final HolderListModel holderListModel = holderListModelList.get(i);
            holderListModel.setCustomSaleId(nextIdSales);

            if (isConnected) {

                networkUtils.sendOrderDetail(holderListModel.getStoreId(), sales_id, appUtils.getDate(), holderListModel.getBrand()
                        , holderListModel.getSkuCategory(), holderListModel.getSKU(), holderListModel.getSaleType(), holderListModel.getNoItem(),
                        holderListModel.getPrice(), holderListModel.getsAmount(), new SampleInterface() {

                            @Override
                            public void success(LoginResponse loginResponse) {
                                Log.i(LOG_TAG, "sendOrder Status" + loginResponse.getStatus() + "  " + holderListModel.getStoreId());

                                final BeemPreferences beemPreferences = new BeemPreferences(OrderActivity.this);
                                beemPreferences.initialize_and_createPreferences_forStatus(loginResponse.getStatus());

                                //Offline Checking
//                                realmCRUD.insertOrderDetails(holderListModel, appUtils.getDate(), 0, 0, 0, 0);
                                realmCRUD.insertOrderDetails(holderListModel, appUtils.getDate(), sales_id, Integer.parseInt(loginResponse.getOrder_id()), loginResponse.getStatus(), 1);

                            }

                            @Override
                            public void failed(String error) {

                                //Offline Checking
//                                realmCRUD.insertOrderDetails(holderListModel, appUtils.getDate(), 0, 0, 0, 0);
                                realmCRUD.insertOrderDetails(holderListModel, appUtils.getDate(), sales_id, 0, 0, 0);
                            }
                        });

            } else {


                //Offline Checking
                realmCRUD.insertOrderDetails(holderListModel, appUtils.getDate(), 0, 0, 0, 0);
            }
        }

        progressHide();

        finish();
//        intent = new Intent(OrderActivity.this, SalesActivity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();

        Log.i("LocationOrder", latitude + "  " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
            return;
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, OrderActivity.this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                finish();
//                intent = new Intent(OrderActivity.this, SalesActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                return true;
        }
        return false;
    }

    private void getBrandItems() {


        LoginResponse loginResponse = realmCRUD.getLoginInformationDetails();

        if (realmCRUD.checkCurrentUserHasSavedBrands(loginResponse.getUserId())) {

//            salesSKUArrayResponseArrayList = realmCRUD.getUserBrandsSKUCategory(loginResponse.getUserId());
            salesSKUArrayResponseArrayList = realmCRUD.getUserBrandsSKUCategoryByBrandId(Integer.parseInt(cBrand));

            CustomListAdapter adapter = new CustomListAdapter(OrderActivity.this, 0,
                    realmCRUD.getUserBrandsSKUCategoryByBrandId(Integer.parseInt(cBrand)));

            for (int i = 0; i < salesSKUArrayResponseArrayList.size(); i++) {
                ListViewModelCheck listViewModelCheck = new ListViewModelCheck();
                listViewModelCheck.setId(salesSKUArrayResponseArrayList.get(i).getId());
                listViewModelCheck.setEditTextView_loose("");
                listViewModelCheck.setEditTextView_carton("");
                listViewModelCheckArrayList.add(listViewModelCheck);
            }
            listView_order.setAdapter(adapter);
        } else {
            frameLayout_noProducts.setVisibility(View.VISIBLE);
        }

    }


    void getSelectedItemAndPrice() {

        int listLength = 0;

        SalesSKUArrayResponse skuArrayResponse;

        beemDatabase.removeSelectedItemTableRaws();

//        int listLength = listView_order.getChildCount();
        if  (listViewModelCheckArrayList != null || listViewModelCheckArrayList.size() != 0) {
            listLength = listViewModelCheckArrayList.size();
        }

        int checkSelectionExist = 0;
        doubleQuantity = 0;

        Integer looseItem, cartonItem;

        if  (listLength > 0) {

            for (int i = 0; i < listLength; i++) {

                looseItem = null; cartonItem = null;

                String looseText = listViewModelCheckArrayList.get(i).getEditTextView_loose().toString();
                String cartonText = listViewModelCheckArrayList.get(i).getEditTextView_carton().toString();

                //Changes requested by humza Friday 15-9-2018
                //Start
                int totalItem = 0, totalAmount = 0;

                if (looseText.equals("") &&
                        cartonText.equals("") ) {

                } else if (looseText.length() > 0 &&
                        cartonText.length() > 0){

                    looseItem = Integer.parseInt(looseText);
                    cartonItem = Integer.parseInt(cartonText);

                    checkSelectionExist++;
                } else if (looseText.length() > 0 &&
                        TextUtils.isEmpty(cartonText)){

                    looseItem = Integer.parseInt(looseText);
                    cartonItem = 0;

                } else if (TextUtils.isEmpty(looseText) &&
                        cartonText.length() > 0){
                    looseItem = 0;
                    cartonItem = Integer.parseInt(cartonText);

                }
                //End

                if (looseItem != null || cartonItem != null) {

                    checkSelectionExist++;

                    skuArrayResponse = salesSKUArrayResponseArrayList.get(i);
                    try {
                        totalItem = ((skuArrayResponse.getItemPerCarton() * cartonItem) + looseItem);
                        totalAmount = (totalItem * skuArrayResponse.getPrice());
                    } catch (NumberFormatException e) {
                        Log.e(LOG_TAG, "Total Number of Item :   " + e.getStackTrace().toString());
                    }

                    LoginResponse loginResponse = beemDatabase.getUserDetail();

                    HolderListModel holder =  new HolderListModel(Integer.valueOf(loginResponse.getStoreId()), sale_id, nextIdSales,
                            appUtils.getDate(), skuArrayResponse.getBrand(), skuArrayResponse.getCateId(), skuArrayResponse.getId(), cartonItem, totalItem,
                            skuArrayResponse.getPrice(),totalAmount, 0);

                    holderListModelList.add(holder);

                }

            }

            //Changes requested by humza Friday 15-9-2018
            //Start
            if (checkSelectionExist > 0) {
                getSalesId(saleStatus);
//                    sendOrder(sale_id);
                    //End
            } else {
                customAlertDialog.hideDialog();
                customAlertDialog.showDialog(true);
            }
        } else {
            listView_order.setVisibility(View.GONE);
            frameLayout_noProducts.setVisibility(View.VISIBLE);

        }

    }

    void getSalesId(int saleStatus) {

        getLocation();

        SharedPreferences preferences = this.getSharedPreferences(Constants.BA_ATTENDANCE_ID, MODE_PRIVATE);
        int id = preferences.getInt(Constants.KEY_BA_ATTENDANCE_ID, 0);

        final LoginResponse loginResponse = beemDatabase.getUserDetail();


        String currentString = age;
        String[] separated = currentString.split("-");

        try {

            calculatedAge = Integer.parseInt(separated[0]);
            calculatedAge += Integer.parseInt(separated[1]);

            calculatedAge = calculatedAge / 2;

        }catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Age Format  " + e.getLocalizedMessage().toString());
        }

        if (networkUtils.isNetworkConnected()) {

//            int parsedContact = Integer.parseInt(contact);

            progressShow();

            networkUtils.sendSaleDetail(cusName, contact, email, gender, calculatedAge, cBrand, pBrand, saleStatus,
                    loginResponse.getUserId(), loginResponse.getName(), "Manager", "Karachi", Integer.valueOf(loginResponse.getStoreId()), new LoginInterface() {
                        @Override
                        public void success(Response<LoginResponse> loginResponseBody) {
                            Log.i(LOG_TAG, "getSalesId Status" + loginResponseBody.body().getStatus());
                            if (loginResponseBody.body().getStatus() == 1) {

                                progressHide();
                                sale_id = loginResponseBody.body().getSales_id();

                                insertSalesDetails(cusName, contact, email, gender, calculatedAge, cBrand, pBrand, saleStatus,
                                        loginResponse.getUserId(), loginResponse.getName(), "Manager", "Karachi", Integer.valueOf(loginResponse.getStoreId()),
                                        loginResponseBody.body().getSales_id(), loginResponseBody.body().getStatus(), 1);

//                                if (saleStatus == 0) {
//                                    finish();
//                                }

                                //Bug REsolving Order Offlin Problem
                                if (saleStatus == 1) {
                                    sendOrder(sale_id);
                                } else {
                                    finish();
                                }
                                //End

                            }
                        }

                        @Override
                        public void failed(String error) {
                            progressHide();

                            insertSalesDetails(cusName, contact, email, gender, calculatedAge, cBrand, pBrand, saleStatus,
                                    loginResponse.getUserId(), loginResponse.getName(), "Manager", "Karachi", Integer.valueOf(loginResponse.getStoreId()),
                                    0, 0, 0);

//                            if (saleStatus == 0) {
//                                finish();
//                            }

                            //Bug REsolving Order Offlin Problem
                            if (saleStatus == 1) {
                                sendOrder(0);
                            } else {
                                finish();
                            }
                            //End

                            Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

//            progressHide();
//
//            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OrderActivity.this);
//            alertBuilder.setTitle("Network")
//                    .setMessage("Please Check your internet connection")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                            insertSalesDetails(cusName, contact, email, gender, calculatedAge, cBrand, pBrand, 1,
                                    loginResponse.getUserId(), loginResponse.getName(), "Manager", "Karachi", Integer.valueOf(loginResponse.getStoreId()),
                                    0, 0, 0);

//            if (saleStatus == 0) {
//                finish();
//            }

            //Bug REsolving Order Offlin Problem
            if (saleStatus == 1 ){
                sendOrder(0);
            } else {
                finish();
            }
            //End
//                        }
//                    });
//
//            alertBuilder.show();
        }
    }


    void insertSalesDetails (String cusName, String contact, String email, String gender, Integer age, String  cBrand,
                             String pBrand, Integer saleStatus, Integer empId, String empName, String designation, String city,
                             Integer location, int salesId, int saleResponseStatus, int syncStatus) {

        SaleApiResponseTableRealm saleApiResponseTableRealm = new SaleApiResponseTableRealm();

        saleApiResponseTableRealm.setCusName(cusName);
        saleApiResponseTableRealm.setContact(contact);
        saleApiResponseTableRealm.setEmail(email);
        saleApiResponseTableRealm.setGender(gender);
        saleApiResponseTableRealm.setAge(age);
        saleApiResponseTableRealm.setcBrand(cBrand);
        saleApiResponseTableRealm.setpBrand(pBrand);
        saleApiResponseTableRealm.setSaleStatus(saleStatus);
        saleApiResponseTableRealm.setEmpId(empId);
        saleApiResponseTableRealm.setEmpName(empName);
        saleApiResponseTableRealm.setDesignation(designation);
        saleApiResponseTableRealm.setCity(city);
        saleApiResponseTableRealm.setLocation(location);
        saleApiResponseTableRealm.setSyncStatus(syncStatus);

        nextIdSales = realmCRUD.insertSalesDetails(saleApiResponseTableRealm, salesId, saleResponseStatus);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}