package com.example.rabee.breath.Models;

/**
 * Created by Rabee on 1/20/2018.
 */

public class YoutubeLinkModel {

    int id;
    String image;
    String title;
    String author_name;
    String link;

    public int getId() {
        return id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

