package com.hc.support.rxJava.observer;

public interface Observer<T> {
    void onNext(T t);

    void onComplete();

    void onError(Throwable r);

    void onSubscribe();
}
