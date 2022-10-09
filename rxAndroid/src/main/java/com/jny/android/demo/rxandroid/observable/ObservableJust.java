package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.disposable.EmptyDisposable;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

public class ObservableJust<T> extends Observable<T> {

    private final T [] value;

    public ObservableJust(T [] value) {
        this.value = value;
    }

    @Override
    public void subscribeActual(Observer<T> downstream) {
        JustObserver<T> upstream = new JustObserver<>(downstream);
        try {
            upstream.onSubscribe(new EmptyDisposable<>());
            if (value != null && value.length > 0) {
                for (T v : value) {
                    upstream.onNext(v);
                }
            }
            upstream.onComplete();
        } catch (Throwable r) {
            upstream.onError(r);
        }
    }

    private static class JustObserver<T> extends BaseObserver<T, T> {

        public JustObserver(Observer<T> actual) {
            super(actual);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }
    }
}
