package com.hc.android_demo.fragment.content.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants

@ARouter(path = FragmentConstants.RECYCLER_ITEM_ANIMATOR, group = FragmentConstants.CUSTOM_VIEW)
class RecyclerItemAnimator: LuFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}