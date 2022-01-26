package com.hc.base.init;

import android.util.Log;

import com.google.auto.service.AutoService;
import com.hc.support.preload.edition3.core.PreloadAction;
import com.hc.support.preload.edition3.core.PreloadActionFactory;
import com.hc.support.preload.edition3.core.PreloadPipeline;

@AutoService({PreloadActionFactory.class})
public class SlidePreloadActionFactory implements PreloadActionFactory {
    @Override
    public PreloadPipeline create() {
        PreloadPipeline preloadPipeline = new PreloadPipeline();
        preloadPipeline.addLast(new PreloadAction() {
            @Override
            public void preload() {
                Log.d("SlidePreloadActionFactory", "toast1");
            }
        });
        preloadPipeline.addLast(new PreloadAction() {
            @Override
            public void preload() {
                Log.d("SlidePreloadActionFactory", "toast2");
            }
        });
        return preloadPipeline;
    }
}
