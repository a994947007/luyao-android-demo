package com.hc.android_demo.fragment.content.mvi.v1;

import com.hc.support.mvi.Action;
import com.hc.support.mvi.BaseViewModel;

public class UserViewModel extends BaseViewModel<User> {

    @Override
    public void dispatch(Action action) {
        if (action instanceof UserAction) {
            uiState.set(((UserAction) action).user);
        }
    }
}
