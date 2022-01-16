package com.hc.android_demo.fragment.content.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserListResponse implements Serializable {

    @SerializedName("data")
    public List<UserModel> userModelList;
}
