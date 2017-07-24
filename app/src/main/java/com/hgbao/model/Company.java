package com.hgbao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Company implements Serializable{
    //Company attributes
    String id;
    String name;
    String address, phone, email, website;
    String detail;
    byte[] logo, marker;
    String colorPrimary, colorSecondary;
    //Shops, products, promotions list
    private ArrayList<Shop> list_shop;
    private ArrayList<Promotion> list_promotion;

    //Constructor
    public Company(){
        list_shop = new ArrayList<>();
        list_promotion = new ArrayList<>();
    }

    //Getters and setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public byte[] getMarker() {
        return marker;
    }

    public void setMarker(byte[] marker) {
        this.marker = marker;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public String getColorSecondary() {
        return colorSecondary;
    }

    public void setColorSecondary(String colorSecondary) {
        this.colorSecondary = colorSecondary;
    }

    public ArrayList<Shop> getList_shop() {
        return list_shop;
    }

    public void setList_shop(ArrayList<Shop> list_shop) {
        this.list_shop = list_shop;
    }

    public ArrayList<Promotion> getList_promotion() {
        return list_promotion;
    }

    public void setList_promotion(ArrayList<Promotion> list_promotion) {
        this.list_promotion = list_promotion;
    }
}
