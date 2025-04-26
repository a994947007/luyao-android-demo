package com.hc.base.framework

import com.android.demo.rxandroid.observable.Observable

interface RxObservable<T> {

    fun notifyChanged(bean: T)

    fun observable(): Observable<T>
}