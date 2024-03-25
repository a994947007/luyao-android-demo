package com.hc.support.mvi2;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

import com.hc.support.mvps.Presenter;

public abstract class MVIPresenter<T extends UIState, VM extends BaseViewModelV2<T>, V extends IView<T, VM>> extends Presenter implements IModel<T, VM, V> {
    protected VM vm;
    protected V v;
    protected ActionBus actionBus;
    private final LifecycleOwner lifecycleOwner;

    public MVIPresenter(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        vm = onCreateViewModel();
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
