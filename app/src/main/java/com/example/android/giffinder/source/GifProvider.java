package com.example.android.giffinder.source;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.giffinder.source.GifContract.GifEntry;

public class GifProvider extends ContentProvider {

    private GifDbHelper gifDbHelper;
    static final int GIFS = 100;
    static final int GIFS_WITH_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final String gifByIdSelection =
            GifEntry.TABLE_NAME + "." + GifEntry._ID + " = ? ";

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GifContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, GifContract.PATH_GIFS, GIFS);
        matcher.addURI(authority, GifContract.PATH_GIFS + "/#", GIFS_WITH_ID);
        return matcher;
    }

    private Cursor getGifById(Uri uri, String[] projection, String sortOrder) {
        long id = GifEntry.getIdFromUri(uri);
        String[] selectionArgs = new String[]{Long.toString(id)};
        return gifDbHelper.getReadableDatabase().query(
                GifEntry.TABLE_NAME,
                projection,
                gifByIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public GifProvider() {
    }

    @Override
    public boolean onCreate() {
        gifDbHelper = new GifDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case GIFS:
                cursor = gifDbHelper.getReadableDatabase().query(
                        GifEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GIFS_WITH_ID:
                cursor = getGifById(uri, projection, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GIFS:
                return GifEntry.CONTENT_DIR_TYPE;
            case GIFS_WITH_ID:
                return GifEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = gifDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        long _id;
        switch (match) {
            case GIFS: {
                _id = db.insertWithOnConflict(GifEntry.TABLE_NAME, null, contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = GifEntry.buildGifUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = gifDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case GIFS:
                rowsDeleted = db.delete(GifEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GIFS_WITH_ID:
                long id = GifEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(GifEntry.TABLE_NAME,
                        gifByIdSelection, new String[]{Long.toString(id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = gifDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case GIFS:
                rowsUpdated = db.update(GifEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        gifDbHelper.close();
        super.shutdown();
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = gifDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case GIFS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(GifEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
