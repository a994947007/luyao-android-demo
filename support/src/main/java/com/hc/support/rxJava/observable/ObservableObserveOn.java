package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.schedule.Scheduler;

import java.util.LinkedList;
import java.util.Queue;

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

    /**
     * 需要维持一个队列，否则onComplete会跑到onNext之前
     */
    private static class ObserveOnObserver<T> extends BaseObserver<T, T> implements Runnable {

        private final Scheduler scheduler;

        private final Queue<Runnable> queue = new LinkedList<>();

        public ObserveOnObserver(Observer<T> actual, Scheduler scheduler) {
            super(actual);
            this.scheduler = scheduler;
        }

        @Override
        public void onNext(final T t) {
            queue.offer(new Runnable() {
                @Override
                public void run() {
                    actual.onNext(t);
                }
            });
            scheduler.scheduleDirect(this);
        }

        @Override
        public void dispose() {
            super.dispose();
            scheduler.scheduleDirect(this);
        }

        @Override
        public void onComplete() {
            queue.offer(new Runnable() {
                @Override
                public void run() {
                    actual.onComplete();
                }
            });
            scheduler.scheduleDirect(this);
        }

        @Override
        public void run() {
            while (!queue.isEmpty()) {
                synchronized (queue) {
                    Runnable poll = queue.poll();
                    poll.run();
                }
            }
        }
    }
}
