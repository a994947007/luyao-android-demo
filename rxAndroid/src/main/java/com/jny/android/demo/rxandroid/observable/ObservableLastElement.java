package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

public class ObservableLastElement<T> extends AbstractObservableWithUpStream<T, T> {

    public ObservableLastElement(Observable<T> source) {
        super(source);
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribeActual(new LastElementObserver<T>(observer));
    }

    /**
     * onNext时仅仅保存数据，onComplete时将保存的数据发射，此时一定是last
     * @param <T>
     */
    private static class LastElementObserver<T> extends BaseObserver<T, T> {

        private T item;

        public LastElementObserver(Observer<T> actual) {
            super(actual);
        }

        @Override
        public void onNext(T t) {
            item = t;
        }

        @Override
        public void onComplete() {
            try {
                onNext(item);
            } catch (Throwable e) {
                onError(e);
            }
            super.onComplete();
        }
    }
}
