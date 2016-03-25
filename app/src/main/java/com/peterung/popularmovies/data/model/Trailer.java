package com.peterung.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Trailer {
    public transient long id;

    @SerializedName("id")
    public String trailerId;

    public String key;
    public String name;
    public String site;
}
