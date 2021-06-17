package com.abdulkarim.userapp.my_address;

import java.io.Serializable;

public class MyAddress implements Serializable {

    private String id;
    private String userId;
    private String name;
    private String phone;
    private String city;
    private String zip;
    private String address;
    private String type;
    private String note;

    public MyAddress() {

    }

    public MyAddress(String id, String name, String phone, String city, String zip, String address, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.zip = zip;
        this.address = address;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "MyAddress{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
