package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.EmptyDisposable;
import com.hc.support.rxJava.observer.Observer;

import java.util.concurrent.Callable;

public class ObservableFromCallable<T> extends Observable<T> {

    private final Callable<T> callable;

    public ObservableFromCallable(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        observer.onSubscribe(new EmptyDisposable<>());
        try {
            T value = callable.call();
            observer.onNext(value);
        } catch (Throwable e) {
            observer.onError(e);
        }
        observer.onComplete();
    }
}
