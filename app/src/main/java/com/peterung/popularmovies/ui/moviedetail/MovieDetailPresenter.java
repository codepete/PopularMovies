package com.peterung.popularmovies.ui.moviedetail;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.data.repository.MovieRepository;
import com.peterung.popularmovies.data.repository.ReviewRepository;
import com.peterung.popularmovies.data.repository.TrailerRepository;
import com.peterung.popularmovies.utility.MovieDbHelper;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MovieDetailPresenter implements MovieDetailContract.UserActionListener {

    MovieDetailContract.View mView;
    CompositeSubscription mSubscriptions;
    MovieRepository mMovieRepository;
    ReviewRepository mReviewRepository;
    TrailerRepository mTrailerRepository;
    MovieDbHelper mMovieDbHelper;

    public MovieDetailPresenter(MovieRepository movieRepository,
                                ReviewRepository reviewRepository,
                                TrailerRepository trailerRepository,
                                MovieDbHelper movieDbHelper) {
        mSubscriptions = new CompositeSubscription();
        mMovieRepository = movieRepository;
        mReviewRepository = reviewRepository;
        mTrailerRepository = trailerRepository;
        mMovieDbHelper = movieDbHelper;
    }

    @Override
    public void loadMovieInfo(long movieId) {
        if (movieId == -1) {
            mView.showNoMovieSelected();
            return;
        }

        mMovieDbHelper.syncReviewsAndTrailersImmediately(movieId);

        Subscription subscription = mMovieRepository.getMovieByExternalId(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> mView.showMovieInfo(query));

        mSubscriptions.add(subscription);
    }

    @Override
    public void loadReviews(long movieId) {
        if (movieId == -1) {
            return;
        }

        Subscription subscription = mReviewRepository.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> mView.showReviews(query));

        mSubscriptions.add(subscription);
    }

    @Override
    public void loadTrailers(long movieId) {
        if (movieId == -1) {
            return;
        }

        Subscription subscription = mTrailerRepository.getTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> mView.showTrailers(query));

        mSubscriptions.add(subscription);
    }

    @Override
    public void setView(MovieDetailContract.View view) {
        mView = view;
    }

    @Override
    public void playTrailer(String trailerKey) {
        String youTubeUrl = String.format(Constants.YOUTUBE_URL_FORMAT, trailerKey);
        mView.showTrailer(youTubeUrl);
    }

    @Override
    public void setFavorite(long movieId, boolean favorite) {
        if (movieId == -1) {
            return;
        }

        mMovieRepository.setMovieFavoriteField(movieId, favorite);
        mView.showFavorite(favorite);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }

}
