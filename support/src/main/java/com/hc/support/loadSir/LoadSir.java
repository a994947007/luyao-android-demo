package com.hc.support.loadSir;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class LoadSir {
    final Map<Class<? extends Callback>, Callback> callbacks = new HashMap<>();
    private static volatile LoadSir loadSir;

    public LoadSir addCallback(Callback callback) {
        callbacks.put(callback.getClass(), callback);
        return this;
    }

    public static LoadSir buildLoadSir() {
        if (loadSir == null) {
            synchronized (LoadSir.class) {
                if (loadSir == null) {
                    loadSir = new LoadSir();
                }
            }
        }
        return loadSir;
    }

    public LoadSir build() {
        return new LoadSir();
    }

    public static LoadService register(View contentView, Callback.OnReloadListener onReloadListener) {
        return new LoadService(loadSir.callbacks, contentView, onReloadListener);
    }
}
