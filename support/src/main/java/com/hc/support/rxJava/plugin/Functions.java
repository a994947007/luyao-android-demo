package com.hc.support.rxJava.plugin;

import com.hc.support.rxJava.observer.Action;
import com.hc.support.rxJava.observer.Consumer;
import com.hc.support.rxJava.plugin.RxJavaPlugins;

public class Functions {
    public static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        @Override
        public void accept(Object o) {}
    };

    public static final Consumer<Throwable> ERROR_CONSUMER = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable r) {
            RxJavaPlugins.onError(r);
        }
    };

    public static final Action EMPTY_ACTION = new Action() {
        @Override
        public void run() {}
    };

    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}
