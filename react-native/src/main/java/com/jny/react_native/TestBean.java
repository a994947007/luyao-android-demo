package com.jny.react_native;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestBean {

    @SerializedName("feedId")
    public int feedId;

    @SerializedName("name")
    public String name;

    @SerializedName("urls")
    public List<String> urls;

    @SerializedName("otherBeans")
    public List<OtherBean> otherBeans;
}

class OtherBean {
    @SerializedName("name")
    String name;
}