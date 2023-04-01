package com.hc.support.mvi;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.android.demo.rxandroid.function.Function;
import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.observer.Consumer;
import com.android.demo.rxandroid.subjects.BehaviorSubject;

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
        observable = subject.compose(RxJavaLifecycle.<T>bindUntilDestroy(owner));
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
            public void accept(T t) {
                observer.accept(t);
            }
        });
    }
}
