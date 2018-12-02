package com.example.arsalansiddiq.beem.models.responsemodels.merchant.competitionsku;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jellani on 12/1/2018.
 */

public class MerchantSKU {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<DatumMerchant> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DatumMerchant> getData() {
        return data;
    }

    public void setData(List<DatumMerchant> data) {
        this.data = data;
    }
}
