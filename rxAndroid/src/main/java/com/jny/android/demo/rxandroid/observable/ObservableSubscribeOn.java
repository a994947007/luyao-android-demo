package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.disposable.Disposable;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;
import com.jny.android.demo.rxandroid.schedule.Scheduler;

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
