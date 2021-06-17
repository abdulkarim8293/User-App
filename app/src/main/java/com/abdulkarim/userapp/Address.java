package com.abdulkarim.userapp;

public class Address {
    private String name;
    private String phone_number;
    private String city;

    public Address() {
    }

    public Address(String name, String phone_number, String city) {
        this.name = name;
        this.phone_number = phone_number;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
