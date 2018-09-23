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
    private Double lat;
    private Double lng;


    public AddShopRequest(Integer emp_id, String shopname, String owner, String contactperson, String contactnumber, Double lat, Double lng) {
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

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
