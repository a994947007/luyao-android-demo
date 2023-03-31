package com.hc.support.mvi;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.hc.support.mvps.Presenter;

public abstract class MVIPresenter<T, VM extends BaseViewModel<T>, V extends IView<VM>> extends Presenter implements IModel<T, VM, V> {

    protected final LifecycleOwner lifecycleOwner;
    protected VM vm;
    protected V v;
    protected ActionBus actionBus;

    public MVIPresenter(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        vm = onCreateViewModel();
        vm.lifecycleOwner = lifecycleOwner;
        vm.onCreate();
        v = onCreateView();
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        v.onCreateView(rootView);
    }

    @Override
    protected void doInject() {
        super.doInject();
        actionBus = inject(ActionBus.class);
    }

    @Override
    protected void onBind() {
        vm.actionBus = actionBus;
        v.onObserveData(vm);
    }
}
