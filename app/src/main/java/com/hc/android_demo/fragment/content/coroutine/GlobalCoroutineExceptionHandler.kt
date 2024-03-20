package com.hc.android_demo.fragment.content.coroutine

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class GlobalCoroutineExceptionHandler: CoroutineExceptionHandler {

    companion object {
        const val TAG = "ExceptionHandler"
    }

    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e(TAG, "CoroutineException $exception")
    }
}