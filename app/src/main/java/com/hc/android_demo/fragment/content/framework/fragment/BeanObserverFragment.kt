package com.hc.android_demo.fragment.content.framework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.demo.rxandroid.disposable.Disposable
import com.android.demo.rxandroid.plugin.Functions
import com.hc.android_demo.R
import com.hc.android_demo.fragment.content.framework.bean.User
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import com.hc.base.framework.notifyChanged
import com.hc.base.framework.observable

@ARouter(
    path = FragmentConstants.BEAN_OBSERVER_FRAGMENT_ID,
    group = FragmentConstants.FRAMEWORK
)
class BeanObserverFragment: LuFragment() {

    private val user = User("123123","abc", "哈哈", false)
    private val user2 = User("123123", "def", "呵呵", false)
    private var id = 0
    private var disposable: Disposable ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bean_observer_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.username_change_btn).setOnClickListener {
            user.name = "哈哈" + ++id
            user.notifyChanged()
        }

        val textView = view.findViewById<TextView>(R.id.user_name_tv)
        disposable = user2.observable().subscribe({
            textView.text = it.name
        }, Functions.emptyConsumer())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null && disposable?.isDisposable == false) {
            disposable?.dispose()
            disposable = null
        }
    }


}