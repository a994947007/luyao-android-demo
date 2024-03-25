package com.hc.android_demo.fragment.content.mvi.v2

import android.view.View
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.hc.android_demo.R
import com.hc.support.mvi2.BaseView
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class UserView : BaseView<User, UserViewModel>() {
    private lateinit var usernameTv: TextView
    private lateinit var imageView: SimpleDraweeView

    override fun onViewCreated(rootView: View) {
        imageView = rootView.findViewById(R.id.header_iv)
        usernameTv = rootView.findViewById(R.id.user_name_tv)
    }

    override fun onObserveData(userViewModel: UserViewModel) {
        viewScope.launch {
            userViewModel.observeState()
                .filterNotNull()
                .collect {
                    imageView.setImageURI(it.url)
                    usernameTv.text = it.username
                }
        }
    }
}