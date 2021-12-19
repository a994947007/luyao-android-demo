package com.hc.support.rxJava.observable;

public interface ObservableTransformer<Upstream, Downstream> {

    ObservableSource<Downstream> apply(Observable<Upstream> upstream);
}
