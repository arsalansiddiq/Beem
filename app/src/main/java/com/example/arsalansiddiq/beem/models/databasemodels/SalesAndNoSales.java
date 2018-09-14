package com.example.arsalansiddiq.beem.models.databasemodels;

/**
 * Created by jellani on 9/2/2018.
 */

public class SalesAndNoSales {

//    @PrimaryKey
//    private int id;
    private int total_sales;
    private int total_nosales;

    public SalesAndNoSales(int total_sales, int total_nosales) {
        this.total_sales = total_sales;
        this.total_nosales = total_nosales;
    }

    public SalesAndNoSales() {
    }

    //    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public int getTotal_sales() {
        return total_sales;
    }

    public void setTotal_sales(int total_sales) {
        this.total_sales = total_sales;
    }

    public int getTotal_nosales() {
        return total_nosales;
    }

    public void setTotal_nosales(int total_nosales) {
        this.total_nosales = total_nosales;
    }
}
