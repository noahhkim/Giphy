package com.example.android.giffinder.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.giffinder.data.room.Gif;
import com.example.android.giffinder.data.room.GifDao;
import com.example.android.giffinder.data.room.GifDatabase;

import java.util.List;

public class GifRepository {
    private GifDao mGifDao;
    private LiveData<List<Gif>> mAllGifs;

    GifRepository(Application application) {
        GifDatabase db = GifDatabase.getGifDatabase(application);
        mGifDao = db.gifDao();
        mAllGifs = mGifDao.getAllGifs();
    }

    LiveData<List<Gif>> getAllGifs() {
        return mAllGifs;
    }

    public void insert(Gif gif) {
        new insertAsyncTask(mGifDao).execute(gif);
    }

    public void deleteAll() {
        new deleteAsyncTask(mGifDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Gif, Void, Void> {

        private GifDao mAsyncTaskDao;

        insertAsyncTask(GifDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Gif... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Gif, Void, Void> {

        private GifDao mAsyncTaskDao;

        deleteAsyncTask(GifDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Gif... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}


