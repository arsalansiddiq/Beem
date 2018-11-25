package com.example.arsalansiddiq.beem.models.requestmodels;

/**
 * Created by jellani on 9/22/2018.
 */

public class AddShopRequest {

    private Integer emp_id;
    private String shopname;
    private String owner;
    private String contactperson;
    private String contactnumber;
    private float lat;
    private float lng;


    public AddShopRequest(Integer emp_id, String shopname, String owner, String contactperson, String contactnumber, float lat, float lng) {
        this.emp_id = emp_id;
        this.shopname = shopname;
        this.owner = owner;
        this.contactperson = contactperson;
        this.contactnumber = contactnumber;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getEmp_id() {
        return emp_id;
    }

    public String getShopname() {
        return shopname;
    }

    public String getOwner() {
        return owner;
    }

    public String getContactperson() {
        return contactperson;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }
}
