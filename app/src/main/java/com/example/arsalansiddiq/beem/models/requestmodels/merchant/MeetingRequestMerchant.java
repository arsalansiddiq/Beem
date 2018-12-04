package com.example.arsalansiddiq.beem.models.requestmodels.merchant;

/**
 * Created by jellani on 12/4/2018.
 */

public class MeetingRequestMerchant {

    private Integer shop_id;
    private Double latitude;
    private Double longitude;

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
