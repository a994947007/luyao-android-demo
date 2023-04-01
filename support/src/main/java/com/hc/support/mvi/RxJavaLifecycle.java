package com.hc.support.mvi;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.demo.rxandroid.disposable.Disposable;
import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.observable.ObservableSource;
import com.android.demo.rxandroid.observable.ObservableTransformer;
import com.android.demo.rxandroid.observer.Consumer;

public class RxJavaLifecycle<T> implements LifecycleObserver, ObservableTransformer<T, T> {

    private Disposable mDisposable;
    private LifecycleOwner owner;

    public RxJavaLifecycle(LifecycleOwner owner) {
        this.owner = owner;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposable()) {
            mDisposable.dispose();
        }
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upStream) {
        return upStream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) {
                mDisposable = disposable;
            }
        });
    }

    public static <T> RxJavaLifecycle<T> bindUntilDestroy(LifecycleOwner owner) {
        RxJavaLifecycle<T> rxJavaLifecycle = new RxJavaLifecycle<>(owner);
        owner.getLifecycle().addObserver(rxJavaLifecycle);
        return rxJavaLifecycle;
    }
}
