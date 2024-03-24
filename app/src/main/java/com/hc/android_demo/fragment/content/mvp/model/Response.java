package com.hc.android_demo.fragment.content.mvp.model;

import com.google.gson.annotations.SerializedName;

public class Response<T> {
    @SerializedName("data")
    public T data;
}
