package com.peterung.popularmovies.data.repository;

import com.peterung.popularmovies.data.provider.ReviewTable;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public class ReviewRepositoryImpl implements ReviewRepository {

    private BriteContentResolver mContentResolver;

    public ReviewRepositoryImpl(BriteContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    @Override
    public Observable<SqlBrite.Query> getReviews(long movieId) {
        return mContentResolver.createQuery(
                ReviewTable.CONTENT_URI,
                null,
                "movie_id = ?",
                new String[] {String.valueOf(movieId)},
                null,
                true
        );
    }
}
