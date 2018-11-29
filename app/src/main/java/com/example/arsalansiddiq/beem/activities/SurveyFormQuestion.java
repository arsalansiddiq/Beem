package com.example.arsalansiddiq.beem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.interfaces.merchantcallback.BaseCallbackInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions.SurveyQuestionsResponseModel;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SurveyFormQuestion extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    //View
    @BindView(R.id.txtView_question)
    TextView txtView_question;
//    @BindView(R.id.checkbox_answer1)
//    CheckBox checkbox_answer1;
//    @BindView(R.id.checkbox_answer2)
//    CheckBox checkbox_answer2;
//    @BindView(R.id.checkbox_answer3)
//    CheckBox checkbox_answer3;

    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;

    private NetworkUtils networkUtils;
    private SurveyQuestionsResponseModel surveyQuestionsResponseModel;
    private List<String> questionsList;
    private List<String> answersList1;
    private List<String> answersList2;
    private List<String> answersList3;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_form_question);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

//        checkbox_answer1.setOnCheckedChangeListener(this);
//        checkbox_answer2.setOnCheckedChangeListener(this);
//        checkbox_answer3.setOnCheckedChangeListener(this);
        radiogroup.setOnCheckedChangeListener(this);

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

                    count = count+1;
                    viewQA();
                }

                @Override
                public void failure(String error) {

                }
            });
        }
    }

    void viewQA() {
        if (count > 0) {

            String question, ans1, ans2, ans3;
            question = questionsList.get(count-1);
            ans1 = answersList1.get(count-1);
            ans2 = answersList2.get(count-1);
            ans3 = answersList3.get(count-1);

             if (!TextUtils.isEmpty(question)) {
                 txtView_question.setText(questionsList.get(count - 1));

                 if (!TextUtils.isEmpty(ans1)) radioButton1.setText(ans1);
                 if (!TextUtils.isEmpty(ans2)) radioButton2.setText(ans2);
                 if (!TextUtils.isEmpty(ans3)) radioButton3.setText(ans3);

             }

        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////        if (buttonView.getId())
//    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int checkedID = radiogroup.getCheckedRadioButtonId();

        RadioButton checkedRadioButton = (RadioButton) findViewById(checkedID);

        Log.i("value", String.valueOf(checkedRadioButton.getText()));

    }
}
