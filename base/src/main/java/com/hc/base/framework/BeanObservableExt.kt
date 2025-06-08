package com.hc.base.framework

import com.android.demo.rxandroid.observable.Observable

fun <T: Any> T.observable(): Observable<T> {
    val syncable = SyncableManager.create(this)
    val rxObservableCache = RxObservableManager.get(syncable)
    if (rxObservableCache != null && rxObservableCache != EmptyRxObservable) {
        return rxObservableCache.observable()
    }
    val rxObservable = DefaultRxObservable<T>()
    RxObservableManager.addRxObservable(syncable, rxObservable)
    return rxObservable.observable().doOnDispose {
        RxObservableManager.removeRxObservable(syncable)
    }
}

fun <T: Any> T.notifyChanged() {
    val syncable = SyncableManager.create(this)
    val rxObservable = RxObservableManager.get(syncable)
    rxObservable?.notifyChanged(this)
}