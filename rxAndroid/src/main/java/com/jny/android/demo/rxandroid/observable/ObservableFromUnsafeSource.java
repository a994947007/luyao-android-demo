package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.observer.Observer;

public class ObservableFromUnsafeSource<T> extends Observable<T> {

    private final ObservableSource<T> source;

    public ObservableFromUnsafeSource(ObservableSource<T> source) {
        this.source = source;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(observer);
    }
}
