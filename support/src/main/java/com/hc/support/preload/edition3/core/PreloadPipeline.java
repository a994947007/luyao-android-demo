package com.hc.support.preload.edition3.core;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PreloadPipeline extends ViewModel implements PreloadAction {
    private final List<PreloadAction> preloadPipeline = new ArrayList<>();

    public void addFirst(PreloadAction preloadAction) {
        preloadPipeline.add(0, preloadAction);
    }

    public void addLast(PreloadAction preloadAction) {
        preloadPipeline.add(preloadAction);
    }

    @Override
    public void preload() {
        for (PreloadAction preloadAction : preloadPipeline) {
            preloadAction.preload();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        preloadPipeline.clear();
    }
}
