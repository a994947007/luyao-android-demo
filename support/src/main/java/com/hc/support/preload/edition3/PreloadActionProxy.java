package com.hc.support.preload.edition3;

public class PreloadActionProxy implements PreloadAction, PreloadFilter{

    private final PreloadAction mPreloadAction;
    private final PreloadFilter mPreloadFilter;
    private boolean isLoaded = false;

    public PreloadActionProxy(PreloadAction preloadAction, PreloadFilter preloadFilter) {
        this.mPreloadAction = preloadAction;
        this.mPreloadFilter = preloadFilter;
    }

    @Override
    public void doPreload() {
        this.mPreloadAction.doPreload();
        isLoaded = true;
    }

    public boolean isPreloaded() {
        return isLoaded;
    }

    @Override
    public boolean filter(float offsetPercent, int offsetPixes) {
        return mPreloadFilter.filter(offsetPercent, offsetPixes);
    }
}
