package com.hc.support.rxJava.observer;

import com.hc.support.rxJava.disposable.Disposable;

public interface Observer<T> {
    void onNext(T t);

    void onComplete();

    void onError(Throwable r);

    /**
     * 上游传递过来的 disposable
     */
    void onSubscribe(Disposable d);
}
