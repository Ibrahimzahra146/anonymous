package com.example.rabee.breath.Models.RequestModels;

/**
 * Created by Rabee on 1/22/2018.
 */

public class SignUpRequestModel {
    String first_name;
    String last_name;
    String email;
    String password;
    String mobile;
    String birthdate;
    String gender;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
           this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
        }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

        public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
