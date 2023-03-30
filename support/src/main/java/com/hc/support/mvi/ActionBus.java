package com.hc.support.mvi;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import androidx.lifecycle.Observer;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class ActionBus {
    public BehaviorSubject<Action> subject = BehaviorSubject.create();

    private final LifecycleOwner owner;

    public ActionBus(LifecycleOwner owner) {
        this.owner = owner;
    }

    public void observe(final Class<? extends Action> actionClass, final Observer<Action> observer) {
        BehaviorSubject<Lifecycle.Event> lifecycleSubject = BehaviorSubject.create();
        owner.getLifecycle().addObserver(new RxJavaLifecycle(lifecycleSubject));
        subject.compose(new LifecycleTransformer<Action>(lifecycleSubject.hide()))
                .filter(new Predicate<Action>() {
                    @Override
                    public boolean test(Action action) throws Exception {
                        return action.getClass() == actionClass;
                    }
                })
                .subscribe(new Consumer<Action>() {
                    @Override
                    public void accept(Action action) throws Exception {
                        observer.onChanged(action);
                    }
                });
    }

    public void dispatch(Action action) {
        subject.onNext(action);
    }
}
