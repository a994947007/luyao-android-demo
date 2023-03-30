package com.hc.support.mvi;


import androidx.lifecycle.Lifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

public class LifecycleTransformer<T> implements ObservableTransformer<T, T> {

    private final Observable<Lifecycle.Event> lifecycle;

    public LifecycleTransformer(Observable<Lifecycle.Event> lifecycle) {
        this.lifecycle = lifecycle;
    }
    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .takeUntil(lifecycle.takeUntil(new Predicate<Lifecycle.Event>() {
                    @Override
                    public boolean test(Lifecycle.Event event) throws Exception {
                        return event == Lifecycle.Event.ON_DESTROY;
                    }
                }));
    }
}
