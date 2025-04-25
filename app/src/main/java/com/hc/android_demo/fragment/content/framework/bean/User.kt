package com.hc.android_demo.fragment.content.framework.bean

import com.android.demo.rxandroid.observable.Observable
import com.android.demo.rxandroid.schedule.Schedules
import com.android.demo.rxandroid.subjects.PublishSubject

data class User(
    var username: String,
    var name: String,
    var isLogin: Boolean = false
) {

}

interface RxObservable<T> {

    fun notifyChanged(t: T)

    fun observable(): Observable<T>
}

class DefaultRxObservable: RxObservable<User> {

    private val subject = PublishSubject.create<User>()

    override fun observable(): Observable<User> {
        return subject.observeOn(Schedules.MAIN)
    }

    override fun notifyChanged(t: User) {
        subject.onNext(t)
    }

}

object UserObserver {
    val rxObservable = DefaultRxObservable()
}

fun User.observable(): Observable<User> {
    return UserObserver.rxObservable.observable()
}

fun User.notifyChanged() {
    UserObserver.rxObservable.notifyChanged(this)
}