package com.example.android.giffinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixedHeight {

    @SerializedName("url")
    @Expose
    private String url;

    public FixedHeight() {
    }

    public FixedHeight(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}