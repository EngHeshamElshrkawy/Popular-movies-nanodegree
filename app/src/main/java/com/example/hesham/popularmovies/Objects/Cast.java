package com.example.hesham.popularmovies.Objects;

public class Cast {
    private String name;
    private String image;
    private String imageDir;

    public Cast(String mName, String mImage){
        name = mName;
        image = mImage;
    }
    public Cast(String mName, String mImage, String mImageDir){
        imageDir = mImageDir;
        name = mName;
        image = mImage;
    }

    public String getImageDir(){return imageDir;}

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImageDir(String imageDir){this.imageDir = imageDir;}


}
