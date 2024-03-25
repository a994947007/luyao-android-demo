package com.hc.support.mvi2;

import android.view.View;

public interface IView<T extends UIState, VM extends BaseViewModelV2<T>> {

    void onCreateView(View rootView);

    void onObserveData(VM vm);
}
