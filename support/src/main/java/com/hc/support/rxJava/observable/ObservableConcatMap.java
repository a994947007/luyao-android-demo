package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observer.BaseObserver;
import com.hc.support.rxJava.observer.Observer;

import java.util.LinkedList;
import java.util.Queue;

public class ObservableConcatMap<T, U> extends AbstractObservableWithUpStream<T, U> {

    private final Function<T, ObservableSource<U>> mapper;

    public ObservableConcatMap(Observable<T> source, Function<T, ObservableSource<U>> mapper) {
        super(source);
        this.mapper = mapper;
    }

    @Override
    public void subscribeActual(Observer<U> observer) {
        source.subscribeActual(new ConcatMapObserver<>(observer, mapper));
    }

    private static class ConcatMapObserver<T, U> extends BaseObserver<T, U> {

        private final Function<T, ObservableSource<U>> mapper;
        private boolean done = false;
        private final Queue<Runnable> queue = new LinkedList<>();

        private boolean hasSubscribe = false;
        private Queue<Disposable> disposableQueue = new LinkedList<>();

        public ConcatMapObserver(Observer<U> actual, Function<T, ObservableSource<U>> mapper) {
            super(actual);
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
            disposableQueue.offer(this);
        }

        @Override
        public void onNext(final T t) {
            queue.add(new Runnable() {
                @Override
                public void run() {
                    ObservableSource<U> apply = mapper.apply(t);
                    apply.subscribe(new InnerObserver(actual));
                }
            });
            drain();
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            queue.add(new Runnable() {
                @Override
                public void run() {
                    actual.onComplete();
                    done = true;
                }
            });
            drain();
        }

        private class InnerObserver extends BaseObserver<U, U> {


            public InnerObserver(Observer<U> actual) {
                super(actual);
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposableQueue.add(d);
            }

            @Override
            public void onNext(U u) {
                InnerObserver.this.actual.onNext(u);
            }

            @Override
            public void onError(Throwable r) {
                if (done) {
                    return;
                }
                actual.onError(r);
                done = true;
                drain();
            }

            @Override
            public void onComplete() { }

            @Override
            public void dispose() {
                super.dispose();
                while (!disposableQueue.isEmpty()) {
                    disposableQueue.poll().dispose();
                }
            }
        }

        private void drain() {
            while (!queue.isEmpty()) {
                synchronized (queue) {
                    Runnable poll = queue.poll();
                    poll.run();
                }
            }
        }
    }

}
