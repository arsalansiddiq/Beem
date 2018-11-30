package com.example.arsalansiddiq.beem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.base.BaseActivity;
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.merchanttask.MerchantTaskResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SurveyFormQuestion extends BaseActivity implements
        View.OnClickListener, UpdateCallback {

    //View
    @BindView(R.id.txtView_question)
    TextView txtView_question;

    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;

    @BindView(R.id.btn_SubmitQ)
    Button btn_SubmitQ;
    @BindView(R.id.btn_nextQ)
    Button btn_nextQ;
    @BindView(R.id.linearLayout_content)
    LinearLayout linearLayout_content;
    //

    private NetworkUtils networkUtils;
    private SurveyQuestionsResponseModel surveyQuestionsResponseModel;
    private List<String> questionsList;
    private List<String> answersList1;
    private List<String> answersList2;
    private List<String> answersList3;
    private int count = 0;

    private List<String> questionListUser;
    private List<String> answersListUser;

    private RealmCRUD realmCRUD;
    private LoginResponse loginResponseRealm;

    private boolean isSurveyEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form_question);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        initProgressBar();


        realmCRUD = new RealmCRUD(this);
        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        linearLayout_content.setVisibility(View.GONE);
        btn_SubmitQ.setOnClickListener(this);
        btn_nextQ.setOnClickListener(this);

        questionListUser = new ArrayList<String>();
        answersListUser = new ArrayList<String>();

        networkUtils = new NetworkUtils(this);

        if (networkUtils.isNetworkConnected()) {
            networkUtils.getSurveyQA(new BaseCallbackInterface() {
                @Override
                public void success(Response response) {
//                    surveyQuestionsResponseModel = (SurveyQuestionsResponseModel) response.body();
                    questionsList = ((SurveyQuestionsResponseModel) response.body()).getData().getQuestions();
                    answersList1 = ((SurveyQuestionsResponseModel) response.body()).getData().getQuestions();
                    answersList2 = ((SurveyQuestionsResponseModel) response.body()).getData().getQuestions();
                    answersList3 = ((SurveyQuestionsResponseModel) response.body()).getData().getQuestions();

                    linearLayout_content.setVisibility(View.VISIBLE);
                    viewQA();
                }

                @Override
                public void failure(String error) {

                }
            });
        } else {
            Toast.makeText(this, "please connect with internet", Toast.LENGTH_SHORT).show();
        }
    }

    void viewQA() {

        if (!TextUtils.isEmpty(questionsList.get(count))) {

            linearLayout_content.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(questionsList.get(count+1))) {
                isSurveyEnd = true;
                hideNext();
            }

                 txtView_question.setText(questionsList.get(count));

                 if (!TextUtils.isEmpty(answersList1.get(count))) radioButton1.setText(answersList1.get(count));
                 else radioButton1.setVisibility(View.GONE);

                 if (!TextUtils.isEmpty(answersList2.get(count))) radioButton2.setText(answersList2.get(count));
                 else radioButton2.setVisibility(View.GONE);

                 if (!TextUtils.isEmpty(answersList3.get(count))) radioButton3.setText(answersList3.get(count));
                 else radioButton3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_SubmitQ:
                if (radiogroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "please choose any one option", Toast.LENGTH_SHORT).show();
                } else {
                    int checkedID = radiogroup.getCheckedRadioButtonId();
                    RadioButton checkedRadioButton = (RadioButton) findViewById(checkedID);

                    questionListUser.add(txtView_question.getText().toString());
                    answersListUser.add(checkedRadioButton.getText().toString());

                    radiogroup.clearCheck();

                    if (isSurveyEnd) {
                        submitQuesitonToServer();
                    }

                    submitHideShow(false);


                }
                break;
            case R.id.btn_nextQ:
                count = count+1;
                viewQA();
                submitHideShow(true);
                break;
        }
    }

    void hideNext() {
//        if (count == questionsList.size()) {
            btn_nextQ.setVisibility(View.GONE);
//        }
    }

    void showNext() {
        btn_nextQ.setVisibility(View.VISIBLE);
    }

    void submitHideShow (boolean isTrue) {
        if (isTrue) {
            btn_SubmitQ.setVisibility(View.VISIBLE);
        } else {
            btn_SubmitQ.setVisibility(View.GONE);
        }
    }

    void submitQuesitonToServer () {
        progressShow();

        if (networkUtils.isNetworkConnected()) {
            networkUtils.dynamicKeyValueQA(8, 8, 8,
                    38, "Questions", questionListUser,
                    "Answers", answersListUser, this);
        } else {
            Toast.makeText(this, "please connect with internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void UpdateSuccess(Response response) {

        progressHide();
        MerchantTaskResponse merchantTaskResponse = (MerchantTaskResponse) response.body();

        if (merchantTaskResponse.getStatus() == 1) {
            Intent intent = new Intent(SurveyFormQuestion.this, MerchantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {
        progressHide();
    }
}
