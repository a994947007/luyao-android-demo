package com.hc.base.util

import com.android.demo.rxandroid.disposable.Disposable
import com.hc.support.mvps.Presenter
import com.android.demo.rxandroid.observable.Observable

open class KtPresenter: Presenter() {
    fun Disposable.autoDispose() {
        addToAutoDispose(this)
    }
}

public inline fun <T> Observable<T>.subscribeBy(
    crossinline onNext: (T) -> Unit = {},
    crossinline onError: (Throwable) -> Unit = {}
): Disposable = subscribe(
    { onNext(it) } ,
    { onError(it) }
)