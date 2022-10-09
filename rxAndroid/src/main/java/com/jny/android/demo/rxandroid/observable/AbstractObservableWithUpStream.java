package com.jny.android.demo.rxandroid.observable;

/**
 * @param <T> 上游
 * @param <R> 下游
 */
public abstract class AbstractObservableWithUpStream<T, R> extends Observable<R>{
    protected Observable<T> source;
    public AbstractObservableWithUpStream(Observable<T> source) {
        this.source = source;
    }
}
