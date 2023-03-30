package com.hc.android_demo.fragment.content.mvi;

import androidx.lifecycle.LifecycleOwner;

import com.hc.support.mvi.MVIPresenter;

public class UserPresenter extends MVIPresenter<User, UserViewModel, UserView> {

    public UserPresenter(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @Override
    public UserViewModel onCreateViewModel() {
        return new UserViewModel();
    }

    @Override
    public UserView onCreateView() {
        return new UserView();
    }

    @Override
    protected void onBind() {
        super.onBind();
        vm.dispatch(new UserAction(User.of("https://img1.baidu.com/it/u=3709586903,1286591012&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1680368400&t=ca5b085d6d7d3ad0ea0785e8369d479a", "abc")));
    }
}
