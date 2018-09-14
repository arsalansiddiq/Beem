package com.example.arsalansiddiq.beem.models.responsemodels.targetsandachievementsmodel;

/**
 * Created by jellani on 9/4/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Target {

    @SerializedName("Storeid")
    @Expose
    private Integer storeid;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("skuids")
    @Expose
    private Integer skuids;
    @SerializedName("skuName")
    @Expose
    private String skuName;
    @SerializedName("skutargets")
    @Expose
    private String skutargets;
    @SerializedName("ach")
    @Expose
    private Integer ach;
    @SerializedName("per")
    @Expose
    private Integer per;

    public Integer getStoreid() {
        return storeid;
    }

    public void setStoreid(Integer storeid) {
        this.storeid = storeid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSkuids() {
        return skuids;
    }

    public void setSkuids(Integer skuids) {
        this.skuids = skuids;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkutargets() {
        return skutargets;
    }

    public void setSkutargets(String skutargets) {
        this.skutargets = skutargets;
    }

    public Integer getAch() {
        return ach;
    }

    public void setAch(Integer ach) {
        this.ach = ach;
    }

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

}
