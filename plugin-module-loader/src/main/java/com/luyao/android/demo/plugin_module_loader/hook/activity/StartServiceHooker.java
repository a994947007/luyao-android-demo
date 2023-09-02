package com.luyao.android.demo.plugin_module_loader.hook.activity;

import com.luyao.android.demo.plugin_module_loader.hook.AbstractAMSHooker;

public class StartServiceHooker extends AbstractAMSHooker {

    public StartServiceHooker(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean doHook() {
        return false;
    }
}
