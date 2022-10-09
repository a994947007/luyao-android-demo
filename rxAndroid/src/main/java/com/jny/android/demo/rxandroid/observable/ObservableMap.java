package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.function.Function;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

import java.io.IOException;

public class ObservableMap<T, R> extends AbstractObservableWithUpStream<T, R>{

    private final Function<T, R> function;

    public ObservableMap(Observable<T> source, Function<T, R> function) {
        super(source);
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<R> observer) {
        source.subscribeActual(new MapObserver<>(observer, function));
    }

    private static class MapObserver<T, R> extends BaseObserver<T, R> {

        private final Function<T, R> function;

        public MapObserver(Observer<R> actual, Function<T, R> function) {
            super(actual);
            this.function = function;
        }

        @Override
        public void onNext(T t) {
            R r = null;
            try {
                r = function.apply(t);
                actual.onNext(r);
            } catch (IOException e) {
                onError(e);
            }
        }
    }
}
