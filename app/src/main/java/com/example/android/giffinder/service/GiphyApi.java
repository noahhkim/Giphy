package com.example.android.giffinder.service;

import com.example.android.giffinder.model.GiphyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GiphyApi {
    @GET("/v1/gifs/search")
    Call<GiphyResponse> getSearchResults();
}
