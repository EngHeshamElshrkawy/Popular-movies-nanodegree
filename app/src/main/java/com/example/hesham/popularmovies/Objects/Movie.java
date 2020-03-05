package com.example.hesham.popularmovies.Objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "movies_table")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int serial;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "plot")
    private String plot;
    @ColumnInfo(name = "rating")
    private double rating;
    @ColumnInfo(name = "id")
    private int id;
    @Ignore
    private String object;
    @ColumnInfo(name = "released")
    private  String released;
    @ColumnInfo(name = "reviews")
    private String reviews;
    @ColumnInfo(name = "casts")
    private String casts;
    @ColumnInfo(name = "recommendations")
    private String recommendations;
    @ColumnInfo(name = "runtime")
    private String runtime;
    @ColumnInfo(name = "imagedir")
    private String imageDir;
    /*
    @ColumnInfo(name = "castdir")
    private String castDir;
    @ColumnInfo(name = "castdir1")
    private String castDir1;
    @ColumnInfo(name = "castdir2")
    private String castDir2;
    @ColumnInfo(name = "recommenddir")
    private String recommendDir;
    @ColumnInfo(name = "recommenddir1")
    private String recommendDir1;
    @ColumnInfo(name = "recommenddir2")
    private String recommendDir2;
    */


    /*
    , String mCastDir, String mCastDir1, String mCastDir2
                , String mRecommendDir, String mRecommendDir1, String mRecommendDir2*/


    public Movie(int mSerial, int mId, String mName, String mPlot, String mImage, String mReleased,
                 double mRating, String mReviews, String mCasts, String mRecommendations,
                 String mRuntime, String mImageDir)
    {
        /*
        recommendDir = mRecommendDir;
        recommendDir1 = mRecommendDir1;
        recommendDir2 = mRecommendDir2;
        castDir2 = mCastDir2;
        castDir1 = mCastDir1;
        castDir = mCastDir;
        */
        imageDir = mImageDir;

        runtime = mRuntime;
        name = mName;
        plot = mPlot;
        image = mImage;
        rating = mRating;
        id = mId;
        released = mReleased;
        serial = mSerial;
        reviews = mReviews;
        casts = mCasts;
        recommendations = mRecommendations;
    }
    @Ignore
    public Movie(int mId, String mName, String mPlot, String mImage, String mReleased,
                 double mRating, String mReviews, String mCasts, String mRecommendations,
                 String mRuntime, String mImageDir){
        /*
        recommendDir = mRecommendDir;
        recommendDir1 = mRecommendDir1;
        recommendDir2 = mRecommendDir2;
        castDir2 = mCastDir2;
        castDir1 = mCastDir1;
        castDir = mCastDir;
        */
        imageDir = mImageDir;

        runtime = mRuntime;
        name = mName;
        plot = mPlot;
        image = mImage;
        rating = mRating;
        id = mId;
        released = mReleased;
        reviews = mReviews;
        casts = mCasts;
        recommendations = mRecommendations;
    }
    @Ignore
    public Movie(String mName, String mPlot, String mImage, double mRating, int mId, String mReleased, String mObject){
        name = mName;
        plot = mPlot;
        image = mImage;
        rating = mRating;
        id = mId;
        object = mObject;
        released = mReleased;
    }

    @Ignore
    public Movie(String mName, String mPlot, String mImage, double mRating, int mId, String mReleased, String mObject, String mImageDir){
        imageDir = mImageDir;
        name = mName;
        plot = mPlot;
        image = mImage;
        rating = mRating;
        id = mId;
        object = mObject;
        released = mReleased;
    }


    public  Movie(){
    }

    public String getImageDir(){
        return imageDir;
    }



    public String getRuntime() {
        return runtime;
    }
    public String getRecommendations(){return recommendations;}
    public int getSerial() {
        return serial;
    }
    public String getCasts() {
        return casts;
    }
    public String getReviews() {
        return reviews;
    }
    public String getReleased() {
        return released;
    }
    public String getObject() {
        return object;
    }
    public double getRating() {
        return rating;
    }
    public String getImage() {
        return image;
    }
    public String getName() {
        return name;
    }
    public String getPlot() {
        return plot;
    }
    public int getId() {
        return id;
    }


    public void setImageDir(String imageDir){
        this.imageDir = imageDir;
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setCasts(String casts) {
        this.casts = casts;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setReleased(String released) {
        this.released = released;
    }
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
    public void setSerial(int serial) {
        this.serial = serial;
    }
    public void setRecommendations(String recommendations){
        this.recommendations = recommendations;
    }
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }


}


