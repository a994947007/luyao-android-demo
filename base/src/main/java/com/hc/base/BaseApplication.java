package com.hc.base;

import android.app.Application;

import com.hc.base.init.InitModuleTask;
import com.hc.base.net.ApiServiceBeanCreator;
import com.hc.support.singleton.Singleton;
import com.hc.support.singleton.annotation.ApiService;
import com.jny.android.demo.base_util.AppEnvironment;
import com.jny.android.demo.plugin.PluginInit;

import java.util.ServiceLoader;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEnvironment.setContext(getApplicationContext());
        AppEnvironment.setApplication(this);
        Singleton.registerBeanCreator(ApiService.class, new ApiServiceBeanCreator());
        for (InitModuleTask initModuleTask : ServiceLoader.load(InitModuleTask.class)) {
            initModuleTask.execute();
        }
        for (PluginInit pluginInit : ServiceLoader.load(PluginInit.class)) {
            pluginInit.execute();
        }
    }
}
