package com.hc.support.preload.edition3.core;

public class PreloadActionProxy implements PreloadAction, PreloadFilter{

    private final PreloadAction mPreloadAction;
    private final PreloadFilter mPreloadFilter;

    public PreloadActionProxy(PreloadAction preloadAction, PreloadFilter preloadFilter) {
        this.mPreloadAction = preloadAction;
        this.mPreloadFilter = preloadFilter;
    }

    @Override
    public void preload() {
        this.mPreloadAction.preload();
    }

    @Override
    public boolean filter(int direction, float offsetPercent, int offsetPixes) {
        return mPreloadFilter.filter(direction, offsetPercent, offsetPixes);
    }
}
