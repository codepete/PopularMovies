package com.peterung.popularmovies.data.model;

import java.util.List;

public class MoviesResponse {
    public int page;
    public List<Movie> results;
    public int totalPages;
    public int totalResults;
}
