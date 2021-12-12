package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observer.Action;
import com.hc.support.rxJava.observer.Consumer;
import com.hc.support.rxJava.plugin.Functions;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.observer.Subscriber;
import com.hc.support.rxJava.schedule.Scheduler;

public abstract class Observable<T> implements ObservableSource<T>{
    public interface OnSubscriber<T> {
        void call(Subscriber<T> subscriber);
    }

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }

    public abstract void subscribeActual(Observer<T> observer);

    public static <T> Observable<T> create(OnSubscriber<T> onSubscriber) {
        return new ObservableCreate<>(onSubscriber);
    }

    public static <T> Observable<T> just(T... t) {
        return new ObservableJust<>(t);
    }

    public <R> Observable<R> map(Function<T, R> function) {
        return new ObservableMap<>(this, function);
    }

    public Observable<T> doOnNext(Consumer<T> onNext) {
        return new ObservableDoOnEach<>(this, onNext, Functions.<Throwable>emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    public Observable<T> doOnError(Consumer<? super Throwable> onError) {
        return new ObservableDoOnEach<>(this, Functions.<T>emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    public Observable<T> doOnComplete(Action onComplete) {
        return new ObservableDoOnEach<>(this, Functions.<T>emptyConsumer(), Functions.<Throwable>emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
    }

    public Observable<T> doOnAfterTerminate(Action onAfterTerminate) {
        return new ObservableDoOnEach<>(this, Functions.<T>emptyConsumer(), Functions.<Throwable>emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate);
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<>(this, scheduler);
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }
}
