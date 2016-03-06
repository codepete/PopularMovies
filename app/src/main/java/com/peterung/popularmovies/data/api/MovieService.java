package com.peterung.popularmovies.data.api;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface MovieService {

    @GET("movie/{id}")
    Observable<Movie> getMovie(@Path("id") int id);

    @GET("discover/movie")
    Observable<MoviesResponse> getMovies(@QueryMap Map<String, String> filters);

}
