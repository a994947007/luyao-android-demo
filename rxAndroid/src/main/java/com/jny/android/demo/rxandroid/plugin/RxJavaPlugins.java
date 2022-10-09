package com.jny.android.demo.rxandroid.plugin;

import com.jny.android.demo.rxandroid.observer.Consumer;

public final class RxJavaPlugins {
    static volatile Consumer<Throwable> errorHandler;

    public static void setErrorHandler(Consumer<Throwable> errorHandler) {
        RxJavaPlugins.errorHandler = errorHandler;
    }

    public static void onError(Throwable r) {
        if (errorHandler != null) {
            errorHandler.accept(r);
            return;
        }
        r.printStackTrace();
    }
}
