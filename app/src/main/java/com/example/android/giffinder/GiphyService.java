package com.example.android.giffinder;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class GiphyService extends IntentService {

    public GiphyService() {
        super("GiphyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
