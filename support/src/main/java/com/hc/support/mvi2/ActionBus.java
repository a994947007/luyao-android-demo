package com.hc.support.mvi2;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.android.demo.rxandroid.filter.Predicate;
import com.android.demo.rxandroid.observer.Consumer;
import com.android.demo.rxandroid.subjects.BehaviorSubject;
import com.hc.support.mvi.RxJavaLifecycle;

public class ActionBus {
    public BehaviorSubject<Action> subject = BehaviorSubject.create();

    private final LifecycleOwner owner;

    public ActionBus(LifecycleOwner owner) {
        this.owner = owner;
    }

    public void observe(final Class<? extends Action> actionClass, final Observer<Action> observer) {
        subject.compose(RxJavaLifecycle.<Action>bindUntilDestroy(owner))
                .filter(new Predicate<Action>() {
                    @Override
                    public boolean test(Action action) {
                        return action.getClass() == actionClass;
                    }
                })
                .subscribe(new Consumer<Action>() {
                    @Override
                    public void accept(Action action) {
                        observer.onChanged(action);
                    }
                });
    }

    public void dispatch(Action action) {
        subject.onNext(action);
    }
}
