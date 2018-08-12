package com.seamlabs.BlueRide.parent_flow.home.model;

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
}
