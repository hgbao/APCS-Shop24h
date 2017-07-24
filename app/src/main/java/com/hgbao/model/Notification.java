package com.hgbao.model;

public class Notification {
    //Notification attributes
    long date;
    String type, description;
    String companyId, objectId;
    //Programming attributes
    Boolean isUpdate;

    //Constructor
    public Notification(){
        isUpdate = false;
    }

    //Getters and setters
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Boolean isUpdate() {
        return this.isUpdate;
    }

    public void setUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
