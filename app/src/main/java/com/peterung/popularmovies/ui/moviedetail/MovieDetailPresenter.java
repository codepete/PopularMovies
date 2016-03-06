package com.peterung.popularmovies.ui.moviedetail;

import com.peterung.popularmovies.data.api.MovieService;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MovieDetailPresenter implements MovieDetailContract.UserActionListener {

    MovieDetailContract.View mView;
    CompositeSubscription mSubscriptions;
    MovieService mMovieService;

    public MovieDetailPresenter(MovieDetailContract.View view, MovieService service) {
        mSubscriptions = new CompositeSubscription();
        mView = view;
        mMovieService = service;
    }

    @Override
    public void loadMovieInfo(int movieId) {
        if (movieId == -1) {
            mView.showNoMovieSelected();
            return;
        }

        Subscription subscription = mMovieService.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movie -> mView.showMovieInfo(movie));

        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }

}
