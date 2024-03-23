package com.hc.android_demo.fragment.content.framework.fragment

import android.content.Intent
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment
import com.hc.base.activity.Constants
import com.hc.base.activity.ContentActivity
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants

@ARouter(path = FragmentConstants.FLOW_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class FlowTestFragment: SimpleRecyclerFragment() {
    init {
        addItem("Flow下载", this::onDownloadFlow)
        addItem("Flow与Room", this::onClickRoomFlow)
    }

    private fun onDownloadFlow() {
        startContentActivity(FragmentConstants.DOWNLOAD_FLOW_TEST_FRAGMENT_ID)
    }

    private fun onClickRoomFlow() {
        startContentActivity(FragmentConstants.ROOM_FLOW_TEST_FRAGMENT_ID)
    }

    override fun startContentActivity(id: String) {
        val intent = Intent(activity, ContentActivity::class.java)
        intent.putExtra(
            Constants.CONTENT_FRAGMENT_KEY,
            "/" + FragmentConstants.FRAMEWORK + "/" + id
        )
        startActivity(intent)
    }
}