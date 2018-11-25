package com.example.arsalansiddiq.beem.models.responsemodels.merchant.compulsory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jellani on 11/24/2018.
 */

public class Data {

    @SerializedName("Take_Store_Picture")
    @Expose
    private Integer takeStorePicture;
    @SerializedName("Submit_Feedback")
    @Expose
    private Integer submitFeedback;
    @SerializedName("Before_Chillers_Pictures_Front")
    @Expose
    private Integer beforeChillersPicturesFront;
    @SerializedName("View_Instructions")
    @Expose
    private Integer viewInstructions;
    @SerializedName("After_Chillers_Picture_Front")
    @Expose
    private Integer afterChillersPictureFront;
    @SerializedName("Take_Competition_Picture")
    @Expose
    private Integer takeCompetitionPicture;
    @SerializedName("Update_Stocks")
    @Expose
    private Integer updateStocks;
    @SerializedName("Update_stock_Prices")
    @Expose
    private Integer updateStockPrices;
    @SerializedName("Survey_Form_Question")
    @Expose
    private Integer surveyFormQuestion;
    @SerializedName("End_Picture")
    @Expose
    private Integer endPicture;

    public Integer getTakeStorePicture() {
        return takeStorePicture;
    }

    public void setTakeStorePicture(Integer takeStorePicture) {
        this.takeStorePicture = takeStorePicture;
    }

    public Integer getSubmitFeedback() {
        return submitFeedback;
    }

    public void setSubmitFeedback(Integer submitFeedback) {
        this.submitFeedback = submitFeedback;
    }

    public Integer getBeforeChillersPicturesFront() {
        return beforeChillersPicturesFront;
    }

    public void setBeforeChillersPicturesFront(Integer beforeChillersPicturesFront) {
        this.beforeChillersPicturesFront = beforeChillersPicturesFront;
    }

    public Integer getViewInstructions() {
        return viewInstructions;
    }

    public void setViewInstructions(Integer viewInstructions) {
        this.viewInstructions = viewInstructions;
    }

    public Integer getAfterChillersPictureFront() {
        return afterChillersPictureFront;
    }

    public void setAfterChillersPictureFront(Integer afterChillersPictureFront) {
        this.afterChillersPictureFront = afterChillersPictureFront;
    }

    public Integer getTakeCompetitionPicture() {
        return takeCompetitionPicture;
    }

    public void setTakeCompetitionPicture(Integer takeCompetitionPicture) {
        this.takeCompetitionPicture = takeCompetitionPicture;
    }

    public Integer getUpdateStocks() {
        return updateStocks;
    }

    public void setUpdateStocks(Integer updateStocks) {
        this.updateStocks = updateStocks;
    }

    public Integer getUpdateStockPrices() {
        return updateStockPrices;
    }

    public void setUpdateStockPrices(Integer updateStockPrices) {
        this.updateStockPrices = updateStockPrices;
    }

    public Integer getSurveyFormQuestion() {
        return surveyFormQuestion;
    }

    public void setSurveyFormQuestion(Integer surveyFormQuestion) {
        this.surveyFormQuestion = surveyFormQuestion;
    }

    public Integer getEndPicture() {
        return endPicture;
    }

    public void setEndPicture(Integer endPicture) {
        this.endPicture = endPicture;
    }

}