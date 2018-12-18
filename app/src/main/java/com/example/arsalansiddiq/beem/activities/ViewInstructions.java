package com.example.arsalansiddiq.beem.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.viewinstruction.ViewInstructionResponseModel;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ViewInstructions extends BaseActivity implements UpdateCallback{

//    @BindView(R.id.constraintLayout_viewInstuction)
//    ConstraintLayout constraintLayout_viewInstuction;

    @BindView(R.id.imgView_viewInstruction)
    ImageView imgView_viewInstruction;

    private NetworkUtils networkUtils;
    private RealmCRUD realmCRUD;
    private LoginResponse loginResponseRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_instructions);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        initProgressBar();

        networkUtils = new NetworkUtils(this);
        realmCRUD = new RealmCRUD(this);
        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        progressShow();
        if (networkUtils.isNetworkConnected()) {
            if (getRandomTaskStatus()) {
                networkUtils.getViewInstruction(Integer.parseInt(loginResponseRealm.getBrand()),
                        0, this);
            } else {
                networkUtils.getViewInstruction(Integer.parseInt(loginResponseRealm.getBrand()),
                        getCount(Constants.SHOP_ID), this);
            }
        } else {
            Toast.makeText(this, "please connect your internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void UpdateSuccess(Response response) {
        ViewInstructionResponseModel viewInstructionResponseModel = (ViewInstructionResponseModel) response.body();

        Picasso.get().load(viewInstructionResponseModel.getData().getImagePath()).into(imgView_viewInstruction);

        progressHide();
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        progressHide();
    }

    int getCount(String key) {
        SharedPreferences prefs = getSharedPreferences(Constants.BEEM_PREFERENCE_COUNT, MODE_PRIVATE);
        int coutn = prefs.getInt(key, 0);
        return coutn;
    }

    boolean getRandomTaskStatus () {
        SharedPreferences prefs = getSharedPreferences(Constants.BEEM_PREFERENCE_COUNT, MODE_PRIVATE);
        boolean isStatus = prefs.getBoolean(Constants.RANDOM_TASK, false);
        return isStatus;
    }
}
