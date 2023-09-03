package com.luyao.android.demo.plugin_module_loader;

import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.schedule.Schedules;
import com.jny.android.demo.plugin.PluginInit;
import com.jny.android.demo.plugin.PluginUtils;
import com.luyao.android.demo.plugin_module_loader.hook.AMSHookerManager;

import java.util.HashSet;
import java.util.Set;

public class PluginLoader {
    private static final String TAG = "PluginLoader";

    private static final Set<String> pluginLoadMap = new HashSet<>();

    public static Observable<Boolean> loadPlugin(String moduleName) {
        return Observable.create((Observable.OnSubscriber<Boolean>) subscriber -> loadPlugin(moduleName, new Callback() {
            @Override
            public void onSuccess() {
                subscriber.onNext(true);
                subscriber.onComplete();
            }

            @Override
            public void onError(Throwable r) {
                subscriber.onError(r);
            }
        }))
        .subscribeOn(Schedules.IO)
        .observeOn(Schedules.MAIN);
    }

    public static void loadPlugin(String moduleName, Callback callback) {
        if (pluginLoadMap.contains(moduleName)) {
            callback.onSuccess();
            return;
        }
        String modulePath = PluginModulePathMapper.getModulePath(moduleName);
        if (modulePath == null || modulePath.equals("")) {
            callback.onError(new IllegalStateException("modulePath is empty"));
        }
        PluginModuleLoader.getInstance().load(AMSHookerManager.getInstance().getContext(), modulePath, new Callback() {
            @Override
            public void onSuccess() {
                PluginInit modulePluginInit = PluginUtils.getModulePluginInit(moduleName);
                if (modulePluginInit != null) {
                    modulePluginInit.execute();
                    callback.onSuccess();
                    pluginLoadMap.add(moduleName);
                }
            }

            @Override
            public void onError(Throwable r) {
                callback.onError(r);
            }
        });
    }
}
