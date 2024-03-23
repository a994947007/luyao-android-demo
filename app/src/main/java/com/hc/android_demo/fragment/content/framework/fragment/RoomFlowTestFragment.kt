package com.hc.android_demo.fragment.content.framework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hc.android_demo.R
import com.hc.android_demo.fragment.content.framework.presenter.RoomFlowDBPresenter
import com.hc.base.fragment.LuFragment
import com.hc.support.mvps.Presenter
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants

@ARouter(path = FragmentConstants.ROOM_FLOW_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class RoomFlowTestFragment: LuFragment() {
    private val mGroupPresenter = Presenter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_db_text_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mGroupPresenter.add(RoomFlowDBPresenter())
        mGroupPresenter.create(view)
        mGroupPresenter.bind(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mGroupPresenter.destroy()
    }
}