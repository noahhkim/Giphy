package com.example.android.giffinder;

import com.example.android.giffinder.model.GiphyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApi {
    /**
     * @param searchString Search string to find gifs.
     * @param limit        Limit results.
     * @param apiKey       Api key for Giphy.
     * @return Response of search results.
     */
    @GET("/v1/gifs/search")
    Call<GiphyResponse> getSearchResults(@Query("q") final String searchString,
                                         @Query("limit") final int limit,
                                         @Query("api_key") final String apiKey);
}
