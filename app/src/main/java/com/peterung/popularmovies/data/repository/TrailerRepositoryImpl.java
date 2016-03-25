package com.peterung.popularmovies.data.repository;

import com.peterung.popularmovies.data.provider.VideoTable;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public class TrailerRepositoryImpl implements TrailerRepository {

    private BriteContentResolver mContentResolver;

    public TrailerRepositoryImpl(BriteContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    @Override
    public Observable<SqlBrite.Query> getTrailers(long movieId) {
        return mContentResolver.createQuery(
                VideoTable.CONTENT_URI,
                null,
                "movie_id = ?",
                new String[] {String.valueOf(movieId)},
                null,
                true
        );
    }
}
