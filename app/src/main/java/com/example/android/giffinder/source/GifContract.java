package com.example.android.giffinder.source;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class GifContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.giffinder";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GIFS = "gifs";

    public static final class GifEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GIFS).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GIFS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GIFS;

        // Table name
        public static final String TABLE_NAME = "gifs";

        // Column names
        public static final String COLUMN_URL = "url";

        public static final String[] COLUMNS = {
                GifEntry.TABLE_NAME + "." + GifEntry._ID,
        };

        public static Uri buildGifUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

        public static final int COL_ID = 0;
        public static final int COL_URL = 1;
    }
}
