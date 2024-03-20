package com.hc.android_demo.fragment.content.framework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.hc.android_demo.R
import com.hc.android_demo.fragment.content.coroutine.downloadImageFlow
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import com.luyao.android.demo.download.download.LuDownload
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.URL

@ARouter(
    path = FragmentConstants.DOWNLOAD_FLOW_TEST_FRAGMENT_ID,
    group = FragmentConstants.FRAMEWORK
)
class DownloadBitmapFlowFragment: LuFragment() {

    private val IMAGE_URL1 =
        "https://img1.baidu.com/it/u=3709586903,1286591012&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
    private val IMAGE_URL2 =
        "https://img2.baidu.com/it/u=3681172266,4264167375&fm=253&fmt=auto&app=138&f=JPEG?w=501&h=500"

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView

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
            LuDownload.getInstance().downloadService
                .downloadImageFlow(URL(IMAGE_URL1))
                .collect {
                    imageView1.setImageBitmap(it)
                }
        }
    }
}