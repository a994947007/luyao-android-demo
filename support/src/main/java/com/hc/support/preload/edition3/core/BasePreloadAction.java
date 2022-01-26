package com.hc.support.preload.edition3.core;

public abstract class BasePreloadAction implements PreloadAction {

    private boolean isPreload = false;

    public void reset() {
        isPreload = false;
    }

    @Override
    public void preload() {
        if (isPreload) {
            return;
        }
        doPreload();
        isPreload = true;
    }

    public abstract void doPreload();
}
