package com.hc.support.rxJava.observable;

import android.util.Log;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.schedule.Scheduler;

public class ObservableObserveOn<T> extends AbstractObservableWithUpStream<T, T>{

    private final Scheduler scheduler;

    public ObservableObserveOn(Observable<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new ObserveOnObserver<T>(observer, scheduler));
    }

    private static class ObserveOnObserver<T> extends BaseObserver<T, T> {

        private final Scheduler scheduler;

        private Disposable d;

        public ObserveOnObserver(Observer<T> actual, Scheduler scheduler) {
            super(actual);
            this.scheduler = scheduler;
        }

        @Override
        public void onNext(final T t) {
            d = scheduler.scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    actual.onNext(t);
                }
            });
        }

        @Override
        public void dispose() {
            super.dispose();
            d.dispose();
        }
    }
}
