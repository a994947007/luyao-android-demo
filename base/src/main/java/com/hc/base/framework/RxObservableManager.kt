package com.hc.base.framework

object RxObservableManager {
    // 这会使得user对象越多，这里的RxObservable也会增加，而且没有释放
    private val observableGroupMap = mutableMapOf<Class<*>, MutableMap<String, RxObservable<out Syncable>>>()

    fun addRxObservable(syncable: Syncable, rxObservable: RxObservable<out Syncable>) {
        var group = observableGroupMap[syncable::class.java]
        if (group == null) {
            group = mutableMapOf()
            observableGroupMap[syncable::class.java] = group
        }
        val key = syncable.key()
        group[key] = rxObservable
    }

    fun removeRxObservable(syncable: Syncable) {
        val group = observableGroupMap[syncable::class.java]
        val key = syncable.key()
        group?.remove(key)
    }

    fun get(syncable: Syncable): RxObservable<Syncable>? {
        val group = observableGroupMap[syncable::class.java]
        val key = syncable.key()
        if (group != null) {
            return group[key] as? RxObservable<Syncable>?
        }
        return EmptyRxObservable
    }
}