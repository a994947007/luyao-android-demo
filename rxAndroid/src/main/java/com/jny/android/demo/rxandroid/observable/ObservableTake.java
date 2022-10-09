package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

/**
 * take 操作符在第count个直接onComplete后面的observer，但不会执行前面的onComplete，比如在take操作符之前
 * 有一个doOnComplete操作符，会执行不到。执行完complete之后会执行dispose。
 */
public class ObservableTake<T> extends AbstractObservableWithUpStream<T, T> {

    private final int count;

    public ObservableTake(Observable<T> source, int count) {
        super(source);
        this.count = count;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribeActual(new TakeObserver<T>(observer, count));
    }

    private static class TakeObserver<T> extends BaseObserver<T, T> {

        private int remaining;

        public TakeObserver(Observer<T> actual, int remaining) {
            super(actual);
            this.remaining = remaining;
        }

        @Override
        public void onNext(T t) {
            if (remaining == 0) {
                onComplete();
                dispose();
                return;
            }
            remaining --;
            actual.onNext(t);
        }
    }
}
