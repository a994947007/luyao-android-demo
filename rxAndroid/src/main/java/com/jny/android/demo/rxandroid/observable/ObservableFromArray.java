package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.disposable.EmptyDisposable;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

public class ObservableFromArray<T> extends Observable<T> {

    private T [] values;

    public ObservableFromArray(T [] values) {
        this.values = values;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        FromArrayObserver<Object> objectFromArrayObserver = new FromArrayObserver<>(new EmptyDisposable<>());
        observer.onSubscribe(objectFromArrayObserver);
        try {
            for (T value : values) {
                if (objectFromArrayObserver.isDisposable()) {
                    break;
                }
                observer.onNext(value);
            }
        } catch (Throwable e) {
            observer.onError(e);
        }
        observer.onComplete();
    }

    private static class FromArrayObserver<T> extends BaseObserver<T, T> {

        public FromArrayObserver(Observer<T> actual) {
            super(actual);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }
    }
}
