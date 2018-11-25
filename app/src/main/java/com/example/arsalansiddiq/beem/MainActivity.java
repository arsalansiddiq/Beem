package com.example.arsalansiddiq.beem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.arsalansiddiq.beem.databases.BeemAppDatabaseInstance;
import com.example.arsalansiddiq.beem.databases.room.BeemAppDatabase;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

//public class MainActivity extends AppCompatActivity implements LocationListener {
public class MainActivity extends AppCompatActivity {

//    private Spinner spinner_saleStatus = null;
//    private NetworkUtils networkUtils = null;
//    String cusName; Long contact; String email; String gender; String age; String cBrand = null;
//    String pBrand = null;
//    Integer saleStatus = 0;
//    private LocationManager locationManager = null;
//    private float latitude, longitude;
//    private BeemDatabase beemDatabase = null;
//
//    private ListView listView_order = null;
//    private Intent intent = null;
//    private FrameLayout frameLayout_noProducts = null;
//    private View view = null;
//    private EditText edtText_loose = null;
//    private EditText edtText_carton = null;
//    private TextView txtView_name = null;
//    private LinearLayout linearLayout_bottom;
//
//    private List<HolderListModel> holderListModelList;
//
//    private List<SalesSKUArrayResponse> salesSKUArrayResponseArrayList = null;
//
//    CustomAlertDialog customAlertDialog;
//
//    int doubleQuantity = 0;
//    private AppUtils appUtils;
BeemAppDatabase beemAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        beemAppDatabase = new BeemAppDatabaseInstance(this).getBeemDatabaseInstance();

//        SalesStatus salesStatus = new SalesStatus();
//        salesStatus.setTotal_sales(1);
//        salesStatus.setTotal_nosales(0);

//        beemAppDatabase.daoBeemDatabase().insertSaleStatus(salesStatus);

//        beemAppDatabase.daoBeemDatabase().getAll();

        performAsync();
    }

    public void performAsync() {


        NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);

//        networkUtils.getBrands("Brite", new SKUCategoryInterface())




//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//        SalesStatus salesStatus = new SalesStatus();
//        salesStatus.setTotal_sales(1);
//        salesStatus.setTotal_nosales(0);
//                beemAppDatabase.daoBeemDatabase().insertSaleStatus(salesStatus);
//
//                List<SalesStatus> salesStatuses = beemAppDatabase.daoBeemDatabase().getAll();
//                salesStatuses.get(1).getTotal_sales();
//                return null;
//            }
//        }.execute();
    }

//        beemDatabase = new BeemDatabase(this);
//        beemDatabase.getReadableDatabase();
//
//        customAlertDialog = new CustomAlertDialog(this);
//        networkUtils = new NetworkUtils(this);
//        appUtils = new AppUtils(this);
//
//        spinner_saleStatus = findViewById(R.id.spinner_saleStatus);
//
//        listView_order = findViewById(R.id.listView_order);
//        linearLayout_bottom = findViewById(R.id.linearLayout_bottom);
//
//        listView_order.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//        listView_order.setItemsCanFocus(true);
//
//        frameLayout_noProducts = findViewById(R.id.frameLayout_noProducts);
//
//        holderListModelList = new ArrayList<>();
//
//        if (getIntent().getExtras() != null) intent = getIntent();
//        cusName = intent.getStringExtra("name");
//        contact = intent.getExtras().getLong("contact");
//        email = intent.getStringExtra("email");
//        gender = intent.getStringExtra("gender");
//        age = intent.getStringExtra("age");
//        pBrand = intent.getStringExtra("pBrand");
//        cBrand = intent.getStringExtra("cBrand");
//
//        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(this, R.array.saleStatus_array, android.R.layout.simple_spinner_item);
//        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_saleStatus.setAdapter(adapterGender);
//        spinner_saleStatus.setSelection(2);
//
//        spinner_saleStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position == 2) {
////                    getSalesId();
//                    linearLayout_bottom.setVisibility(View.VISIBLE);
//                } else if (position == 1){
//                    linearLayout_bottom.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        frameLayout_noProducts.setVisibility(View.GONE);
//
//        if (spinner_saleStatus.getSelectedItemPosition() == 2) {
//            linearLayout_bottom.setVisibility(View.VISIBLE);
//            getSalesId();
//            getBrandItems();
//        }

