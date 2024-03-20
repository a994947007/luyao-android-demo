package com.hc.android_demo.fragment.content.framework.fragment

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.hc.android_demo.fragment.base.SimpleRecyclerFragment
import com.hc.android_demo.fragment.content.mvvm.vm.CoroutineTestViewModel
import com.hc.util.ToastUtils
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import kotlin.coroutines.*

/**
 * kotlin中的协程只是一个线程框架，方便调用
 */
@ARouter(path = FragmentConstants.COROUTINE_TEST_FRAGMENT_ID, group = FragmentConstants.FRAMEWORK)
class CoroutineTestFragment: SimpleRecyclerFragment() {

    private var mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var defaultScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
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
        addItem("启动方式-async", this::onAsyncTest)
        addItem("分阶段任务（任务A之后，再任务B、C）", this::onJoinTest)
        addItem("分阶段任务（任务A之后，再任务B、C），async", this::onAsyncJoinTest)
        addItem("分阶段请求（请求A之后，再请求B、C）", this::onAsyncAwaitTest)
        addItem("启动模式-DEFAULT", this::onStartDefault)
        addItem("启动模式-ATOMIC", this::onStartAtomic)
        addItem("启动模式-LAZY", this::onStartLazy)
        addItem("启动模式-UNDISPATCHED", this::onStartUnDispatched)
        addItem("作用域-coroutineScope", this::onCoroutineScope)
        addItem("作用域-supervisorScope", this::onSupervisorScope)
        addItem("CPU密集型任务取消-直接cancel", this::onCancelDefaultJob)
        addItem("CPU密集型任务取消-isActive标记位判断取消", this::onIsActiveDefaultJob)
        addItem("CPU密集型任务取消-ensureActive取消", this::onEnsureActiveDefaultJob)
        addItem("CPU密集型任务取消-yield", this::onYieldDefaultJob)
        addItem("协程资源释放-try_finally", this::onTryFinallyJob)
        addItem("协程资源释放-use", this::onUseClosable)
        addItem("协程已经cancel后又在finally中运行别的协程", this::onNonCancelable)
        addItem("协程超时任务", this::onWithTimeout)
        addItem("协程超时任务-超时返回默认Null", this::onWithTimeoutOrNull)
        addItem("协程异常-发生异常，其他兄弟协程也取消", this::onCancelByJob)
        addItem("协程异常-发生异常，其他兄弟协程不取消", this::onCancelBySupervisorJob)
        addItem("协程异常-异常处理器", this::onCoroutineExceptionHandler)
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

    private fun onAsyncTest() {
        mainScope.launch {
            // 只能作为子协程使用
            val deffer = async {
                Log.d(TAG, "async的方式启动协程")
                "async result"
            }
            // 需要手动调用，才能获取返回值
            Log.d(TAG, deffer.await())
        }
    }

    private fun onJoinTest() {
        mainScope.launch {
            launch {
                Log.d(TAG, "TasKA:" + System.currentTimeMillis())
                delay(2000)
            }.join()
            launch {
                Log.d(TAG, "TasKB:" + System.currentTimeMillis())
            }
            launch {
                Log.d(TAG, "TasKC:" + System.currentTimeMillis())
            }
        }
    }

    private fun onAsyncJoinTest()  {
        mainScope.launch {
            val defferA = async {
                Log.d(TAG, "TasKA:" + System.currentTimeMillis())
                delay(2000)
            }
            defferA.join()
            val defferB = async {
                Log.d(TAG, "TasKB:" + System.currentTimeMillis())
            }
            val defferC = async {
                Log.d(TAG, "TasKC:" + System.currentTimeMillis())
            }
        }
    }

    private fun onAsyncAwaitTest() {
        mainScope.launch {
            val defferA = async {
                Log.d(TAG, "TasKA:" + System.currentTimeMillis())
                delay(2000)
                "TaskA result"
            }
            Log.d(TAG, defferA.await() + ":" + System.currentTimeMillis())
            val defferB = async {
                Log.d(TAG, "TasKB:" + System.currentTimeMillis())
                delay(200)
                "TaskB result"
            }
            val defferC = async {
                Log.d(TAG, "TasKC:" + System.currentTimeMillis())
                delay(200)
                "TaskC result"
            }
            Log.d(TAG, defferB.await() + ":" + System.currentTimeMillis())
            Log.d(TAG, defferC.await() + ":" + System.currentTimeMillis())
        }
    }

    /**
     * 协程创建后，立即开始调度，如果协程被取消，则直接进入取消响应状态
     */
    private fun onStartDefault() {
        mainScope.launch {
            val job = launch(start = CoroutineStart.DEFAULT) {
                Log.d(TAG, "协程没有被取消")
                delay(50)
            }
            job.cancel()
            Log.d(TAG, "协程被取消")
        }
    }

    /**
     * 协程创建后，立即开始调度，如果cancel，则需要等到执行到第一个挂起点时才取消（遇到suspend函数）
     */
    private fun onStartAtomic() {
        mainScope.launch {
            val job = launch(start = CoroutineStart.ATOMIC) {
                Log.d(TAG, "协程没有被取消")
                delay(50)
            }
            job.cancel()
            Log.d(TAG, "协程被取消")
        }
    }

