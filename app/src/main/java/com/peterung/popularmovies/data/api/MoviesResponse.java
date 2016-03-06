package com.peterung.popularmovies.data.api;

import java.util.List;

/**
 * Created by peter on 2/26/16.
 */
public class MoviesResponse {
    public int page;
    public List<Movie> results;
    public int totalPages;
    public int totalResults;
}
