package com.hc;

import com.hc.util.ToastUtils;
import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.common.DialogPlugin;

@InjectModule
public class DialogPluginImpl implements DialogPlugin {
    @Override
    public void testPlugin() {
        ToastUtils.show("dialogPlugin");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
