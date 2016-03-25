package com.peterung.popularmovies.data.api;

import com.peterung.popularmovies.data.model.Movie;
import com.peterung.popularmovies.data.model.MoviesResponse;
import com.peterung.popularmovies.data.model.ReviewsResponse;
import com.peterung.popularmovies.data.model.TrailersResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface MovieService {

    @GET("movie/{id}")
    Observable<Movie> getMovie(@Path("id") long id);

    @GET("movie/{id}/videos")
    Observable<TrailersResponse> getTrailers(@Path("id") long id);

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getReviews(@Path("id") long id);

    @GET("discover/movie")
    Observable<MoviesResponse> getMovies(@QueryMap Map<String, String> filters);

}
