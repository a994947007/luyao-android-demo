package com.hc.support.mvi;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

public abstract class BaseViewModel<T> {
    public State<T> uiState;

    public LifecycleOwner lifecycleOwner;

    @Nullable
    public ActionBus actionBus;

    public abstract void dispatch(Action action);

    public void onCreate() {
        uiState = State.create(lifecycleOwner);
    }
}
