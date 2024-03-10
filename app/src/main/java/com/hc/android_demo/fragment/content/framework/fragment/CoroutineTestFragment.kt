package com.hc.android_demo.fragment.content.framework.fragment

import com.hc.android_demo.fragment.base.SimpleRecyclerFragment
import com.hc.util.ToastUtils
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import kotlinx.coroutines.*

@ARouter(path = FragmentConstants.COROUTINE_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class CoroutineTestFragment: SimpleRecyclerFragment() {

    private var mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        addItem("协程HelloWord", this::onCoroutineHelloWord)
    }

    private fun onCoroutineHelloWord() {
        mainScope.launch(Dispatchers.Main) {
            val data = fetchData()
            ToastUtils.show(data)
        }
    }

    private suspend fun fetchData(): String {
        return withContext(Dispatchers.IO) {
            delay(500)
            "协程HelloWord"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainScope.cancel()
    }

}