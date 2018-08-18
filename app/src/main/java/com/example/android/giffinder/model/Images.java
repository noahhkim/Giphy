package com.example.android.giffinder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {
    @SerializedName("fixed_height")
    @Expose
    private FixedHeight fixedHeight;

    public Images() {
    }

    public Images(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }
}
