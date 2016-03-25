package com.peterung.popularmovies.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class BaseTable implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.peterung.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
