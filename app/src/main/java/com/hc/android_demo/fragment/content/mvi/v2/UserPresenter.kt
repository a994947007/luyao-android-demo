package com.hc.android_demo.fragment.content.mvi.v2

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.hc.support.mvi2.MVIPresenter

class UserPresenter(private val viewModelStoreOwner: ViewModelStoreOwner, lifecycleOwner: LifecycleOwner)
    : MVIPresenter<User, UserViewModel, UserView>(lifecycleOwner) {

    override fun onCreateViewModel(): UserViewModel {
        return ViewModelProvider(viewModelStoreOwner).get(UserViewModel::class.java)
    }

    override fun onCreateView(): UserView {
        return UserView()
    }

    override fun onBind() {
        super.onBind()
        vm.updateUIState {
            User.of(
                "https://img1.baidu.com/it/u=3709586903,1286591012&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1680368400&t=ca5b085d6d7d3ad0ea0785e8369d479a",
                "abc"
            )
        }
    }
}