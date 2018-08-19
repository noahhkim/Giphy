package com.example.android.giffinder.data.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "gif_table")
public class Gif {
    @PrimaryKey(autoGenerate = true)
    private int gifId;

    @ColumnInfo(name = "gif_url")
    private String gifUrl;

    public Gif(@NonNull String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public int getGifId() {
        return gifId;
    }

    public void setGifId(int gifId) {
        this.gifId = gifId;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }
}
