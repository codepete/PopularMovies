package com.peterung.popularmovies.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.peterung.popularmovies.Constants;
import com.peterung.popularmovies.data.provider.MovieTable;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import rx.Observable;

public class MovieRepositoryImpl implements MovieRepository{
    BriteContentResolver mBriteContentResolver;
    ContentResolver mContentResolver;

    public MovieRepositoryImpl(BriteContentResolver briteContentResolver, ContentResolver contentResolver) {
        mBriteContentResolver = briteContentResolver;
        mContentResolver = contentResolver;
    }

    @Override
    public Observable<SqlBrite.Query> getMovies(String filter) {

        String where = null;
        String[] whereArgs = null;
        String sortBy = null;
        switch (filter) {
            case Constants.TOP_RATED_FILTER:
                where = "vote_count >= ?";
                whereArgs = new String[] {"1000"};
                sortBy = "vote_count DESC";
                break;
            case Constants.POPULAR_FILTER:
                sortBy = "popularity DESC";
                break;
            case Constants.FAVORITE_FILTER:
                where = "favorite = ?";
                whereArgs = new String[] {"1"};
            default:
                sortBy = "popularity DESC";
                break;
        }

        return mBriteContentResolver.createQuery(
                MovieTable.CONTENT_URI,
                null,
                where,
                whereArgs,
                sortBy,
                true
        );
    }

    @Override
    public Observable<SqlBrite.Query> getMovieByInternalId(long id) {
        return mBriteContentResolver.createQuery(
                MovieTable.getContentUriWithId(id),
                null,
                null,
                null,
                null,
                true
        );
    }

    @Override
    public Observable<SqlBrite.Query> getMovieByExternalId(long id) {
        return mBriteContentResolver.createQuery(
                MovieTable.CONTENT_URI,
                null,
                "movie_id = ?",
                new String[] {String.valueOf(id)},
                null,
                true
        );
    }

    @Override
    public void setMovieFavoriteField(long extMovieId, boolean favorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieTable.FAVORITE, favorite ? 1 : 0);
        mContentResolver.update(
                MovieTable.CONTENT_URI,
                contentValues,
                "movie_id = ?",
                new String[] {String.valueOf(extMovieId)}
        );
    }
}
