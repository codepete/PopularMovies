package com.peterung.popularmovies.data.repository;

import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public interface MovieRepository {

    Observable<SqlBrite.Query> getMovies(String filter);
    Observable<SqlBrite.Query> getMovieByInternalId(long id);
    Observable<SqlBrite.Query> getMovieByExternalId(long id);
    void setMovieFavoriteField(long extMovieId, boolean favorite);
}
