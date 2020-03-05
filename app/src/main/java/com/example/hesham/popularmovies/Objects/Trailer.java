package com.example.hesham.popularmovies.Objects;

public class Trailer {
    private String key;
    private String name;
    private String type;

    public Trailer(String mKey, String mName, String mType){
        key = mKey;
        name = mName;
        type = mType;
    }

    public String getKey(){
        return key;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }


}
