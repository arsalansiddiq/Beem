package com.example.arsalansiddiq.beem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.databases.BeemPreferencesCount;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.compulsory.CompulsoryStepsResponseModel;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import java.io.File;

import retrofit2.Response;

public class MerchantActivity extends AppCompatActivity implements View.OnClickListener, UpdateCallback{

    private String callTag = null;

    private String CURRENT_KEY = "";
    private static  final String TAKE_STORE_PICTURE_COUNT = "Take_Store_Picture_";
    private static  final String TAKE_FRONT_CHILLER_COUNT = "Before_Chillers_Pic_Front_";
    private static  final String TAKE_AFTER_CHILLER_COUNT = "After_Chillers_Pic_Front_";
    private static  final String TAKE_COMPETITION_PICTURE_COUNT = "Take_Competition_Pic_";
    private static  final String END_PIC_COUNT = "End_Pic";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button btn_takeStorePicture, btn_submitFeedback, btn_takeFrontChillersPicture,
            btn_viewInstructions, btn_takeAfterChillersPicture, btn_takeCompetionsPicture,
            btn_updateStocks, btn_updateStockPrices, btn_surveyFormQuestions, btn_endPicture;

    private NetworkUtils networkUtils;
    private BeemPreferences beemPreferences;
    private BeemPreferencesCount beemPreferencesCount;

    private RealmCRUD realmCRUD;
    private LoginResponse loginResponseRealm;

    //Image Instances
    private Bitmap imageBitmap;
    private AppUtils appUtils;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        appUtils = new AppUtils(this);
        realmCRUD = new RealmCRUD(this);
        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        beemPreferences = new BeemPreferences(this);
        beemPreferencesCount = new BeemPreferencesCount(this);

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
                compulsoryInstructions(compulsoryStepsResponseModel);
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_takeStorePicture:
                i = getCount(Constants.TAKE_STORE_PICTURE);
                if (i >= 3) {
                    Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    i = i+1;
                    CURRENT_KEY = TAKE_STORE_PICTURE_COUNT + i;
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.btn_takeFrontChillersPicture:
                i = getCount(Constants.TAKE_FRONT_CHILLER);
                if (i >= 3) {
                    Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    i = i + 1;
                    CURRENT_KEY = TAKE_FRONT_CHILLER_COUNT + "1";
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.btn_takeAfterChillersPicture:
                i = getCount(Constants.TAKE_AFTER_CHILLER);
                if (i >= 3) {
                    Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    i = i + 1;
                    CURRENT_KEY = TAKE_AFTER_CHILLER_COUNT + "1";
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.btn_takeCompetionsPicture:
                i = getCount(Constants.TAKE_COMPETITION_PICTURE);
                if (i >= 3) {
                    Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    i = i + 1;
                    CURRENT_KEY = TAKE_COMPETITION_PICTURE_COUNT + "1";
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.btn_endPicture:
                i = getCount(Constants.TAKE_END_PICTURE);
                if (i >= 3) {
                    Toast.makeText(this, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                } else {
                    i = i + 1;
                    CURRENT_KEY = END_PIC_COUNT + "1";
                    dispatchTakePictureIntent();
                }
                break;

            case R.id.btn_surveyFormQuestions:
                Intent intent = new Intent(MerchantActivity.this, SurveyFormQuestion.class);
                startActivity(intent);
                break;

            case R.id.btn_viewInstructions:
                networkUtils.getViewInstruction(Integer.parseInt(loginResponseRealm.getBrand()),
                        "Visit Shop", this);
                break;


        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            File userImageFile = appUtils.getImageFile(imageBitmap);

            networkUtils.dynamicKeyFiles(8, 8,
                    8,
                    8,
                    CURRENT_KEY, userImageFile, this);
        }
    }

    @Override
    public void UpdateSuccess(Response response) {
        String currentKey = CURRENT_KEY.substring(0, CURRENT_KEY.length() - 1);

        if (currentKey.equals(TAKE_STORE_PICTURE_COUNT)) {
            beemPreferencesCount.putInt(Constants.TAKE_STORE_PICTURE, i++);
        } else if (currentKey.equals(TAKE_FRONT_CHILLER_COUNT)) {
            beemPreferencesCount.putInt(Constants.TAKE_FRONT_CHILLER, i++);
        } else if (currentKey.equals(TAKE_AFTER_CHILLER_COUNT)) {
            beemPreferencesCount.putInt(Constants.TAKE_AFTER_CHILLER, i++);
        } else if (currentKey.equals(TAKE_COMPETITION_PICTURE_COUNT)) {
            beemPreferencesCount.putInt(Constants.TAKE_COMPETITION_PICTURE, i++);
        } else if (currentKey.equals(END_PIC_COUNT)) {
            beemPreferencesCount.putInt(Constants.TAKE_END_PICTURE, i++);
        }
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {

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

    int getCount(String key) {
        SharedPreferences prefs = getSharedPreferences(Constants.BEEM_PREFERENCE_COUNT, MODE_PRIVATE);
        int coutn = prefs.getInt(key, 0);
        return coutn;
    }
}
