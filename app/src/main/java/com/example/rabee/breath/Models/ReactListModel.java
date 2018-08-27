package com.example.rabee.breath.Models;

/**
 * Created by Rabee on 1/20/2018.
 */

public class ReactListModel {
    ReactSingleModel likeList;
    ReactSingleModel dislikeList;
    ReactSingleModel loveList;
    public ReactSingleModel getLikeList() {
        return likeList;
    }
    public void setLikeList(ReactSingleModel likeList) {
        this.likeList = likeList;
    }
    public ReactSingleModel getDislikeList() {
        return dislikeList;
    }
    public void setDislikeList(ReactSingleModel dislikeList) {
        this.dislikeList = dislikeList;
    }
    public ReactSingleModel getLoveList() {
        return loveList;
    }
    public void setLoveList(ReactSingleModel loveList) {
        this.loveList = loveList;
    }
}