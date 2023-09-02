package com.luyao.android.demo.plugin_module_loader.hook;

import java.util.List;

public class RealHookerChain implements AMSHooker.Chain {

    private final List<AMSHooker> hookers;
    private int currentIndex = 0;

    public RealHookerChain(List<AMSHooker> hookers) {
        this.hookers = hookers;
    }

    @Override
    public void proceed() {
        if (currentIndex >= hookers.size()) {
            return;
        }
        AMSHooker hooker = hookers.get(currentIndex);
        currentIndex ++;
        hooker.hook();
    }
}
