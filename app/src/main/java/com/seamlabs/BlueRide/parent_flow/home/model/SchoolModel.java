package com.seamlabs.BlueRide.parent_flow.home.model;

import java.io.Serializable;

public class SchoolModel implements Serializable {

    private String schoolCover ;
    private String schoolTitle ;
    private String schoolAddress ;
    private int schoolID ;
    private String schoolLong;
    private String schoolLat;
    private String schoolCreatedAt;
    private String schoolUpdatedAt;
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

    public String getschoolAddress() {
        return schoolAddress;
    }

    public void setschoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public int getschoolID() {
        return schoolID;
    }

    public void setschoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public String getschoolLong() {
        return schoolLong;
    }

    public void setschoolLong(String schoolLong) {
        this.schoolLong = schoolLong;
    }

    public String getschoolLat() {
        return schoolLat;
    }

    public void setschoolLat(String schoolLat) {
        this.schoolLat = schoolLat;
    }

    public String getschoolCreatedAt() {
        return schoolCreatedAt;
    }

    public void setschoolCreatedAt(String schoolCreatedAt) {
        this.schoolCreatedAt = schoolCreatedAt;
    }

    public String getschoolUpdatedAt() {
        return schoolUpdatedAt;
    }

    public void setschoolUpdatedAt(String schoolUpdatedAt) {
        this.schoolUpdatedAt = schoolUpdatedAt;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }
}
