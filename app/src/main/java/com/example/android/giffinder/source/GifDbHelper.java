package com.example.android.giffinder.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.giffinder.source.GifContract.GifEntry;


public class GifDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gif.db";

    public GifDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GIFS_TABLE = "CREATE TABLE " + GifEntry.TABLE_NAME + " (" +
                GifEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GifEntry.COLUMN_URL + " TEXT NOT NULL " + " );";
        db.execSQL(SQL_CREATE_GIFS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GifEntry.TABLE_NAME);
        onCreate(db);
    }
}
