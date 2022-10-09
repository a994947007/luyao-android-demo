package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.disposable.Disposable;
import com.jny.android.demo.rxandroid.observer.Observer;

public interface ObservableSource<T> {
    void subscribe(Observer<T> observer);
}
