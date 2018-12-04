package com.example.arsalansiddiq.beem.models.responsemodels;

/**
 * Created by jellani on 9/21/2018.
 */

public class ResponseSUP {

    private Integer status;
    private Integer id;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
