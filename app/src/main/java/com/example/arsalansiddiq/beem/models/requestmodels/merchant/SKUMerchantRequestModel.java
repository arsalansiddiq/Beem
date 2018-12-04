
package com.example.arsalansiddiq.beem.models.requestmodels.merchant;

import com.google.gson.annotations.SerializedName;

public class SKUMerchantRequestModel {

    @SerializedName("brand_id")
    private Long mBrandId;
    @SerializedName("competition")
    private Long mCompetition;
    @SerializedName("price")
    private Long mPrice;
    @SerializedName("shop_id")
    private Long mShopId;
    @SerializedName("sku_id")
    private Long mSkuId;
    @SerializedName("stock")
    private Long mStock;
    @SerializedName("task_id")
    private Long mTaskId;
    @SerializedName("user_id")
    private Long mUserId;

    public SKUMerchantRequestModel() {
    }

    public SKUMerchantRequestModel(Long mBrandId, Long mCompetition, Long mShopId,
                                   Long mSkuId, Long mTaskId, Long mUserId) {
        this.mTaskId = mTaskId;
        this.mBrandId = mBrandId;
        this.mShopId = mShopId;
        this.mUserId = mUserId;
        this.mSkuId = mSkuId;
        this.mPrice = mPrice;
        this.mCompetition = mCompetition;
    }

    public Long getBrandId() {
        return mBrandId;
    }

    public void setBrandId(Long brandId) {
        mBrandId = brandId;
    }

    public Long getCompetition() {
        return mCompetition;
    }

    public void setCompetition(Long competition) {
        mCompetition = competition;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public Long getShopId() {
        return mShopId;
    }

    public void setShopId(Long shopId) {
        mShopId = shopId;
    }

    public Long getSkuId() {
        return mSkuId;
    }

    public void setSkuId(Long skuId) {
        mSkuId = skuId;
    }

    public Long getStock() {
        return mStock;
    }

    public void setStock(Long stock) {
        mStock = stock;
    }

    public Long getTaskId() {
        return mTaskId;
    }

    public void setTaskId(Long taskId) {
        mTaskId = taskId;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

}
