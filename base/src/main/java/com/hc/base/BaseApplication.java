package com.hc.base;

import android.app.Application;

import com.hc.support.init.InitModuleTask;

import java.util.ServiceLoader;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEnvironment.setContext(getApplicationContext());
        for (InitModuleTask initModuleTask : ServiceLoader.load(InitModuleTask.class)) {
            initModuleTask.execute();
        }
    }
}