//    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void onSubmit(View view) {
//
//        getLocation();
//        getSelectedItemAndPrice();
//
//    }
//
//
//    void sendOrder(final int sales_id) {
//
//
//        int j = 0;
//
//        Log.i("dates", appUtils.getDate());
//
//        for (int i = 0; i < holderListModelList.size(); i++) {
//
//            HolderListModel holderListModel = holderListModelList.get(i);
//
//            final int finalI = i;
//
//            networkUtils.sendOrderDetail(1, sales_id, appUtils.getDate(), holderListModel.getBrand()
//                    , holderListModel.getSkuCategory() , holderListModel.getSKU(), holderListModel.getSaleType(), holderListModel.getNoItem(),
//                    holderListModel.getPrice(), holderListModel.getsAmount(), new SampleInterface() {
//
//                        @Override
//                        public void success(LoginResponse loginResponse) {
//                            final BeemPreferences beemPreferences = new BeemPreferences(MainActivity.this);
//                            beemPreferences.initialize_and_createPreferences_forStatus(loginResponse.getSyncStatus());
//
//                        }
//
//                        @Override
//                        public void failed(String error) {
//
//                        }
//                    });
//        }
//
//        intent = new Intent(MainActivity.this, SalesActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        latitude = (float) location.getLatitude();
//        longitude = (float) location.getLongitude();
//
//        Log.i("LocationOrder", latitude + "  " + longitude);
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
//            return;
//        } else {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MainActivity.this);
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch(keyCode)
//        {
//            case KeyEvent.KEYCODE_BACK:
//
//                intent = new Intent(MainActivity.this, SalesActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//
//                return true;
//        }
//        return false;
//    }
//
//    private void getBrandItems() {
//
//
//        NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
//
//        if (networkUtils.isNetworkConnected()) {
//            networkUtils.getBrandsofUser(cBrand, new SKUCategoryInterface() {
//
//                @Override
//                public void success(Response<SalesObjectResponse> response) {
//                    if (response.body().getSyncStatus() == 1) {
//
//                        salesSKUArrayResponseArrayList = response.body().getSku();
//                        CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, 0, salesSKUArrayResponseArrayList);
//                        listView_order.setAdapter(adapter);
//
//                    } else {
//                        Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void failed(String error) {
//                    Log.i("SKU", error);
//                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//                    listView_order.setVisibility(View.GONE);
//
//                    frameLayout_noProducts.setVisibility(View.VISIBLE);
//                }
//            });
//        } else {
//            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_SHORT).show();
//            frameLayout_noProducts.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//
//    void getSelectedItemAndPrice() {
//
//        SalesSKUArrayResponse skuArrayResponse;
//
//
//        beemDatabase.removeSelectedItemTableRaws();
//
//        int listLength = listView_order.getChildCount();
//
//        int checkSelectionExist = 0;
//        doubleQuantity = 0;
//
//        int looseItem, cartonItem;
//
//        if  (listLength > 0) {
//
//            for (int i = 0; i < listLength; i++) {
//
//                looseItem = 0; cartonItem = 0;
//
//                view = listView_order.getChildAt(i);
//
//                txtView_name = (TextView) view.findViewById(R.id.txtView_name);
//
//                edtText_loose = (EditText) view.findViewById(R.id.edtText_loose);
//                edtText_carton = (EditText) view.findViewById(R.id.edtText_carton);
//
//                String looseText = edtText_loose.getText().toString().trim();
//                String cartonText = edtText_carton.getText().toString().trim();
//
//                if (looseText.equals("")  &&
//                        cartonText.equals("")) {
//                } else if (looseText.length() > 0 &&
//                        cartonText.length() > 0){
//                    doubleQuantity += 1;
//                } else if (looseText.length() > 0 &&
//                        cartonText.equals("")){
//                    looseItem = Integer.parseInt(looseText);
//                } else if (looseText.equals("") &&
//                        cartonText.length() > 0){
//                    cartonItem = Integer.parseInt(cartonText);
//                }
//
//                if (looseItem == 0 && cartonItem == 0 && doubleQuantity == 0) {
//                    checkSelectionExist++;
//                }
//
//                if (looseItem != 0 || cartonItem != 0) {
//
//                    int saleTypes, totalItem;
//                    if (cartonItem != 0) {
//                        saleTypes = 1;
//                        totalItem = cartonItem;
//                    } else {
//                        saleTypes = 0;
//                        totalItem = looseItem;
//                    }
//
//                    skuArrayResponse = salesSKUArrayResponseArrayList.get(i);
//                    LoginResponse loginResponse = beemDatabase.getUserDetail();
//
//                    HolderListModel holder = new HolderListModel(Integer.valueOf(loginResponse.getStoreId()), saleStatus, appUtils.getDate(), skuArrayResponse.getBrand(),
//                            0, skuArrayResponse.getCateId(), saleTypes, totalItem, skuArrayResponse.getPrice(),
//                            skuArrayResponse.getPrice() * totalItem);
//
//                    holderListModelList.add(holder);
//
//                }
//
//            }
//
//            if (checkSelectionExist == 0) {
//
//                if (holderListModelList.size() > 0 && doubleQuantity == 0) {
//                    sendOrder(saleStatus);
//                } else {
//                    holderListModelList.clear();
//                    customAlertDialog.hideDialog();
//                    customAlertDialog.showDialog(false);
//                }
//            } else {
//                customAlertDialog.hideDialog();
//                customAlertDialog.showDialog(true);
//            }
//        } else {
//            listView_order.setVisibility(View.GONE);
//            frameLayout_noProducts.setVisibility(View.VISIBLE);
//
//        }
//
//    }
//
//    void getSalesId() {
//
//        getLocation();
//
//        SharedPreferences preferences = this.getSharedPreferences(Constants.BA_ATTENDANCE_ID, MODE_PRIVATE);
//        int id = preferences.getInt(Constants.KEY_BA_ATTENDANCE_ID, 0);
//
//        LoginResponse loginResponse = beemDatabase.getUserDetail();
//
//        if (networkUtils.isNetworkConnected()) {
//
//            String currentString = age;
//            String[] separated = currentString.split("-");
//
//            int calculatedAge = Integer.parseInt(separated[0]);
//            calculatedAge += Integer.parseInt(separated[1]);
//
//            calculatedAge = calculatedAge / 2;
//
//            networkUtils.sendSaleDetail(cusName, String.valueOf(contact), email, gender, calculatedAge, cBrand, pBrand, saleStatus,
//                    id, loginResponse.getName(), "Manager", "Karachi", (int) latitude, new LoginInterface() {
//                        @Override
//                        public void success(Response<LoginResponse> loginResponse) {
//                            if (loginResponse.body().getSyncStatus() == 1) {
//                                saleStatus = loginResponse.body().getSales_id();
//                            }
//                        }
//
//                        @Override
//                        public void failed(String error) {
//                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//        }
//    }

}
