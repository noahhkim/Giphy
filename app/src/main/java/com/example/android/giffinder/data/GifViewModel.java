package com.example.android.giffinder.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.giffinder.data.room.Gif;

import java.util.List;

public class GifViewModel extends AndroidViewModel {
    private GifRepository mRepository;
    private LiveData<List<Gif>> mAllGifs;

    public GifViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GifRepository(application);
        mAllGifs = mRepository.getAllGifs();
    }

    public LiveData<List<Gif>> getAllGifs() {
        return mAllGifs;
    }

    public void insert(Gif gif) {
        mRepository.insert(gif);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
