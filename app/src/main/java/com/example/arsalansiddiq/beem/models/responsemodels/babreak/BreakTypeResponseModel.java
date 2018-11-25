package com.example.arsalansiddiq.beem.models.responsemodels.babreak;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jellani on 9/29/2018.
 */

public class BreakTypeResponseModel {

    @SerializedName("status")
    @Expose
    private List<Status> status = null;

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }


}
