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

object EmptyRxObservable: RxObservable<Any> {
    override fun notifyChanged(bean: Any) {}

    override fun observable(): Observable<Any> {
        return Observable.create {  }
    }

}