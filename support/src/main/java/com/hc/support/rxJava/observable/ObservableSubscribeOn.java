package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.schedule.Scheduler;

public class ObservableSubscribeOn<T> extends AbstractObservableWithUpStream<T, T> {

    private final Scheduler scheduler;

    public ObservableSubscribeOn(Observable<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(final Observer<T> observer) {
        final SubscribeOnObserver<T> tSubscribeOnObserver = new SubscribeOnObserver<>(observer);
        Disposable disposable = scheduler.scheduleDirect(new Runnable() {
            @Override
            public void run() {
                source.subscribe(tSubscribeOnObserver);
            }
        });
        tSubscribeOnObserver.setDisposable(disposable);
    }

    private static class SubscribeOnObserver<T> extends BaseObserver<T, T> {

        private Disposable d;

        public SubscribeOnObserver(Observer<T> actual) {
            super(actual);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        public void setDisposable(Disposable d) {
            this.d = d;
        }

        @Override
        public void dispose() {
            super.dispose();
            d.dispose();
        }
    }
}
