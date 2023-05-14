package com.example.sharepay;

public class User {
    public String fullname, email;
    public User(){

    }
    public User(String fullname , String email){
        this.fullname = fullname;
        this.email = email;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
