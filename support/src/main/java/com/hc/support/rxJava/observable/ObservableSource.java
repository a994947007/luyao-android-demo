package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.observer.Observer;

public interface ObservableSource<T> {
    void subscribe(Observer<T> observer);
}
