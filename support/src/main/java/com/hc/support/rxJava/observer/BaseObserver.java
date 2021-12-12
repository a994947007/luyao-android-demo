package com.hc.support.rxJava.observer;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.disposable.EmptyDisposable;

public class BaseObserver<T, R> implements Observer<T>, Disposable {

    protected Observer<R> actual; // 下游

    public BaseObserver(Observer<R> actual) {
        this.actual = actual;
    }

    @Override
    public void onNext(T t) { }

    @Override
    public void onComplete() {
        actual.onComplete();
    }

    @Override
    public void onError(Throwable r) {
        actual.onError(r);
    }

    @Override
    public void onSubscribe() {
        actual.onSubscribe();
    }

    @Override
    public void dispose() {
        actual = EmptyDisposable.emptyObservable();
    }

    @Override
    public boolean isDisposable() {
        return actual == EmptyDisposable.emptyObservable();
    }
}