    /**
     * 协程创建后不会立即开始调度，只有调用start、join、await函数时才开始调度，在此之前如果被取消，就不会继续调度
     */
    private fun onStartLazy() {
        mainScope.launch {
            val job = launch(start = CoroutineStart.LAZY) {
                Log.d(TAG, "协程没有被取消")
                delay(50)
            }
            job.cancel()
            Log.d(TAG, "协程被取消")
        }
    }

    /**
     * 协程创建后立即在当前函数调用栈中执行，直到遇到第一个挂起点
     */
    private fun onStartUnDispatched() {
        mainScope.launch {
            // context是无效的
            val job = mainScope.launch(start = CoroutineStart.UNDISPATCHED, context = Dispatchers.IO) {
                Log.d(TAG, "协程没有被取消")
                delay(50)
            }
            job.cancel()
            Log.d(TAG, "协程被取消")
        }
    }

    private fun onCoroutineScope() {
        mainScope.launch(Dispatchers.IO) {
            coroutineScope {
                launch {
                    delay(400)
                    Log.d(TAG, "job1 end")
                }

                launch {
                    delay(200)
                    Log.d(TAG, "job2 end")
                    throw RuntimeException()
                }
            }
        }
    }

    private fun onSupervisorScope() {
        val handler = CoroutineExceptionHandler{_, exc ->
            Log.d(TAG, "Caught $exc")
        }
        mainScope.launch(Dispatchers.IO) {
            supervisorScope {
                launch(handler) {
                    delay(400)
                    Log.d(TAG, "job1 end")
                }

                launch(handler) {
                    delay(200)
                    Log.d(TAG, "job2 end")
                    throw RuntimeException()
                }
            }
        }
    }

    /**
     * 这段程序应该在打印2次时被取消，实际上没有取消成功
     */
    private fun onCancelDefaultJob() {
        defaultScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        Log.d(TAG, "job: i ${i++}")
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            Log.d(TAG, "waiting")
            job.cancelAndJoin()
            Log.d(TAG, "quited")
        }
    }

    private fun onIsActiveDefaultJob() {
        defaultScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5 && isActive) {
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        Log.d(TAG, "job: i ${i++}")
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            Log.d(TAG, "waiting")
            job.cancelAndJoin()
            Log.d(TAG, "quited")
        }
    }

    private fun onEnsureActiveDefaultJob() {
        defaultScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    ensureActive()
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        Log.d(TAG, "job: i ${i++}")
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            Log.d(TAG, "waiting")
            job.cancelAndJoin()
            Log.d(TAG, "quited")
        }
    }

    private fun onYieldDefaultJob() {
        defaultScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    yield() // 这里yield之后，有机会执行到cancel
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        Log.d(TAG, "job: i ${i++}")
                        nextPrintTime += 500
                    }
                }
            }
            delay(1300)
            Log.d(TAG, "waiting")
            job.cancelAndJoin()
            Log.d(TAG, "quited")
        }
    }

    private fun onTryFinallyJob() {
        ioScope.launch {
            try {
                val res = fetchData()
                Log.d(TAG, res)
            } finally {
                Log.d(TAG, "这里释放数据")
            }
        }
    }

    private fun onUseClosable() {
        val br = FileInputStream(File(""))
        br.use {
            br.read()
            // xxx
        }
    }

    private fun onNonCancelable() {
        val job = ioScope.launch {
            try {
                ioScope.launch {
                    Log.d(TAG, "delay 500")
                    delay(500)
                }
            } finally {
                withContext(NonCancellable) {
                    Log.d(TAG, "onNonCancelable finally start")
                    delay(500)
                    Log.d(TAG, "onNonCancelable finally end")
                }
            }
        }
        job.cancel()
    }

    private fun onWithTimeout() {
        defaultScope.launch {
            withTimeout(500) {
                Log.d(TAG, "start")
                delay(1000)
                Log.d(TAG, "end")
            }
        }
    }

    private fun onWithTimeoutOrNull() {
        defaultScope.launch {
            val res = withTimeoutOrNull(500) {
                Log.d(TAG, "start")
                delay(1000)
                Log.d(TAG, "end")
                "res"
            }
            Log.d(TAG, "$res")
        }
    }

    private fun onCancelByJob() {
        val handler = CoroutineExceptionHandler{_, exc ->
            Log.d(TAG, "Caught $exc")
        }
        val scope = CoroutineScope(Job() + Dispatchers.IO + handler)
        scope.launch {
            Log.d(TAG, "job1")
            delay(200)
            throw RuntimeException()
        }
        scope.launch {
            delay(500)
            Log.d(TAG, "job2")
        }
    }

    private fun onCancelBySupervisorJob() {
        // 如果不设置ExceptionHandler会把异常抛出去，程序会crash
        val handler = CoroutineExceptionHandler{_, exc ->
            Log.d(TAG, "Caught $exc")
        }
        val scope = CoroutineScope(SupervisorJob() + handler)
        scope.launch { // 这里也有加上SupervisorJob
            launch(SupervisorJob()) {
                Log.d(TAG, "job1")
                delay(200)
                throw IllegalArgumentException()
            }
            launch(SupervisorJob()) {
                delay(500)
                Log.d(TAG, "job2")
            }
        }
    }

    private fun onCoroutineExceptionHandler() {
        val handler = CoroutineExceptionHandler{_, exc ->
            Log.d(TAG, "Caught $exc")
        }
        ioScope.launch(SupervisorJob() + handler) {
            throw RuntimeException()
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
        defaultScope.cancel()
    }

    companion object {
        const val TAG = "CoroutineTestFragment"
    }

}