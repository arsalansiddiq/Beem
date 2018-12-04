package com.example.arsalansiddiq.beem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.adapters.merchant.CustomListAdapterSKUs;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.models.ListViewModelCheck;
import com.example.arsalansiddiq.beem.models.ListViewModelCheckMerchant;
import com.example.arsalansiddiq.beem.models.requestmodels.merchant.SKUMerchantRequestModel;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.competitionsku.MerchantSKU;
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
    public static ArrayList<ListViewModelCheck> listViewModelCheckArrayList;

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

        listViewModelCheckArrayList = new ArrayList<>();

        if (getIntent().getExtras() != null) intent = getIntent();
        tag = intent.getStringExtra("tag");

        frameLayout_noProductsMechant.setVisibility(View.GONE);

        listViewModelCheckArrayList = new ArrayList<>();
        realmCRUD = new RealmCRUD();
        loginResponse = realmCRUD.getLoginInformationDetails();

        networkUtils = new NetworkUtils(this);

        if (networkUtils.isNetworkConnected()){
            networkUtils.getMerchantSKU(Integer.parseInt(loginResponse.getBrand()), this);
        }

        btn_submitMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SKUMerchantRequestModel skuMerchantRequestModel = null;
                networkUtils.storeSKUPrice(skuMerchantRequestModel, PriceUpdateActivity.this);
            }
        });
    }

    @Override
    public void UpdateSuccess(Response response) {

        MerchantSKU merchantSKUS = (MerchantSKU) response.body();

        CustomListAdapterSKUs adapter = new
                CustomListAdapterSKUs(PriceUpdateActivity.this, 0,
                merchantSKUS.getData(), tag);

        listView_merchant.setAdapter(adapter);
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {

    }
}
