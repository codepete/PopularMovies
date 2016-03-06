package com.peterung.popularmovies.ui.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.peterung.popularmovies.data.api.Movie;
import com.peterung.popularmovies.data.api.MovieService;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MoviesPresenter implements MoviesContract.UserActionsListener {

    public final String TAG = getClass().getSimpleName();
    public final String POPULAR_FILTER = "popularity.desc";
    public final String TOP_RATED_FILTER = "vote_average.desc";

    MoviesContract.View mMoviesView;
    MovieService mMovieService;
    CompositeSubscription mSubscriptions;



    public MoviesPresenter(MovieService service, @NonNull MoviesContract.View view) {
        mMoviesView = view;
        mMovieService = service;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadMovies(String filter) {
        HashMap<String, String> filters = new HashMap<>();

        if (filter == null) {
            filter = POPULAR_FILTER;
        }

        switch (filter) {
            case TOP_RATED_FILTER:
                filters.put("sort_by", TOP_RATED_FILTER);
                filters.put("vote_count.gte", "1000");
                break;
            case POPULAR_FILTER:
                filters.put("sort_by", POPULAR_FILTER);
                break;
            default:
                filters.put("sort_by", POPULAR_FILTER);
                break;
        }

        mMovieService.getMovies(filters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        moviesResponse -> mMoviesView.showMoviesList(moviesResponse.results),
                        error -> Log.d(TAG, error.getMessage())
                );

    }

    @Override
    public void openMovieDetails(int pos, Movie movie) {
        mMoviesView.showMovieDetails(pos, movie);
    }


    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }
}
