package com.peterung.popularmovies.data.repository;

import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public interface TrailerRepository {
    Observable<SqlBrite.Query> getTrailers(long movieId);
}
