package com.hc.android_demo.fragment.content.mvi.v1;

public class User {
    public String url;
    public String username;

    public static User of(String url, String username) {
        User user = new User();
        user.url = url;
        user.username = username;
        return user;
    }
}
