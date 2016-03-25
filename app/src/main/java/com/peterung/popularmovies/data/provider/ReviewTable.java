package com.peterung.popularmovies.data.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ReviewTable extends BaseTable {
    public static final String TABLE_NAME = "review";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String REVIEW_ID = "review_id";
    public static final String MOVIE_ID = "movie_id";
    public static final String URL = "url";
    public static final String CONTENT = "content";
    public static final String AUTHOR = "author";

    public static final int COL_REVIEW_ID = 1;
    public static final int COL_MOVIE_ID = 2;
    public static final int COL_URL = 3;
    public static final int COL_CONTENT = 4;
    public static final int COL_AUTHOR = 5;

    public static void createDatabase(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                REVIEW_ID + " STRING NOT NULL, " +
                MOVIE_ID + " INTEGER NOT NULL, " +
                URL + " TEXT NOT NULL, " +
                CONTENT + " TEXT NOT NULL, " +
                AUTHOR + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MOVIE_ID + ") REFERENCES " +
                MovieTable.TABLE_NAME + " (" + MovieTable._ID + ") ON DELETE CASCADE, " +

                " UNIQUE (" + REVIEW_ID +  "," + MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }
}
