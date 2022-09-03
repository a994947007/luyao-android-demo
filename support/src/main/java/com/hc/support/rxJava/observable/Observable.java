package com.hc.support.rxJava.observable;

import com.hc.support.rxJava.disposable.Disposable;
import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observer.Action;
import com.hc.support.rxJava.observer.Consumer;
import com.hc.support.rxJava.observer.LambdaObserver;
import com.hc.support.rxJava.plugin.Functions;
import com.hc.support.rxJava.observer.Observer;
import com.hc.support.rxJava.observer.Subscriber;
import com.hc.support.rxJava.schedule.Scheduler;
import com.hc.support.rxJava.schedule.Schedules;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

    @SafeVarargs
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

    public Observable<T> distinctUntilChanged() {
        return new ObservableDistinctUntilChanged<>(this);
    }

    public static <T> Observable<T> fromCallable(Callable<T> callable) {
        return new ObservableFromCallable<>(callable);
    }

    @SafeVarargs
    public static <T> Observable<T> fromArray(T... array) {
        return new ObservableFromArray<>(array);
    }

    public Observable<T> elementAt(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index >= 0 required but it was " + index);
        }
        return new ObservableElementAt<>(this, index);
    }

    public Observable<T> firstElement() {
        return elementAt(0);
    }

    public Observable<T> lastElement() {
        return new ObservableLastElement<>(this);
    }

    public Observable<T> take(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count >= 0 required but it was " + count);
        }
        return new ObservableTake<>(this, count);
    }

    public <U> Observable<U> concatMap(Function<T, ObservableSource<U>> mapper) {
        return new ObservableConcatMap<>(this, mapper);
    }

    public <U> Observable<U> flatMap(Function<T, ObservableSource<U>> mapper) {
        return new ObservableFlatMap<>(this ,mapper);
    }

    public static Observable<Long> timer(long time, TimeUnit unit) {
        return new ObservableTimer(time, unit, Schedules.COMPUTATION);
    }

    @SafeVarargs
    public static <T> Observable<T> concat(Observable<T> ... observables) {
        return new ObservableFromArray<>(observables).concatMap(new Function<Observable<T>, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable;
            }
        });
    }

    public static <T> Observable<T> wrap(ObservableSource<T> source) {
        if (source instanceof Observable) {
            return (Observable<T>)source;
        }
        return new ObservableFromUnsafeSource<>(source);
    }

    public final <R> Observable<R> compose(ObservableTransformer<T, R> upstream) {
        return wrap(upstream.apply(this));
    }

    @SafeVarargs
    public static <T> Observable<T> merge(Observable<T> ... observables) {
        return new ObservableFromArray<>(observables).flatMap(new Function<Observable<T>, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable;
            }
        });
    }

    public Disposable subscribe(Consumer<T> consumer) {
        LambdaObserver<T> ls = new LambdaObserver<>(consumer, Functions.<Throwable>emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        this.subscribe(ls);
        return ls;
    }

    public Disposable subscribe(Consumer<T> consumer, Consumer<Throwable> errConsumer) {
        LambdaObserver<T> ls = new LambdaObserver<>(consumer, errConsumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        this.subscribe(ls);
        return ls;
    }

    public Disposable subscribe(Consumer<T> consumer, Consumer<Throwable> errConsumer, Action completeAction) {
        LambdaObserver<T> ls = new LambdaObserver<>(consumer, errConsumer, completeAction, Functions.EMPTY_ACTION);
        this.subscribe(ls);
        return ls;
    }

    public Disposable subscribe(Consumer<T> consumer, Consumer<Throwable> errConsumer, Action completeAction, Action onSubscribeAction) {
        LambdaObserver<T> ls = new LambdaObserver<>(consumer, errConsumer, completeAction, onSubscribeAction);
        this.subscribe(ls);
        return ls;
    }

    public Disposable subscribe() {
        return subscribe(Functions.<T>emptyConsumer());
    }
}
