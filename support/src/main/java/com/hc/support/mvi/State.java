package com.hc.support.mvi;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import androidx.lifecycle.Observer;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class State<T> {

    Observable<T> observable;
    BehaviorSubject<T> subject = BehaviorSubject.create();

    public State() { }

    private State(Observable<T> observable) {
        this.observable = observable;
    }

    public static <T> State<T> create(@NonNull LifecycleOwner owner) {
        State<T> state = new State<>();
        return state.onLifeCycle(owner);
    }

    public State<T> onLifeCycle(@NonNull LifecycleOwner owner) {
        BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();
        owner.getLifecycle().addObserver(new RxJavaLifecycle(lifecycleSubject));
        observable = subject.compose(new LifecycleTransformer<T>(lifecycleSubject.hide()));
        return this;
    }

    public void set(T t) {
        subject.onNext(t);
    }

    public <R> State<R> get(@NonNull Function<T, R> function) {
        Observable<R> rObservable = observable.map(function)
                .distinctUntilChanged();
        return new State<>(rObservable);
    }

    public void observe(@NonNull final Consumer<? super T> observer) {
        observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                observer.accept(t);
            }
        });
    }
}
