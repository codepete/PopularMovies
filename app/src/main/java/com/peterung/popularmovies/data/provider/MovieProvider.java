package com.peterung.popularmovies.data.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class MovieProvider extends ContentProvider {
    public final String LOG_TAG = MovieProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.peterung.popularmovies";

    private static final int MOVIE_CONTENT_URI = 0;

    private static final int MOVIE_MOVIE_ID = 1;

    private static final int REVIEW_CONTENT_URI = 2;

    private static final int VIDEO_CONTENT_URI = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "movie", MOVIE_CONTENT_URI);
        MATCHER.addURI(AUTHORITY, "movie/#", MOVIE_MOVIE_ID);
        MATCHER.addURI(AUTHORITY, "review", REVIEW_CONTENT_URI);
        MATCHER.addURI(AUTHORITY, "video", VIDEO_CONTENT_URI);
    }

    private SQLiteOpenHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }



    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int match = MATCHER.match(uri);
        if (match == -1) {
            throw new UnsupportedOperationException();
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table = uri.getPathSegments().get(0);

        db.beginTransaction();
        int count = 0;
        try {
            for (ContentValues cv : values) {
                String colName = String.format("%s_id", table);
                String where = String.format("%s = ?", colName);
                String[] whereArgs = new String[] {cv.getAsString(colName)};
                int affected = db.update(table, cv, where, whereArgs);
                if (affected == 0) {
                    long id = db.insert(table, null, cv);
                    if (id > 0) count++;
                }
            }

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.w(LOG_TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch(MATCHER.match(uri)) {
            case MOVIE_CONTENT_URI: {
                return "vnd.android.cursor.dir/movie";
            }
            case MOVIE_MOVIE_ID: {
                return "vnd.android.cursor.item/movie";
            }
            case REVIEW_CONTENT_URI: {
                return "vnd.android.cursor.dir/review";
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = MATCHER.match(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        String table = uri.getPathSegments().get(0);
        if (match == MOVIE_MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            selection = "_id = ?";
            selectionArgs = new String[] {id};
        }

        return mOpenHelper.getReadableDatabase().query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = MATCHER.match(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown URI " + uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table = uri.getPathSegments().get(0);

        final long id = db.insertOrThrow(table, null, values);

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = MATCHER.match(uri);
        if (match == -1) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table = uri.getPathSegments().get(0);

        if (match == MOVIE_MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            selection = "_id = ?";
            selectionArgs = new String[] {id};
        }

        int updatedCount = db.update(table, values, selection, selectionArgs);

        if (updatedCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedCount;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = MATCHER.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Prevent deletion of entire table
        if (selection.isEmpty()) {
            selection = "1";
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table = uri.getPathSegments().get(0);

        if (match == MOVIE_MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            selection = "_id = ?";
            selectionArgs = new String[] {id};
        }

        int count = db.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }


    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }



}
