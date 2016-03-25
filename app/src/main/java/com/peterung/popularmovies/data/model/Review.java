package com.peterung.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    public transient long id;

    @SerializedName("id")
    public String reviewId;

    public String author;
    public String content;
    public String url;
}
