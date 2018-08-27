package com.example.rabee.breath.Models.RequestModels;

/**
 * Created by Rabee on 1/22/2018.
 */

public class EditEmailRequestModel {

    int id;
    String new_email;
    String password;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String first_name) {
        this.new_email = first_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String last_name) {
        this.password = last_name;
    }
}
