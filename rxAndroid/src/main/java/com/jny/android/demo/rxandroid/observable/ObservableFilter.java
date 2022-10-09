package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.filter.Filter;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

public class ObservableFilter<T> extends AbstractObservableWithUpStream<T, T> {

    private final Filter<T> filter;

    public ObservableFilter(Observable<T> source, Filter<T> filter) {
        super(source);
        this.filter = filter;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new FilterObserver<T>(observer, filter));
    }

    private static class FilterObserver<T> extends BaseObserver<T, T> {

        private final Filter<T> filter;

        public FilterObserver(Observer<T> actual, Filter<T> filter) {
            super(actual);
            this.filter = filter;
        }

        @Override
        public void onNext(T t) {
            try {
                if (filter.accept(t)) {
                    actual.onNext(t);
                }
            } catch (Throwable r) {
                actual.onError(r);
                dispose();
            }
        }
    }
}
