package com.hc.base.framework

import com.android.demo.rxandroid.observable.Observable
import com.android.demo.rxandroid.schedule.Schedules
import com.android.demo.rxandroid.subjects.PublishSubject

class DefaultRxObservable<T>: RxObservable<T> {
    private val subject = PublishSubject.create<T>()

    override fun observable(): Observable<T> {
        return subject.observeOn(Schedules.MAIN)
    }

    override fun notifyChanged(bean: T) {
        subject.onNext(bean)
    }
}