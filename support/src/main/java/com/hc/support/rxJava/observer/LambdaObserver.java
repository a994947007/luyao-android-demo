package com.hc.support.rxJava.observer;

import com.hc.support.rxJava.disposable.Disposable;

public class LambdaObserver<T> implements Observer<T>, Disposable {

    private final Consumer<T> consumer;
    private final Consumer<Throwable> errConsumer;
    private final Action completeAction;
    private final Action onSubscribeAction;

    private boolean isDisposable = false;
    private Disposable superDisposable;

    public LambdaObserver(Consumer<T> consumer, Consumer<Throwable> errConsumer, Action completeAction, Action onSubscribeAction) {
        this.consumer = consumer;
        this.errConsumer = errConsumer;
        this.completeAction = completeAction;
        this.onSubscribeAction = onSubscribeAction;
    }

    @Override
    public void onNext(T t) {
        consumer.accept(t);
    }

    @Override
    public void onComplete() {
        completeAction.run();
    }

    @Override
    public void onError(Throwable r) {
        errConsumer.accept(r);
    }

    @Override
    public void onSubscribe(Disposable d) {
        onSubscribeAction.run();
        superDisposable = d;
    }

    @Override
    public void dispose() {
        if (!isDisposable) {
            superDisposable.dispose();
            isDisposable = true;
        }
    }

    @Override
    public boolean isDisposable() {
        return isDisposable;
    }
}
