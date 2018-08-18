package com.example.android.giffinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GiphyResponse {
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<>();

    public GiphyResponse() {
    }

    public GiphyResponse(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(final List<Data> data) {
        this.data = data;
    }
}
