package com.jny.react_native;

import com.google.auto.service.AutoService;
import com.hc.support.init.InitModuleTask;
import com.jny.react_native.core.ReactPackageManager;

@AutoService({InitModuleTask.class})
public class ReactInitModule implements InitModuleTask {
    @Override
    public void execute() {
        ReactPackageManager.getInstance().registerAll();
    }
}
