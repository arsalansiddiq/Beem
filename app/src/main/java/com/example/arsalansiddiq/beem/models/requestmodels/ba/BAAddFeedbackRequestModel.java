package com.example.arsalansiddiq.beem.models.requestmodels.ba;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jellani on 12/23/2018.
 */

public class BAAddFeedbackRequestModel {

    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("comments")
    @Expose
    private String comments;

    public BAAddFeedbackRequestModel(String customerName, String contactNo, Integer rating, String productName, String comments) {
        this.customerName = customerName;
        this.contactNo = contactNo;
        this.rating = rating;
        this.productName = productName;
        this.comments = comments;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
