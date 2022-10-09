package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

/**
 * firstElement经常配合concat一起使用，用于网络请求，缓存请求等情况，先中了缓存就不需要网络请求了。
 * 通过在上游Observer中直接complete，不走onNext中的计数逻辑。
 */
public class ObservableElementAt<T> extends AbstractObservableWithUpStream<T, T> {

    private final int index;

    public ObservableElementAt(Observable<T> source, int index) {
        super(source);
        this.index = index;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new ElementAtObserver<T>(observer, index));
    }

    private static class ElementAtObserver<T> extends BaseObserver<T, T> {

        private int count = 0;
        private final int index;
        private boolean done = false;

        public ElementAtObserver(Observer<T> actual, int index) {
            super(actual);
            this.index = index;
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            if (count == index) {
                done = true;
                actual.onNext(t);
                dispose();
                return;
            }
            count ++;
        }
    }
}
