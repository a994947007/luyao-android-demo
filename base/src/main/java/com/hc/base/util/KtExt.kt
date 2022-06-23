package com.hc.base.util

import com.hc.support.mvps.Presenter
import com.hc.support.rxJava.disposable.Disposable
import com.hc.support.rxJava.observable.Observable

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