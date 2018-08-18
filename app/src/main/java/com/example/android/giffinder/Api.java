package com.example.android.giffinder;

import com.example.android.giffinder.model.Gif;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("/v1/gifs/search")
    Call<List<Gif>> getGifs();


}
