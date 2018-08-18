package com.example.android.giffinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("images")
    @Expose
    private FixedHeight images;

    public Data() {
    }

    public Data(FixedHeight images) {
        this.images = images;
    }

    public FixedHeight getImages() {
        return images;
    }

    public void setImages(FixedHeight images) {
        this.images = images;
    }
}