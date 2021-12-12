package com.hc.support.rxJava.observer;

public interface Subscriber<T> {
    void onNext(T t);

    void onError(Throwable r);

    void onComplete();
}
