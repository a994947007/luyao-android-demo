package com.hc.support.mvi;

public interface IModel<T, VM extends BaseViewModel<T>, V extends IView<VM>> {
    VM onCreateViewModel();

    V onCreateView();
}
