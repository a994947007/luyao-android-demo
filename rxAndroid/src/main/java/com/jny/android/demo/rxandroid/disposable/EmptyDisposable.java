package com.jny.android.demo.rxandroid.disposable;

import com.jny.android.demo.rxandroid.observer.Observer;

public class EmptyDisposable<T> implements Observer<T>, Disposable{

    private volatile static EmptyDisposable<Object> DISPOSABLE = null;

    public static <T> Observer<T> emptyObservable() {
        if (DISPOSABLE == null) {
            synchronized (EmptyDisposable.class) {
                if (DISPOSABLE == null) {
                    DISPOSABLE = new EmptyDisposable<>();
                }
            }
        }
        return (Observer<T>) DISPOSABLE;
    }

    @Override
    public void dispose() { }

    @Override
    public boolean isDisposable() {
        return true;
    }

    @Override
    public void onNext(T t) { }

    @Override
    public void onComplete() { }

    @Override
    public void onError(Throwable r) { }

    @Override
    public void onSubscribe(Disposable d) { }
}
