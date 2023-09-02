package com.luyao.android.demo.plugin_module_loader.hook;

public interface AMSHooker {
    void hook();

    interface Chain {
        void proceed();
    }
}
