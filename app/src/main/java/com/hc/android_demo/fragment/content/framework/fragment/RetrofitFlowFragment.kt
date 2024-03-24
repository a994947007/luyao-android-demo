package com.hc.android_demo.fragment.content.framework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.facebook.drawee.view.SimpleDraweeView
import com.hc.android_demo.R
import com.hc.android_demo.fragment.content.framework.net.TestApi
import com.hc.base.fragment.LuFragment
import com.hc.support.singleton.Singleton
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ARouter(
    path = FragmentConstants.RETROFIT_FLOW_TEST_FRAGMENT_ID,
    group = FragmentConstants.FRAMEWORK
)
class RetrofitFlowFragment: LuFragment() {

    private lateinit var imageView1: SimpleDraweeView
    private lateinit var imageView2: SimpleDraweeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_download_image_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageView1 = view.findViewById(R.id.image1)
        imageView2 = view.findViewById(R.id.image2)

        lifecycleScope.launch {
            flow {
                val list = Singleton.get(TestApi::class.java)
                    .getUsers()
                emit(list)
            }.collect{
                imageView1.setImageURI(it.userModelList[0].avatar)
            }
        }

        lifecycleScope.launch {
            Singleton.get(TestApi::class.java)
                .getUser()
                .collect {
                    imageView2.setImageURI(it.data.avatar)
                }
        }
    }
}