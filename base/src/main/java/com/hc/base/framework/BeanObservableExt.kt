package com.hc.base.framework

import com.android.demo.rxandroid.observable.Observable

fun <T: Syncable> T.observable(): Observable<T> {
    val rxObservableCache = RxObservableManager.get(this)
    if (rxObservableCache != null) {
        return rxObservableCache.observable() as Observable<T>
    }
    val rxObservable = DefaultRxObservable<T>()
    RxObservableManager.addRxObservable(this, rxObservable)
    return rxObservable.observable().apply {
        doOnDispose {
            RxObservableManager.removeRxObservable(this@observable)
        }
    }
}

fun <T: Syncable> T.notifyChanged() {
    val rxObservable = RxObservableManager.get(this)
    rxObservable?.notifyChanged(this)
}