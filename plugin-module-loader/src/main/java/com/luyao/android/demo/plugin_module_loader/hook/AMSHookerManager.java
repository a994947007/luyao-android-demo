package com.luyao.android.demo.plugin_module_loader.hook;

import android.content.Context;

import com.luyao.android.demo.plugin_module_loader.hook.activity.StartActivityAPI29Hooker;
import com.luyao.android.demo.plugin_module_loader.hook.activity.StartActivityAPI30Hooker;
import com.luyao.android.demo.plugin_module_loader.hook.resource.ResourcesAPI30Hooker;
import com.luyao.android.demo.plugin_module_loader.hook.service.StartServiceHooker;

import java.util.ArrayList;
import java.util.List;

public class AMSHookerManager {

    private RealHookerChain startActivityHookChain;
    private RealHookerChain startServiceHookChain;
    private RealHookerChain resourceHookChain;

    private Context context;

    private AMSHookerManager() {}

    private static class Instance {
        private static final AMSHookerManager instance = new AMSHookerManager();
    }

    public static AMSHookerManager getInstance() {
        return Instance.instance;
    }

    protected RealHookerChain getStartActivityHookConfig() {
        List<AMSHooker> hookers = new ArrayList<>();
        startActivityHookChain = new RealHookerChain(hookers);
        hookers.add(new StartActivityAPI29Hooker(startActivityHookChain));
        hookers.add(new StartActivityAPI30Hooker(startActivityHookChain));
        return startActivityHookChain;
    }

    protected RealHookerChain getStartServiceHookConfig() {
        List<AMSHooker> hookers = new ArrayList<>();
        startServiceHookChain = new RealHookerChain(hookers);
        hookers.add(new StartServiceHooker(startServiceHookChain));
        return startServiceHookChain;
    }

    protected RealHookerChain getResourceHookChain() {
        List<AMSHooker> hookers = new ArrayList<>();
        resourceHookChain = new RealHookerChain(hookers);
        hookers.add(new ResourcesAPI30Hooker(resourceHookChain));
        return resourceHookChain;
    }

    public void hook() {
        startActivityHookChain.proceed();
        startServiceHookChain.proceed();
        resourceHookChain.proceed();
    }

    public void init(Context appContext) {
        this.context = appContext;
        startActivityHookChain = getStartActivityHookConfig();
        startServiceHookChain = getStartServiceHookConfig();
        resourceHookChain = getResourceHookChain();
    }

    public Context getContext() {
        return this.context;
    }
}
