package com.example.rabee.breath.Models.RequestModels;

/**
 * Created by Rabee on 1/22/2018.
 */

public class EditPrivacyRequestModel {
    int id;
    String isPublic;
    String isProfileImagePublic;
    String themeColor;

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsProfileImagePublic() {
        return isProfileImagePublic;
    }

    public void setIsProfileImagePublic(String isProfileImagePublic) {
        this.isProfileImagePublic = isProfileImagePublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }


}

