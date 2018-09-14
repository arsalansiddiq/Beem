package com.example.arsalansiddiq.beem.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.azimolabs.maskformatter.MaskFormatter;
import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.SKUCategoryInterface;
import com.example.arsalansiddiq.beem.models.databasemodels.SalesAndNoSales;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

public class SalesActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    //    private List<String> skuCategoryList;
    private SalesSKUArrayResponse skuCategory = null;

    private TextView txtView_recordsSent_count = null;
    private TextView txtView_recordsPending_count = null;

    private String[] brandList = null;

    private Spinner spinner_gender, spinner_age, spinner_pBrand, spinner_cBrand = null;

    String name, email, gender, age,  pBrand = null;

    String contact = null;
    private Integer cBrand;

    private EditText edtText_name, edtText_contact, edtText_email;

    private ArrayAdapter<CharSequence> adapterGender, adapterAge, adapterBrand;
    private Button btn_next;

    private Response<SalesObjectResponse> salesObjectResponse = null;

    private SalesAndNoSales salesAndNoSales;

    private RealmCRUD realmCRUD;

    private List<SalesSKUArrayResponse> salesSKUArrayResponseList;

    private List<SalesSKUArrayResponse> salesSKUArrayResponseListDuplicateComparator;

    private String valueBrandId;


    //    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        txtView_recordsSent_count = findViewById(R.id.txtView_recordsSent_count);
        txtView_recordsPending_count = findViewById(R.id.txtView_recordsPending_count);

        realmCRUD = new RealmCRUD(this);

        SharedPreferences preferences = this.getSharedPreferences(Constants.SALE_STATUS, MODE_PRIVATE);
        int id = preferences.getInt(Constants.KEY_SALE_STATUS, 0);

//        txtView_recordsSent_count.setText(String.valueOf(id));

        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_age = findViewById(R.id.spinner_age);
        spinner_pBrand = findViewById(R.id.spinner_pBrand);
        spinner_cBrand = findViewById(R.id.spinner_cBrand);
        btn_next = findViewById(R.id.btn_next);

        edtText_name = findViewById(R.id.edtText_name);
        edtText_contact = findViewById(R.id.edtText_contact);
        edtText_email = findViewById(R.id.edtText_email);


        spinner_gender.setOnItemSelectedListener(this);
        spinner_age.setOnItemSelectedListener(this);
        spinner_pBrand.setOnItemSelectedListener(this);
        spinner_cBrand.setOnItemSelectedListener(this);

        setupEditTextWithDashes(R.id.edtText_contact, Constants.NUMBERS_DASHED_MASK);

