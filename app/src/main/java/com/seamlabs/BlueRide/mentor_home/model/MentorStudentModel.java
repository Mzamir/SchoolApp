package com.seamlabs.BlueRide.mentor_home.model;

public class MentorStudentModel {
    private int studentID;
    private String studentName;
    private String studentNationalID;
    private int schoolID;
    private int classID;
    private String studentCreatedAt;
    private String studentUpdatedAt;
    private boolean marked;
    private boolean mentorCanDeliver ;
    private String studentPicture;
    int requestId ;
    String requestState ;



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

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public boolean isMentorCanDeliver() {
        return mentorCanDeliver;
    }

    public void setMentorCanDeliver(boolean mentorCanDeliver) {
        this.mentorCanDeliver = mentorCanDeliver;
    }
}
