package com.jny.android.demo.rxandroid.observable;

import com.jny.android.demo.rxandroid.disposable.Disposable;
import com.jny.android.demo.rxandroid.function.Function;
import com.jny.android.demo.rxandroid.observer.BaseObserver;
import com.jny.android.demo.rxandroid.observer.Observer;

import java.io.IOException;
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
                    ObservableSource<U> apply = null;
                    try {
                        apply = mapper.apply(t);
                        apply.subscribe(new InnerObserver(actual));
                    } catch (IOException e) {
                        onError(e);
                    }
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
