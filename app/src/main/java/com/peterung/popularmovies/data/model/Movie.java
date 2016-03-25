package com.peterung.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    public transient int id;

    @SerializedName("id")
    public long movieId;

    public String title;
    public String overview;
    public String releaseDate;
    public String posterPath;
    public String originalLanguage;
    public String originalTitle;
    public boolean video;
    public boolean adult;
    public float popularity;
    public int voteCount;
    public float voteAverage;
    public String backdropPath;
    public int[] genreIds;


}
