package com.hc.recycler;

public class UserModel {
    public String id;
    public String username;
    public String avatar;
    public String msg;

    public UserModel() {}

    public UserModel(UserModel userModel) {
        this.id = userModel.id;
        this.username = userModel.username;
        this.avatar = userModel.avatar;
        this.msg = userModel.msg;
    }

    public UserModel(String id, String username, String avatar, String msg) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.msg = msg;
    }

}
