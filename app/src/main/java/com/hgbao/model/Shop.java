package com.hgbao.model;

import java.io.Serializable;

public class Shop implements Serializable{
    //Shop's attributes
    String id;
    String companyId;
    String name;
    String address;
    String phone;
    //Programming attributes
    double latitude, longitude;
    String districtId, cityId;
    boolean isFavourite;
    //Products list

    //Constructor
    public Shop(){

    }

    //Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite() {
        if (this.isFavourite)
            this.isFavourite = false;
        else
            this.isFavourite = true;
    }
}
