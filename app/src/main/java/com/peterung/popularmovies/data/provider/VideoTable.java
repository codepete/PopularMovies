package com.peterung.popularmovies.data.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class VideoTable extends BaseTable{
    public static final String TABLE_NAME = "video";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String MOVIE_ID = "movie_id";
    public static final String VIDEO_ID = "video_id";
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";

    public static final int COL_VIDEO_ID = 1;
    public static final int COL_MOVIE_ID = 2;
    public static final int COL_KEY = 3;
    public static final int COL_NAME = 4;
    public static final int COL_SITE = 5;

    public static void createDatabase(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VIDEO_ID + " STRING NOT NULL, " +
                MOVIE_ID + " INTEGER NOT NULL, " +
                KEY + " TEXT NOT NULL, " +
                NAME + " TEXT NOT NULL, " +
                SITE + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + MOVIE_ID + ") REFERENCES " +
                MovieTable.TABLE_NAME + " (" + MovieTable._ID + ") ON DELETE CASCADE, " +

                " UNIQUE (" + VIDEO_ID +  "," + MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

}
