package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.exception.Exceptions;
import com.jny.android.demo.rxandroid.observer.Action;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Consumer;
import com.jny.android.demo.rxandroid.observer.Observer;
import com.jny.android.demo.rxandroid.plugin.RxJavaPlugins;

public class ObservableDoOnEach<T> extends AbstractObservableWithUpStream<T, T> {

    private final Consumer<T> onNext;
    private final Consumer<? super Throwable> onError;
    private final Action onComplete;
    private final Action onAfterTerminate;

    public ObservableDoOnEach(Observable<T> source, Consumer<T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
        super(source);
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onAfterTerminate = onAfterTerminate;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new DoOnEachObserver<T>(observer, onNext, onError, onComplete, onAfterTerminate));
    }

    private static final class DoOnEachObserver<T> extends BaseObserver<T, T> {

        private final Consumer<T> onNext;
        private final Consumer<? super Throwable> onError;
        private final Action onComplete;
        private final Action onAfterTerminate;

        public DoOnEachObserver(Observer<T> actual, Consumer<T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
            super(actual);
            this.onNext = onNext;
            this.onError = onError;
            this.onComplete = onComplete;
            this.onAfterTerminate = onAfterTerminate;
        }

        @Override
        public void onNext(T t) {
            try {
                onNext.accept(t);
            } catch (Throwable r) {
                onError(r);
                dispose();
            }
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable r) {
            try {
                onError.accept(r);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            actual.onError(r);
        }

        @Override
        public void onComplete() {
            try {
                onComplete.run();
            } catch (Throwable e) {
                onError(e);
            }
            actual.onComplete();
            try {
                onAfterTerminate.run(); // onAfterTerminate不走onError
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }
}
