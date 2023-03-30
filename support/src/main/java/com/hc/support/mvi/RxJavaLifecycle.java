package com.hc.support.mvi;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.subjects.BehaviorSubject;

public class RxJavaLifecycle implements LifecycleObserver {

    private final BehaviorSubject<Lifecycle.Event> subject;

    public RxJavaLifecycle(BehaviorSubject<Lifecycle.Event> subject) {
        this.subject = subject;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        subject.onNext(Lifecycle.Event.ON_DESTROY);
    }
}
