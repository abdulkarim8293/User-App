package com.abdulkarim.userapp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String product_id;
    private String user_id;
    private String order_status;
    private String price;
    private String delivery_charge;
    private String place_date;


    public Order() {

    }

    public Order(String product_id, String user_id, String order_status, String price, String delivery_charge, String place_date) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.order_status = order_status;
        this.price = price;
        this.delivery_charge = delivery_charge;
        this.place_date = place_date;
    }

    public String getPlace_date() {
        return place_date;
    }

    public void setPlace_date(String place_date) {
        this.place_date = place_date;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    @Override
    public String toString() {
        return "Order{" +
                "product_id='" + product_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", order_status='" + order_status + '\'' +
                ", price='" + price + '\'' +
                ", delivery_charge='" + delivery_charge + '\'' +
                '}';
    }
}
