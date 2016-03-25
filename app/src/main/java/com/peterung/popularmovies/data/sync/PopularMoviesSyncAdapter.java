package com.peterung.popularmovies.data.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.PopularMoviesApplication;
import com.peterung.popularmovies.data.api.MovieService;
import com.peterung.popularmovies.data.model.Movie;
import com.peterung.popularmovies.data.model.Review;
import com.peterung.popularmovies.data.model.Trailer;
import com.peterung.popularmovies.data.provider.MovieTable;
import com.peterung.popularmovies.data.provider.ReviewTable;
import com.peterung.popularmovies.data.provider.VideoTable;
import com.peterung.popularmovies.data.repository.SharedPreferencesRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class PopularMoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = PopularMoviesSyncAdapter.class.getSimpleName();

    @Inject
    public MovieService mMovieService;

    @Inject
    public SharedPreferencesRepository mSharedPreferencesRepository;

    public PopularMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        ((PopularMoviesApplication) context).getComponent().inject(this);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("onPerformSync", "onPerformSync Called.");
        Log.d("onPerformSync", "Ooooo: " + extras.getLong("movie_id", -1));
        long movieId = extras.getLong("movie_id", -1);
        if (movieId > 0) {
            Log.d("onPerformSync", "Getting movie info.");
            mMovieService.getReviews(movieId)
                    .subscribe(
                        reviewsResponse -> addReviews(reviewsResponse.results, movieId)
                    );

            mMovieService.getTrailers(movieId)
                    .subscribe(
                        trailersResponse -> addTrailers(trailersResponse.results, movieId)
                    );

        } else {
            Log.d("onPerformSync", "Getting movie list.");
            HashMap<String, String> filters = new HashMap<>();

            String filter = mSharedPreferencesRepository.getFilterType();

            switch (filter) {
                case Constants.TOP_RATED_FILTER:
                    filters.put("sort_by", Constants.TOP_RATED_FILTER);
                    filters.put("vote_count.gte", "1000");
                    break;
                case Constants.POPULAR_FILTER:
                    filters.put("sort_by", Constants.POPULAR_FILTER);
                    break;
                default:
                    filters.put("sort_by", Constants.POPULAR_FILTER);
                    break;
            }

            mMovieService.getMovies(filters)
                    .subscribe(
                            moviesResponse -> addMovies(moviesResponse.results),
                            error -> Log.d(LOG_TAG, error.getMessage())
                    );
        }
    }

    public int addReviews(List<Review> reviews, long movieId) {
        Log.d("onPerformSync", "Returned Review Count: " + reviews.size());
        ArrayList<ContentValues> cVVector = new ArrayList<>();

        for (Review review: reviews) {
            ContentValues reviewValues = new ContentValues();

            reviewValues.put(ReviewTable.MOVIE_ID, movieId);
            reviewValues.put(ReviewTable.REVIEW_ID, review.reviewId);
            reviewValues.put(ReviewTable.AUTHOR, review.author);
            reviewValues.put(ReviewTable.CONTENT, review.content);
            reviewValues.put(ReviewTable.URL, review.url);


            cVVector.add(reviewValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] values = new ContentValues[cVVector.size()];
            cVVector.toArray(values);

            return getContext().getContentResolver().bulkInsert(ReviewTable.CONTENT_URI, values);
        }

        return 0;
    }

    public int addTrailers(List<Trailer> trailers, long movieId) {
        Log.d("onPerformSync", "Returned Trailer Count: " + trailers.size());
        ArrayList<ContentValues> cVVector = new ArrayList<>();

        for (Trailer trailer: trailers) {
            ContentValues trailerValues = new ContentValues();

            trailerValues.put(VideoTable.MOVIE_ID, movieId);
            trailerValues.put(VideoTable.VIDEO_ID, trailer.trailerId);
            trailerValues.put(VideoTable.KEY, trailer.key);
            trailerValues.put(VideoTable.SITE, trailer.site);
            trailerValues.put(VideoTable.NAME, trailer.name);



            cVVector.add(trailerValues);
        }

        if (cVVector.size() > 0) {
            Log.d("onPerformSync", "Content Values Array Size: " + cVVector.size());
            ContentValues[] values = new ContentValues[cVVector.size()];
            cVVector.toArray(values);

            return getContext().getContentResolver().bulkInsert(VideoTable.CONTENT_URI, values);
        }

        return 0;
    }

    public int addMovies(List<Movie> movies) {
        Log.d("onPerformSync", "Returned Movie Count: " + movies.size());
        ArrayList<ContentValues> cVVector = new ArrayList<>();

        for (Movie movie: movies) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieTable.MOVIE_ID, movie.movieId);
            movieValues.put(MovieTable.ADULT, movie.adult ? 1 : 0);
            movieValues.put(MovieTable.BACKDROP_PATH, movie.backdropPath);
            movieValues.put(MovieTable.ORIGINAL_TITLE, movie.originalTitle);
            movieValues.put(MovieTable.OVERVIEW, movie.overview);
            movieValues.put(MovieTable.POPULARITY, movie.popularity);
            movieValues.put(MovieTable.POSTER_PATH, movie.posterPath);
            movieValues.put(MovieTable.RELEASE_DATE, movie.releaseDate);
            movieValues.put(MovieTable.TITLE, movie.title);
            movieValues.put(MovieTable.VOTE_AVERAGE, movie.voteAverage);
            movieValues.put(MovieTable.VIDEO, movie.video ? 1 : 0);
            movieValues.put(MovieTable.VOTE_COUNT, movie.voteCount);


            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            Log.d("onPerformSync", "Content Values Array Size: " + cVVector.size());
            ContentValues[] values = new ContentValues[cVVector.size()];
            cVVector.toArray(values);

            return getContext().getContentResolver().bulkInsert(MovieTable.CONTENT_URI, values);
        }

        return 0;
    }
}
