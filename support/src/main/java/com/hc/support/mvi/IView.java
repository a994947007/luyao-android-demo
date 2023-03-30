package com.hc.support.mvi;

import android.view.View;

public interface IView<VM extends BaseViewModel> {

    void onCreateView(View rootView);

    void onObserveData(VM vm);
}
