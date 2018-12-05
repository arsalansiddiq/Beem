package com.example.arsalansiddiq.beem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.adapters.merchant.CustomListAdapterSKUs;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.ListViewModelCheckMerchant;
import com.example.arsalansiddiq.beem.models.requestmodels.merchant.SKUMerchantRequestModel;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.competitionsku.MerchantSKU;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class PriceUpdateActivity extends AppCompatActivity implements UpdateCallback{

    private NetworkUtils networkUtils;
    private List<MerchantSKU> merchantSKUList= null;
    private LoginResponse loginResponse;
    private RealmCRUD realmCRUD;
    public static ArrayList<ListViewModelCheckMerchant> listViewModelCheckMerchantArrayList;

    @BindView(R.id.listView_merchant)
    ListView listView_merchant;
    @BindView(R.id.frameLayout_noProductsMechant)
    FrameLayout frameLayout_noProductsMechant;
    @BindView(R.id.txtView_noProductsMerchant)
    TextView txtView_noProductsMerchant;
    @BindView(R.id.btn_submitMerchant)
    Button btn_submitMerchant;

    public static ArrayList<ListViewModelCheckMerchant> listViewModelCheckMerchants;

    private Intent intent = null;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_update);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        listView_merchant.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listView_merchant.setItemsCanFocus(true);

        if (getIntent().getExtras() != null) intent = getIntent();
        tag = intent.getStringExtra("tag");

        frameLayout_noProductsMechant.setVisibility(View.GONE);

        listViewModelCheckMerchantArrayList = new ArrayList<>();
        realmCRUD = new RealmCRUD();
        loginResponse = realmCRUD.getLoginInformationDetails();

        networkUtils = new NetworkUtils(this);

        if (networkUtils.isNetworkConnected()){
            networkUtils.getMerchantSKU(Integer.parseInt(loginResponse.getBrand()), this);
        }

        btn_submitMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedItemAndPriceToServer();
            }
        });
    }

//    private void storeRecordsOnServer() {
//
//
//
//        SKUMerchantRequestModel skuMerchantRequestModel = null;
//        networkUtils.storeSKUPrice(skuMerchantRequestModel, new BaseCallbackInterface() {
//            @Override
//            public void success(Response response) {
//
//            }
//
//            @Override
//            public void failure(String error) {
//
//            }
//        });
//    }

    @Override
    public void UpdateSuccess(Response response) {

        MerchantSKU merchantSKUS = (MerchantSKU) response.body();

        CustomListAdapterSKUs adapter = new
                CustomListAdapterSKUs(PriceUpdateActivity.this, 0,
                merchantSKUS.getData(), tag);

        for (int i = 0; i < merchantSKUS.getData().size(); i++) {
            ListViewModelCheckMerchant listViewModelCheckMerchant = new ListViewModelCheckMerchant();
            listViewModelCheckMerchant.setId(merchantSKUS.getData().get(i).getId());

            if  (tag.equals("price") || tag.equals("priceCompetition")) {
                listViewModelCheckMerchant.setedtText_priceMerchant("");
            } else if (tag.equals("stock")) {
                listViewModelCheckMerchant.setedtText_stockMerchant("");
            }

            listViewModelCheckMerchantArrayList.add(listViewModelCheckMerchant);

        }
        listView_merchant.setAdapter(adapter);
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {

    }

    void getSelectedItemAndPriceToServer() {

        int listLength = 0;

        List<SKUMerchantRequestModel> skuMerchantRequestModelArrayList = new ArrayList<>();

        SKUMerchantRequestModel skuMerchantRequestModel = new SKUMerchantRequestModel();

        if (listViewModelCheckMerchantArrayList != null || listViewModelCheckMerchantArrayList.size() != 0) {
            listLength = listViewModelCheckMerchantArrayList.size();
        }

        int checkSelectionExist = 0;

        Long priceMerchant;

        if (listLength > 0) {

            for (int i = 0; i < listLength; i++) {

                priceMerchant = null;

                String priceStockEdtText = null;
                if  (tag.equals("price") || tag.equals("priceCompetition")) {
                    priceStockEdtText = listViewModelCheckMerchantArrayList.get(i).getedtText_priceMerchant().toString();
                } else if (tag.equals("stock")) {
                    priceStockEdtText = listViewModelCheckMerchantArrayList.get(i).getedtText_stockMerchant().toString();
                }

                if (priceStockEdtText.equals("")) {

                } else if (priceStockEdtText.length() > 0) {

                    skuMerchantRequestModel.setUserId(Long.valueOf(loginResponse.getUserId()));
                    skuMerchantRequestModel.setBrandId(Long.valueOf(loginResponse.getBrand()));
                    skuMerchantRequestModel.setTaskId(Long.valueOf(getCount(Constants.TASK_ID)));
                    skuMerchantRequestModel.setShopId(Long.valueOf(getCount(Constants.SHOP_ID)));
                    skuMerchantRequestModel.setSkuId(Long.valueOf(listViewModelCheckMerchantArrayList.get(i).getId()));

                    if  (tag.equals("price") || tag.equals("priceCompetition")) {
                        priceMerchant = Long.valueOf(priceStockEdtText);
                        skuMerchantRequestModel.setPrice(priceMerchant);
                    } else if (tag.equals("stock")) {
                        priceMerchant = Long.valueOf(priceStockEdtText);
                        skuMerchantRequestModel.setStock(priceMerchant);
                    }

                    if  (tag.equals("priceCompetition")) {
                        skuMerchantRequestModel.setCompetition(Long.valueOf(1));
                    } else {
                        skuMerchantRequestModel.setCompetition(Long.valueOf(0));
                    }

                    skuMerchantRequestModelArrayList.add(skuMerchantRequestModel);

                    checkSelectionExist++;

                }

            }

            if (checkSelectionExist > 0) {
                for (int j = 0; j < skuMerchantRequestModelArrayList.size(); j++) {
                    if (networkUtils.isNetworkConnected()) {
                        skuMerchantRequestModel = skuMerchantRequestModelArrayList.get(j);
                        networkUtils.storeSKUPrice(skuMerchantRequestModel, new BaseCallbackInterface() {
                            @Override
                            public void success(Response response) {
                            }

                            @Override
                            public void failure(String error) {
                            }
                        });
                    }

                    int k = j + 1;
                    if (k == skuMerchantRequestModelArrayList.size()) {
                        finish();
                    }
                }
            } else {
                Toast.makeText(this, "please enter some quantity!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    int getCount(String key) {
            SharedPreferences prefs = getSharedPreferences(Constants.BEEM_PREFERENCE_COUNT, MODE_PRIVATE);
            int coutn = prefs.getInt(key, 0);
            return coutn;
        }
}
