package com.peterung.popularmovies.ui.movies;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.data.repository.MovieRepository;
import com.peterung.popularmovies.utility.MovieDbHelper;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MoviesPresenter implements MoviesContract.UserActionsListener {

    private MoviesContract.View mMoviesView;
    private CompositeSubscription mSubscriptions;
    private MovieDbHelper mMovieDbHelper;
    private MovieRepository mMovieRepository;

    public MoviesPresenter(MovieRepository movieRepository, MovieDbHelper movieDbHelper) {
        mSubscriptions = new CompositeSubscription();
        mMovieRepository = movieRepository;
        mMovieDbHelper = movieDbHelper;
    }

    @Override
    public void setView(MoviesContract.View view) {
        mMoviesView = view;
    }

    @Override
    public void loadMovies(String filter) {

        if (!filter.equals(Constants.FAVORITE_FILTER)) {
            mMovieDbHelper.syncMoviesImmediately();
        }
        mSubscriptions.clear();

        Subscription subscription = mMovieRepository.getMovies(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> mMoviesView.showMoviesList(query));

        mSubscriptions.add(subscription);
    }

    @Override
    public void openMovieDetails(long movieId) {
        mMoviesView.showMovieDetails(movieId);
    }


    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }
}
