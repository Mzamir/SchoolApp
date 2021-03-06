package com.seamlabs.BlueRide.parent_flow.home.model;

import com.seamlabs.BlueRide.network.response.StudentPivotResponseModel;

public class StudentModel {

    private int studentID;
    private String studentName;
    private String studentNationalID;
    private int schoolID;
    private int classID;
    private String studentCreatedAt;
    private String studentUpdatedAt;
    private boolean marked;
    private String studentPicture;
    String class_name;
    String grade_name;
    boolean in_request ;
    StudentPivotResponseModel pivot = new StudentPivotResponseModel();

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNationalID() {
        return studentNationalID;
    }

    public void setStudentNationalID(String studentNationalID) {
        this.studentNationalID = studentNationalID;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getStudentCreatedAt() {
        return studentCreatedAt;
    }

    public void setStudentCreatedAt(String studentCreatedAt) {
        this.studentCreatedAt = studentCreatedAt;
    }

    public String getStudentUpdatedAt() {
        return studentUpdatedAt;
    }

    public void setStudentUpdatedAt(String studentUpdatedAt) {
        this.studentUpdatedAt = studentUpdatedAt;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getStudentPicture() {
        return studentPicture;
    }

    public void setStudentPicture(String studentPicture) {
        this.studentPicture = studentPicture;
    }

    public StudentPivotResponseModel getPivot() {
        return pivot;
    }

    public void setPivot(StudentPivotResponseModel pivot) {
        this.pivot = pivot;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public boolean isIn_request() {
        return in_request;
    }

    public void setIn_request(boolean in_request) {
        this.in_request = in_request;
    }
}
