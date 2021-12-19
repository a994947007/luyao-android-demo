package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.disposable.EmptyDisposable;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.observer.Subscriber;

public class ObservableCreate<T> extends Observable<T> {

    private final OnSubscriber<T> source;

    public ObservableCreate(OnSubscriber<T> onSubscriber) {
        this.source = onSubscriber;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        observer.onSubscribe(new EmptyDisposable<>());
        source.call(new CreateSubscriber<>(observer));
    }

    private static class CreateSubscriber<T> implements Subscriber<T>, Disposable {

        private Observer<T> actual;

        private CreateSubscriber(Observer<T> observer) {
            this.actual = observer;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable r) {
            actual.onError(r);
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }

        @Override
        public void dispose() {
            actual = EmptyDisposable.emptyObservable();
        }

        @Override
        public boolean isDisposable() {
            return actual == EmptyDisposable.emptyObservable();
        }
    }
}
