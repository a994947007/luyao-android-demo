package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;

public class ObservableFlatMap<T, R> extends AbstractObservableWithUpStream<T, R> {

    private final Function<T, ObservableSource<R>> function;

    public ObservableFlatMap(Observable<T> source, Function<T, ObservableSource<R>> function) {
        super(source);
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<R> observer) {
        source.subscribe(new FlatMapObserver<>(observer, function));
    }

    private static class FlatMapObserver<T, R> extends BaseObserver<T, R> {

        private final Function<T, ObservableSource<R>> function;
        private boolean done = false;

        public FlatMapObserver(Observer<R> actual, Function<T, ObservableSource<R>> function) {
            super(actual);
            this.function = function;
        }

        @Override
        public void onNext(T t) {
            ObservableSource<R> p = function.apply(t);
            p.subscribe(actual);    // 让actual成为p的下游
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            actual.onComplete();
            done = true;
        }

        private class InnerObserver extends BaseObserver<R, R> {

            public InnerObserver(Observer<R> actual) {
                super(actual);
            }

            @Override
            public void onNext(R r) {
                super.onNext(r);
            }

            @Override
            public void onError(Throwable r) {
                super.onError(r);
                done = true;
            }

            /**
             * FlatMap的Observable不需要再次触发onSubscribe
             */
            @Override
            public void onSubscribe() { }

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
                actual.onComplete();
                done = true;
            }
        }
    }
}
