package com.hc.support.mvi2;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

public abstract class BaseModel<T extends UIState, VM extends BaseViewModelV2<T>, V extends IView<T, VM>> implements IModel<T, VM, V> {

    protected final LifecycleOwner lifecycleOwner;
    protected VM vm;
    protected V v;
    @Nullable
    protected ActionBus actionBus;

    public BaseModel(@NonNull LifecycleOwner lifecycleOwner, @NonNull ActionBus actionBus) {
        this(lifecycleOwner);
        this.actionBus = actionBus;
    }

    public BaseModel(@NonNull LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        vm = onCreateViewModel();
        v = onCreateView();
    }

    public final void attach(View view) {
        vm.actionBus = actionBus;
        v.onCreateView(view);
        v.onObserveData(vm);
        onAttach();
    }

    protected void onAttach() {}

    public final void detach() {
        onDetach();
    }

    protected void onDetach() {}
}
