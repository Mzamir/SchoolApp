package com.example.mahmoudsamir.schoolappand.parent_home.model;

public class SchoolModel {

    private String schoolCover ;
    private String schoolTitle ;
    private String shcoolAddress ;
    private int shcoolID ;
    private String shcoolLong;
    private String shcoolLat;
    private String shcoolCreatedAt;
    private String shcoolUpdatedAt;
    private boolean marked ;

    public String getSchoolCover() {
        return schoolCover;
    }

    public void setSchoolCover(String schoolCover) {
        this.schoolCover = schoolCover;
    }

    public String getSchoolTitle() {
        return schoolTitle;
    }

    public void setSchoolTitle(String schoolTitle) {
        this.schoolTitle = schoolTitle;
    }

    public String getShcoolAddress() {
        return shcoolAddress;
    }

    public void setShcoolAddress(String shcoolAddress) {
        this.shcoolAddress = shcoolAddress;
    }

    public int getShcoolID() {
        return shcoolID;
    }

    public void setShcoolID(int shcoolID) {
        this.shcoolID = shcoolID;
    }

    public String getShcoolLong() {
        return shcoolLong;
    }

    public void setShcoolLong(String shcoolLong) {
        this.shcoolLong = shcoolLong;
    }

    public String getShcoolLat() {
        return shcoolLat;
    }

    public void setShcoolLat(String shcoolLat) {
        this.shcoolLat = shcoolLat;
    }

    public String getShcoolCreatedAt() {
        return shcoolCreatedAt;
    }

    public void setShcoolCreatedAt(String shcoolCreatedAt) {
        this.shcoolCreatedAt = shcoolCreatedAt;
    }

    public String getShcoolUpdatedAt() {
        return shcoolUpdatedAt;
    }

    public void setShcoolUpdatedAt(String shcoolUpdatedAt) {
        this.shcoolUpdatedAt = shcoolUpdatedAt;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
