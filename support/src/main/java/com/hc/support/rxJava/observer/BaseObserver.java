package com.hc.support.rxJava.observer;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.disposable.EmptyDisposable;

public class BaseObserver<T, R> implements Observer<T>, Disposable {

    protected Observer<R> actual; // 下游
    protected Disposable d;

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
    public void onSubscribe(Disposable d) {
        this.d = d;
        actual.onSubscribe(this);
    }

    @Override
    public void dispose() {
        actual = EmptyDisposable.emptyObservable();
        this.d.dispose();
        this.d = null;
    }

    @Override
    public boolean isDisposable() {
        return actual == EmptyDisposable.emptyObservable();
    }

    public void setDisposable(Disposable d) {
        this.d = d;
    }
}
