package com.peterung.popularmovies.data.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public final class MovieTable extends BaseTable {

    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String MOVIE_ID = "movie_id";
    public static final String VOTE_COUNT = "vote_count";
    public static final String ADULT = "adult";
    public static final String VIDEO = "video";
    public static final String TITLE = "title";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String RELEASE_DATE = "release_date";
    public static final String OVERVIEW = "overview";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POSTER_PATH = "poster_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String FAVORITE = "favorite";


    public static final int COL_MOVIE_ID = 1;
    public static final int COL_VOTE_COUNT = 2;
    public static final int COL_ADULT = 3;
    public static final int COL_VIDEO = 4;
    public static final int COL_TITLE = 5;
    public static final int COL_ORIGINAL_TITLE = 6;
    public static final int COL_RELEASE_DATE = 7;
    public static final int COL_OVERVIEW = 8;
    public static final int COL_BACKDROP_PATH = 9;
    public static final int COL_POSTER_PATH = 10;
    public static final int COL_POPULARITY = 11;
    public static final int COL_VOTE_AVERAGE = 12;
    public static final int COL_FAVORITE = 13;

    public static void createDatabase(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MOVIE_ID + " INTEGER NOT NULL, " +
                VOTE_COUNT + " INTEGER DEFAULT 0, " +
                ADULT + " INTEGER DEFAULT 0, " +
                VIDEO + " INTEGER DEFAULT 0, " +
                TITLE + " TEXT NOT NULL, " +
                ORIGINAL_TITLE + " TEXT NOT NULL, " +
                RELEASE_DATE + " TEXT NOT NULL, " +
                OVERVIEW + " TEXT NOT NULL, " +
                BACKDROP_PATH + " TEXT NOT NULL, " +
                POSTER_PATH + " TEXT NOT NULL, " +
                POPULARITY + " REAL NOT NULL, " +
                VOTE_AVERAGE + " REAL NOT NULL, " +
                FAVORITE + " INTEGER DEFAULT 0, " +

                " UNIQUE (" + MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    public static Uri getContentUriWithId(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

}
