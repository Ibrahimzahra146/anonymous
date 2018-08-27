package com.example.rabee.breath;

/**
 * Created by Rabee on 1/16/2018.
 */

public class ReactsRecyclerViewModel {
    String image;
    String name;
    int user_id;
    public ReactsRecyclerViewModel(int user_id,String name,String image){
        this.user_id=user_id;
        this.name=name;
        this.image=image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
