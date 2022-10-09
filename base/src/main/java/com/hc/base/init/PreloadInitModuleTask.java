package com.hc.base.init;

import com.google.auto.service.AutoService;
import com.hc.support.preload.edition3.core.PreloadActionFactory;
import com.hc.support.preload.edition3.core.PreloadPipeline;
import java.util.ServiceLoader;

@AutoService({InitModuleTask.class})
public class PreloadInitModuleTask implements InitModuleTask {

    public void execute() {
        for (PreloadActionFactory preloadActionFactory : ServiceLoader.load(PreloadActionFactory.class)) {
            PreloadPipeline preloadPipeline = preloadActionFactory.create();
            preloadPipeline.preload();
        }
    }
}
