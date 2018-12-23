package com.example.arsalansiddiq.beem.models.responsemodels.bamodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by jellani on 12/22/2018.
 */

public class Data extends RealmObject {

    private int loginUserRelationIdWithBrands;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    public int getLoginUserRelationIdWithBrands() {
        return loginUserRelationIdWithBrands;
    }

    public void setLoginUserRelationIdWithBrands(int loginUserRelationIdWithBrands) {
        this.loginUserRelationIdWithBrands = loginUserRelationIdWithBrands;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
