package com.hc.support.mvi2;

public interface IModel<T extends UIState, VM extends BaseViewModelV2<T>, V extends IView<T, VM>> {
    VM onCreateViewModel();

    V onCreateView();
}
