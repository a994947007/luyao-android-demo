package com.hc.android_demo.fragment.content.mvi.v2;

import com.hc.support.mvi2.UIState;

public class User implements UIState {
    public String url;
    public String username;

    public static User of(String url, String username) {
        User user = new User();
        user.url = url;
        user.username = username;
        return user;
    }
}
