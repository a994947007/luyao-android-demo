package com.hc.android_demo.fragment.content.mvi.v1;

import com.hc.support.mvi.Action;

public class UserAction implements Action {
    public User user;
    public UserAction(User user) {
        this.user = user;
    }
}
