package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.schedule.Scheduler;

import java.util.concurrent.TimeUnit;

public class ObservableTimer extends Observable<Long> {

    private final long time;
    private final TimeUnit unit;
    private final Scheduler scheduler;

    public ObservableTimer(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(final Observer<Long> observer) {
        final TimerObserver<Long> timerObserver = new TimerObserver<>(observer);
        timerObserver.onSubscribe(timerObserver);
        Disposable schedule = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    timerObserver.onNext(time);
                } catch (Throwable e) {
                    timerObserver.onError(e);
                }
                timerObserver.onComplete();
            }
        }, time, unit);
        timerObserver.setDisposable(schedule);
    }

    private static class TimerObserver<T> extends BaseObserver<T, T> {

        public TimerObserver(Observer<T> actual) {
            super(actual);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }
    }
}
