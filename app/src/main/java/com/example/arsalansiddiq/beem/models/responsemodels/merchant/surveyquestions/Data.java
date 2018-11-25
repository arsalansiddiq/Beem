package com.example.arsalansiddiq.beem.models.responsemodels.merchant.surveyquestions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jellani on 11/24/2018.
 */

public class Data {

    @SerializedName("questions")
    @Expose
    private List<String> questions = null;
    @SerializedName("Answers1")
    @Expose
    private List<String> answers1 = null;
    @SerializedName("Answers2")
    @Expose
    private List<String> answers2 = null;
    @SerializedName("Answers3")
    @Expose
    private List<String> answers3 = null;

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getAnswers1() {
        return answers1;
    }

    public void setAnswers1(List<String> answers1) {
        this.answers1 = answers1;
    }

    public List<String> getAnswers2() {
        return answers2;
    }

    public void setAnswers2(List<String> answers2) {
        this.answers2 = answers2;
    }

    public List<String> getAnswers3() {
        return answers3;
    }

    public void setAnswers3(List<String> answers3) {
        this.answers3 = answers3;
    }

}
