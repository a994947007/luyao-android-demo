package com.luyao.android.demo.plugin_module_loader.hook.activity;

import com.luyao.android.demo.plugin_module_loader.hook.AbstractAMSHooker;

public class StartActivityAPI29Hooker extends AbstractAMSHooker {

    public StartActivityAPI29Hooker(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean doHook() {
        return false;
    }
}
