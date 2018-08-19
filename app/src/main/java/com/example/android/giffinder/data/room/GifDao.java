package com.example.android.giffinder.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GifDao {
    @Query("SELECT * FROM gif_table")
    LiveData<List<Gif>> getAllGifs();

    @Insert
    void insert(Gif gif);

    @Insert
    void insertAll(Gif... gifs);

    @Delete
    void delete(Gif gif);

    @Query("DELETE FROM gif_table")
    void deleteAll();
}
