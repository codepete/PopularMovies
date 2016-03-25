package com.peterung.popularmovies.data.repository;

import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public interface ReviewRepository {

    Observable<SqlBrite.Query> getReviews(long movieId);
}
