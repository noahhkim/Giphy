package com.example.android.giffinder.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Gif.class}, version = 1)
public abstract class GifDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "gif_database";

    private static GifDatabase INSTANCE;

    public abstract GifDao gifDao();

    public static GifDatabase getGifDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (GifDatabase.class) {
                // Create gif database
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        GifDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return INSTANCE;
    }
}