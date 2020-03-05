package com.example.hesham.popularmovies.Objects;

public class Review {
    private String author;
    private String content;
    private String url;


    public Review(String mAuthor, String mContent, String mUrl){
        author = mAuthor;
        content = mContent;
        url = mUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
