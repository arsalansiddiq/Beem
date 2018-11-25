package com.example.arsalansiddiq.beem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.compulsory.CompulsoryStepsResponseModel;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import retrofit2.Response;

public class MerchantActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_takeStorePicture, btn_submitFeedback, btn_takeFrontChillersPicture,
            btn_viewInstructions, btn_takeAfterChillersPicture, btn_takeCompetionsPicture,
            btn_updateStocks, btn_updateStockPrices, btn_surveyFormQuestions, btn_endPicture;

    private NetworkUtils networkUtils;
    private BeemPreferences beemPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        beemPreferences = new BeemPreferences(this);
        beemPreferences.initialize_and_createPreferences_forLoginSession(Constants.STATUS_ON_MERCHANT);

        networkUtils = new NetworkUtils(this);

        getCompulsorySteps();
        btn_takeStorePicture = (Button) findViewById(R.id.btn_takeStorePicture);
        btn_submitFeedback = (Button) findViewById(R.id.btn_submitFeedback);
        btn_takeFrontChillersPicture = (Button) findViewById(R.id.btn_takeFrontChillersPicture);
        btn_viewInstructions = (Button) findViewById(R.id.btn_viewInstructions);
        btn_takeAfterChillersPicture = (Button) findViewById(R.id.btn_takeAfterChillersPicture);
        btn_takeCompetionsPicture = (Button) findViewById(R.id.btn_takeCompetionsPicture);
        btn_updateStocks = (Button) findViewById(R.id.btn_updateStocks);
        btn_updateStockPrices = (Button) findViewById(R.id.btn_updateStockPrices);
        btn_surveyFormQuestions = (Button) findViewById(R.id.btn_surveyFormQuestions);
        btn_endPicture = (Button) findViewById(R.id.btn_endPicture);


        btn_takeStorePicture.setOnClickListener(this);
        btn_submitFeedback.setOnClickListener(this);
        btn_takeFrontChillersPicture.setOnClickListener(this);
        btn_viewInstructions.setOnClickListener(this);
        btn_takeAfterChillersPicture.setOnClickListener(this);
        btn_takeCompetionsPicture.setOnClickListener(this);
        btn_updateStocks.setOnClickListener(this);
        btn_updateStockPrices.setOnClickListener(this);
        btn_surveyFormQuestions.setOnClickListener(this);
        btn_endPicture.setOnClickListener(this);
    }

    private void getCompulsorySteps() {
        networkUtils.getComplusorySteps(38, new BaseCallbackInterface() {
            @Override
            public void success(Response response) {
                CompulsoryStepsResponseModel compulsoryStepsResponseModel = (CompulsoryStepsResponseModel) response.body();
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    void compulsoryInstructions(CompulsoryStepsResponseModel compulsoryStepsResponseModel) {
        if (compulsoryStepsResponseModel.getData().getTakeStorePicture() == 1) {
            btn_takeStorePicture.setVisibility(View.VISIBLE);
        } else {
            btn_takeStorePicture.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getSubmitFeedback() == 1) {
            btn_submitFeedback.setVisibility(View.VISIBLE);
        } else {
            btn_submitFeedback.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getBeforeChillersPicturesFront() == 1) {
            btn_takeFrontChillersPicture.setVisibility(View.VISIBLE);
        } else {
            btn_takeFrontChillersPicture.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getViewInstructions() == 1) {
            btn_viewInstructions.setVisibility(View.VISIBLE);
        } else {
            btn_viewInstructions.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getAfterChillersPictureFront() == 1) {
            btn_takeAfterChillersPicture.setVisibility(View.VISIBLE);
        } else {
            btn_takeAfterChillersPicture.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getTakeCompetitionPicture() == 1) {
            btn_takeCompetionsPicture.setVisibility(View.VISIBLE);
        } else {
            btn_takeCompetionsPicture.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getUpdateStocks() == 1) {
            btn_updateStocks.setVisibility(View.VISIBLE);
        } else {
            btn_updateStocks.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getUpdateStockPrices() == 1) {
            btn_updateStockPrices.setVisibility(View.VISIBLE);
        } else {
            btn_updateStockPrices.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getSurveyFormQuestion() == 1) {
            btn_surveyFormQuestions.setVisibility(View.VISIBLE);
        } else {
            btn_surveyFormQuestions.setVisibility(View.GONE);
        }

        if (compulsoryStepsResponseModel.getData().getEndPicture() == 1) {
            btn_endPicture.setVisibility(View.VISIBLE);
        } else {
            btn_endPicture.setVisibility(View.GONE);
        }
    }
}
