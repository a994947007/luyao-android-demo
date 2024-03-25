package com.hc.support.mvi2

import android.util.Log
import android.view.View
import kotlinx.coroutines.*

abstract class BaseView<T: UIState, VM : BaseViewModelV2<T>> : IView<T, VM> {

    val scopeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("BaseView", "$throwable.message")
    }
    val viewScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + scopeErrorHandler)

    override fun onCreateView(rootView: View) {
        rootView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            override fun onViewDetachedFromWindow(v: View?) {
                viewScope.cancel()
            }
        })
        onViewCreated(rootView)
    }

    abstract fun onViewCreated(rootView: View)

}