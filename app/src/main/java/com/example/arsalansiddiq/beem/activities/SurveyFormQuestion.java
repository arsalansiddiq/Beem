package com.example.arsalansiddiq.beem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SurveyFormQuestion extends AppCompatActivity {

    //View
    @BindView(R.id.txtView_question)
    TextView txtView_question;
    @BindView(R.id.checkbox_answer1)
    CheckBox checkbox_answer1;
    @BindView(R.id.checkbox_answer2)
    CheckBox checkbox_answer2;
    @BindView(R.id.checkbox_answer3)
    CheckBox checkbox_answer3;

    private NetworkUtils networkUtils;
    private SurveyQuestionsResponseModel surveyQuestionsResponseModel;
    private List<String> questionsList;
    private List<String> answersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form_question);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        networkUtils = new NetworkUtils(this);

        if (networkUtils.isNetworkConnected()) {
            networkUtils.getSurveyQA(new BaseCallbackInterface() {
                @Override
                public void success(Response response) {
//                    surveyQuestionsResponseModel = (SurveyQuestionsResponseModel) response.body();
                    questionsList = ((SurveyQuestionsResponseModel) response.body()).getData().getQuestions();

                }

                @Override
                public void failure(String error) {

                }
            });
        }
    }

    void viewQA() {

    }
}
