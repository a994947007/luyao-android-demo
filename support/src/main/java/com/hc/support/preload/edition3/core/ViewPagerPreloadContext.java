package com.hc.support.preload.edition3.core;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerPreloadContext {
    private final Map<Integer, PreloadActionProxy> mPreloadActionMap;

    public ViewPagerPreloadContext() {
        mPreloadActionMap = new HashMap<>();
    }

    public void add(LifecycleOwner lifecycleOwner, final int position, PreloadAction preloadAction, PreloadFilter preloadFilter) {
        if (mPreloadActionMap.get(position) != null) {
            return;
        }
        PreloadActionProxy preloadActionProxy = new PreloadActionProxy(preloadAction, preloadFilter);
        mPreloadActionMap.put(position, preloadActionProxy);
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    source.getLifecycle().removeObserver(this);
                    mPreloadActionMap.remove(position);
                }
            }
        });
    }

    public void remove(int position) {
        mPreloadActionMap.remove(position);
    }

    public PreloadActionProxy get(int position) {
        return mPreloadActionMap.get(position);
    }

    public int getPreloadActionCount() {
        return mPreloadActionMap.size();
    }
}