//        genderList = new  {"Male", "Female"};
//        skuCategory = new ArrayList<SalesSKUArrayResponse>();

        getBrandDetails();

        salesAndNoSales = realmCRUD.getSaleAndNoSales();

        if (salesAndNoSales != null) {
            txtView_recordsSent_count.setText("" + salesAndNoSales.getTotal_sales());
            txtView_recordsPending_count.setText("" + salesAndNoSales.getTotal_nosales());
        } else {
            txtView_recordsSent_count.setText("" + 0);
            txtView_recordsPending_count.setText("" + 0);
        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getBrandDetails() {

        SharedPreferences preferences = this.getSharedPreferences(Constants.USER_BRAND, MODE_PRIVATE);
        String brand = preferences.getString(Constants.KEY_USER_BRAND, null);

        if (!TextUtils.isEmpty(brand)) {

            LoginResponse loginResponse = realmCRUD.getLoginInformationDetails();

            boolean checkBrandsExists = realmCRUD.checkCurrentUserHasSavedBrands(loginResponse.getUserId());

            if (checkBrandsExists) {
                callRelmFetcherForBrands(loginResponse.getUserId());
            } else {

                NetworkUtils networkUtils = new NetworkUtils(SalesActivity.this);

                if (networkUtils.isNetworkConnected()) {
                    progressShow();
                    networkUtils.getBrandsofUser(loginResponse.getBrand(), new SKUCategoryInterface() {

                        @Override
                        public void success(Response<SalesObjectResponse> response) {
                            if (response.body().getStatus() == 1) {

                                progressHide();

                                salesObjectResponse = response;
                                List<SalesSKUArrayResponse> salesSKUArrayResponseList = response.body().getSku();

                                for (int i = 0; i < response.body().getSku().size(); i++) {

                                    realmCRUD.insertBrandsCategoryDetails(salesSKUArrayResponseList.get(i), loginResponse.getUserId());

//                                //Offline Checking
//                                skuCategory = response.body().getSku().get(i);
////                            skuCategoryList.add(skuCategory.getSKUCaategory());
//                                brandList[i + 1] = skuCategory.getSKUCaategory();

                                }

                                callRelmFetcherForBrands(loginResponse.getUserId());

//                            setAdatpers(brandList);

                                Log.i("SKU", String.valueOf(response.body().getStatus()));
                                Log.i("SKUCat", String.valueOf(brandList));
                            }
                        }

                        @Override
                        public void failed(String error) {
                            progressHide();
                            Log.i("SKU", error);
                        }
                    });
                } else {

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SalesActivity.this);
                    alertBuilder.setTitle("Network")
                            .setMessage("Please connect with internet to get brands")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getBrandDetails();
//                            insertorAddOrder(sales_id, isConnected);
                                }
                            });

                    alertBuilder.show();
                }
            }
        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.N)
    private void callRelmFetcherForBrands(Integer userId) {

        salesSKUArrayResponseListDuplicateComparator = new ArrayList<>();

        salesSKUArrayResponseList = realmCRUD.getUserBrandsSKUCategory(userId);

        Map<Integer, SalesSKUArrayResponse> map = new LinkedHashMap<>();

        for (SalesSKUArrayResponse saleArray : salesSKUArrayResponseList) {
            map.put(saleArray.getCateId(), saleArray);
        }

        salesSKUArrayResponseListDuplicateComparator.addAll(map.values());

//        List<SalesSKUArrayResponse> salesSKUArrayResponseList = realmCRUD.getUserBrandsSKUCategory(userId);

//        salesSKUArrayResponseListDuplicateComparator =
//                salesSKUArrayResponseList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(
//                        comparingInt(SalesSKUArrayResponse::getCateId))), ArrayList::new));

        brandList = new String[salesSKUArrayResponseListDuplicateComparator.size() + 1];
        brandList[0] = "Select Brand";


        for (int i = 0; i < salesSKUArrayResponseListDuplicateComparator.size(); i++) {

            //Offline Checking
//            skuCategory = salesSKUArrayResponseList.get(i);
//            skuCategoryList.add(skuCategory.getSKUCaategory());
//             brandList[i + 1] = skuCategory.getSKUCaategory();

            brandList[i + 1] = salesSKUArrayResponseListDuplicateComparator.get(i).getSKUCaategory();
//             brandList[i + 1] = salesSKUArrayResponseList.get(i).getSKUCaategory();

        }

        setAdatpers(brandList);

    }

    private void setAdatpers(String[] brandList) {

        adapterGender = ArrayAdapter.createFromResource(SalesActivity.this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        adapterAge = ArrayAdapter.createFromResource(SalesActivity.this, R.array.age_array, android.R.layout.simple_spinner_item);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age.setAdapter(adapterAge);

        adapterBrand = new ArrayAdapter<CharSequence>(SalesActivity.this, android.R.layout.simple_spinner_item, brandList);
        adapterBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cBrand.setAdapter(adapterBrand);
        spinner_pBrand.setAdapter(adapterBrand);
    }

    public void onNext(View v) {

        name = edtText_name.getText().toString();
        edtText_email.setTextColor(Color.parseColor("#000000"));
        edtText_contact.setTextColor(Color.parseColor("#000000"));

        if (TextUtils.isEmpty(name) || spinner_gender.getSelectedItemPosition() == 0
                || spinner_age.getSelectedItemPosition() == 0 ||spinner_pBrand.getSelectedItemPosition() == 0
                || spinner_cBrand.getSelectedItemPosition() == 0) {

            Toast.makeText(SalesActivity.this, "please complete information", Toast.LENGTH_SHORT).show();

        } else {

            valueBrandId = String.valueOf(salesSKUArrayResponseListDuplicateComparator.get(cBrand - 1).getCateId());
//            valueBrandId = String.valueOf(salesSKUArrayResponseList.get(cBrand - 1).getBrandId());

            if (!TextUtils.isEmpty(edtText_name.getText().toString()) &&
                    (TextUtils.isEmpty(edtText_email.getText().toString()) && TextUtils.isEmpty(edtText_contact.getText().toString()))) {
                Intent intent = new Intent(SalesActivity.this, OrderActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("contact", "0");
                intent.putExtra("email", "");
                intent.putExtra("gender", gender);
                intent.putExtra("age", age);
                intent.putExtra("pBrand", pBrand);
                intent.putExtra("cBrand", valueBrandId);
//                intent.putExtra("cBrand", cBrand);
                startActivity(intent);
            } else if ((!TextUtils.isEmpty(edtText_name.getText().toString())) &&
                    (!TextUtils.isEmpty(edtText_email.getText().toString()) && TextUtils.isEmpty(edtText_contact.getText().toString()))) {
//                    || (TextUtils.isEmpty(edtText_email.getText().toString()) && TextUtils.isEmpty(edtText_contact.getText().toString()))) {

                if (isEmailValid(edtText_email.getText().toString())) {
                    email = edtText_email.getText().toString();
                    Intent intent = new Intent(SalesActivity.this, OrderActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("contact", "0");
                    intent.putExtra("email", email);
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", age);
                    intent.putExtra("pBrand", pBrand);
                    intent.putExtra("cBrand", valueBrandId);
//                    intent.putExtra("cBrand", cBrand);
                    startActivity(intent);
                } else {
                    edtText_email.setTextColor(Color.parseColor("#ff0000"));
                    Toast.makeText(this, " please enter valid email", Toast.LENGTH_SHORT).show();
                }

            } else if ((!TextUtils.isEmpty(edtText_name.getText().toString())) &&
                    (TextUtils.isEmpty(edtText_email.getText().toString()) && !TextUtils.isEmpty(edtText_contact.getText().toString()))) {
                if (edtText_contact.getText().length() != 13) {
                    edtText_contact.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    contact = edtText_contact.getText().toString();
                    contact = contact.replace(" ", "");

                    Intent intent = new Intent(SalesActivity.this, OrderActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("contact", contact);
                    intent.putExtra("email", "");
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", age);
                    intent.putExtra("pBrand", pBrand);
                    intent.putExtra("cBrand", valueBrandId);
//                    intent.putExtra("cBrand", cBrand);
                    startActivity(intent);
                }
            } else if ((!TextUtils.isEmpty(edtText_name.getText().toString())) &&
                    (!TextUtils.isEmpty(edtText_email.getText().toString()) && !TextUtils.isEmpty(edtText_contact.getText().toString()))) {
                if (edtText_contact.getText().length() != 13 && !isEmailValid(edtText_email.getText().toString())) {
                    edtText_email.setTextColor(Color.parseColor("#ff0000"));
                    edtText_contact.setTextColor(Color.parseColor("#ff0000"));
                } else if (edtText_contact.getText().length() == 13 && !isEmailValid(edtText_email.getText().toString())) {

                    edtText_email.setTextColor(Color.parseColor("#ff0000"));
                } else if (edtText_contact.getText().length() != 13 && isEmailValid(edtText_email.getText().toString())) {
                    edtText_contact.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    email = edtText_email.getText().toString();
                    contact = edtText_contact.getText().toString();
                    contact = contact.replace(" ", "");

                    Intent intent = new Intent(SalesActivity.this, OrderActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("contact", contact);
                    intent.putExtra("email", "");
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", age);
                    intent.putExtra("pBrand", pBrand);
                    intent.putExtra("cBrand", valueBrandId);
//                    intent.putExtra("cBrand", cBrand);
//                    intent.putExtra("cBrand", cBrand);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "please insert valid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()) {
            case R.id.spinner_gender:
//                if (spinner_gender.getSelectedItemPosition() == 0) {
//                    Toast.makeText(SalesActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
//                } else {
                gender = spinner_gender.getSelectedItem().toString();
//                }
                break;
            case R.id.spinner_age:
//                if (spinner_age.getSelectedItemPosition() == 0) {
//                    Toast.makeText(SalesActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
//                } else {
                age = spinner_age.getSelectedItem().toString();
//                }
                break;
            case R.id.spinner_pBrand:
//                if (spinner_pBrand.getSelectedItemPosition() == 0) {
//                    Toast.makeText(SalesActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
//                } else {
                pBrand = spinner_pBrand.getSelectedItem().toString();
//                }
                break;
            case R.id.spinner_cBrand:
//                if (spinner_cBrand.getSelectedItemPosition() == 0) {
//                    Toast.makeText(SalesActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
//                } else {
                cBrand = spinner_cBrand.getSelectedItemPosition();
//                    cBrand = spinner_cBrand.getSelectedItem().toString();
//                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(SalesActivity.this, NavigationDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
        }
        return false;
    }


    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    //Number Validation
    private void setupEditTextWithDashes(int layoutId, String mask) {
        EditText field = (EditText) findViewById(layoutId);
        field.addTextChangedListener(new MaskFormatter(mask, field));
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner_gender.setSelection(spinner_gender.getSelectedItemPosition());
        spinner_age.setSelection(spinner_age.getSelectedItemPosition());
        spinner_cBrand.setSelection(spinner_cBrand.getSelectedItemPosition());
        spinner_pBrand.setSelection(spinner_pBrand.getSelectedItemPosition());

        edtText_name.setText("");


        salesAndNoSales = realmCRUD.getSaleAndNoSales();

        if (salesAndNoSales != null) {
            txtView_recordsSent_count.setText("" + salesAndNoSales.getTotal_sales());
            txtView_recordsPending_count.setText("" + salesAndNoSales.getTotal_nosales());
        } else {
            txtView_recordsSent_count.setText("" + 0);
            txtView_recordsPending_count.setText("" + 0);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        edtText_name.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        edtText_name.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        edtText_name.setText("");
    }


}