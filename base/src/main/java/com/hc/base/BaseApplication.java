package com.hc.base;

import android.app.Application;

import com.hc.base.init.InitModuleTask;
import com.jny.android.demo.base_util.AppEnvironment;

import java.util.ServiceLoader;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEnvironment.setContext(getApplicationContext());
        AppEnvironment.setApplication(this);
        for (InitModuleTask initModuleTask : ServiceLoader.load(InitModuleTask.class)) {
            initModuleTask.execute();
        }
    }
}
