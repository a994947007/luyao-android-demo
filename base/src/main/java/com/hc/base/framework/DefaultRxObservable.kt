package com.hc.base.framework

import com.android.demo.rxandroid.observable.Observable
import com.android.demo.rxandroid.schedule.Schedules
import com.android.demo.rxandroid.subjects.PublishSubject

class DefaultRxObservable<T>: RxObservable<T> {
    private val subject = PublishSubject.create<T>()

    override fun observable(): Observable<T> {
        return subject.hide()
    }

    override fun notifyChanged(bean: T) {
        subject.onNext(bean)
    }
}

object EmptyRxObservable: RxObservable<Syncable> {
    override fun notifyChanged(bean: Syncable) {}

    override fun observable(): Observable<Syncable> {
        return Observable.create {  }
    }

}