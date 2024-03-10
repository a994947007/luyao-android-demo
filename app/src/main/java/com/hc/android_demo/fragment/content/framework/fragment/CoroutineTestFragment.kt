package com.hc.android_demo.fragment.content.framework.fragment

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment
import com.hc.android_demo.fragment.content.mvvm.vm.CoroutineTestViewModel
import com.hc.util.ToastUtils
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * kotlin中的协程只是一个线程框架，方便调用
 */
@ARouter(path = FragmentConstants.COROUTINE_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class CoroutineTestFragment: SimpleRecyclerFragment() {

    private var mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var mainScope2 = MainScope()
    private var globalJob: Job? = null
    private var viewModel = CoroutineTestViewModel()

    init {
        addItem("协程HelloWord", this::onCoroutineHelloWord)
        addItem("协程创建的另一种方式", this::onCoroutineHelloWord2)
        addItem("协程调度器-Dispatchers.Main", this::onCoroutineDispatchersMain)
        addItem("协程调度器-Dispatchers.IO", this::onCoroutineDispatchersIO)
        addItem("协程调度器-Dispatchers.Default", this::onCoroutineDispatchersDefault)
        addItem("作用域-GlobalScope", this::onGlobalScope)
        addItem("作用域-MainScope", this::onMainScope)
        addItem("作用域-ViewModelScope", this::onViewModelScope)
        addItem("作用域-LifecycleScope", this::onLifecycleScope)
        addItem("启动方式-launch", this::onLaunchTest)
    }

    private fun onCoroutineHelloWord() {
        mainScope.launch(Dispatchers.Main) {
            val data = fetchData()
            ToastUtils.show(data)
        }
    }

    private fun onCoroutineHelloWord2() {
        val continuation = suspend {
            Log.d(TAG, "suspend block")
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                Log.d(TAG, "Coroutine End: $result")
            }
        })
        continuation.resume(Unit)
    }

    /**
     * Main，主线程使用
     */
    private fun onCoroutineDispatchersMain() {
        mainScope.launch(Dispatchers.Main) {
            Log.d(TAG, "线程名: ${Thread.currentThread().name}")
        }
    }

    /**
     * IO，网络，磁盘
     */
    private fun onCoroutineDispatchersIO() {
        mainScope.launch(Dispatchers.IO) {
            Log.d(TAG, "线程名: ${Thread.currentThread().name}")
        }
    }

    /**
     * Default，计算密集型
     */
    private fun onCoroutineDispatchersDefault() {
        mainScope.launch(Dispatchers.Default) {
            Log.d(TAG, "线程名: ${Thread.currentThread().name}")
        }
    }

    /**
     * 生命周期是process级别的，不会自动销毁
     */
    private fun onGlobalScope() {
        globalJob = GlobalScope.launch {
            Log.d(TAG, "作用域：$this，线程名: ${Thread.currentThread().name}")
        }
    }

    /**
     * 主线程中使用
     */
    private fun onMainScope() {
        mainScope2.launch {
            Log.d(TAG, "作用域：$this，线程名: ${Thread.currentThread().name}")
        }
    }

    /**
     * viewModel作用域
     */
    private fun onViewModelScope() {
        viewModel.refreshUserNameText()
    }

    /**
     * lifecycle作用域
     */
    private fun onLifecycleScope() {
        lifecycleScope.launch {
            Log.d(TAG, "作用域：$this，线程名: ${Thread.currentThread().name}")
        }
    }

    private fun onLaunchTest() {
        mainScope.launch {
            Log.d(TAG, "launch的方式启动协程")
        }
    }

    private fun onDefferTest() {
        mainScope.launch {
            // 只能作为子协程使用
            val deffer = mainScope.async {
                Log.d(TAG, "async的方式启动协程")
            }
            deffer.await()
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
        globalJob?.cancel()
        mainScope2.cancel()
    }

    companion object {
        const val TAG = "CoroutineTestFragment"
    }

}