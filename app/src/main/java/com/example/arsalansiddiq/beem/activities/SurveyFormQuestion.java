package com.example.arsalansiddiq.beem.activities;

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
import com.example.arsalansiddiq.beem.databases.RealmCRUD;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;
import com.example.arsalansiddiq.beem.utils.data.BaseResponse;
import com.example.arsalansiddiq.beem.utils.data.UpdateCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SurveyFormQuestion extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form_question);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);


        realmCRUD = new RealmCRUD(this);
        loginResponseRealm = realmCRUD.getLoginInformationDetails();

        linearLayout_content.setVisibility(View.GONE);
        radiogroup.setOnCheckedChangeListener(this);
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
        }
    }

    void viewQA() {

        hideNext();

        if (count >= 0) {

            String question, ans1, ans2, ans3;
            question = questionsList.get(count);
            ans1 = answersList1.get(count);
            ans2 = answersList2.get(count);
            ans3 = answersList3.get(count);

             if (!TextUtils.isEmpty(question)) {
                 txtView_question.setText(questionsList.get(count));

                 if (!TextUtils.isEmpty(ans1)) radioButton1.setText(ans1);
                 else radioButton1.setVisibility(View.GONE);

                 if (!TextUtils.isEmpty(ans2)) radioButton2.setText(ans2);
                 else radioButton2.setVisibility(View.GONE);

                 if (!TextUtils.isEmpty(ans3)) radioButton3.setText(ans3);
                 else radioButton3.setVisibility(View.GONE);

             }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int checkedID = radiogroup.getCheckedRadioButtonId();

        RadioButton checkedRadioButton = (RadioButton) findViewById(checkedID);

        Log.i("value", String.valueOf(checkedRadioButton.getText()));

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

                    if (count == questionsList.size()) {
                        submitQuesitonToServer();
                    }

                    count = count + 1;
                    submitHideShow(false);


                }
                break;
            case R.id.btn_nextQ:
                viewQA();
                hideNext();
                submitHideShow(true);
                break;
        }
    }

    void hideNext() {
        if (count == questionsList.size()) {
            btn_nextQ.setVisibility(View.GONE);
        }
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
        networkUtils.dynamicKeyValueQA(loginResponseRealm.getUserId(), 8, 8,
                Integer.parseInt(loginResponseRealm.getBrand()), "Questions", questionListUser,
                "Answers", answersListUser, this);
    }

    @Override
    public void UpdateSuccess(Response response) {

    }

    @Override
    public void UpdateFailure(BaseResponse baseResponse) {

    }
}
