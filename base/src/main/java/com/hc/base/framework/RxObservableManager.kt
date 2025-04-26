package com.hc.base.framework

object RxObservableManager {
    private val observableMap = mutableMapOf<String, RxObservable<out Syncable>>()

    fun addRxObservable(syncable: Syncable, rxObservable: RxObservable<out Syncable>) {
        val key = syncable.key()
        observableMap[key] = rxObservable
    }

    fun removeRxObservable(syncable: Syncable) {
        val key = syncable.key()
        observableMap.remove(key)
    }

    fun get(syncable: Syncable): RxObservable<Syncable>? {
        val key = syncable.key()
        return observableMap[key] as? RxObservable<Syncable>?
    }
}