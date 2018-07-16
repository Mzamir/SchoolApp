package com.example.mahmoudsamir.schoolappand.parent_signin.model;

public class ParentModel {
    String id;
    String password;

    public ParentModel() {
    }

    public ParentModel(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
