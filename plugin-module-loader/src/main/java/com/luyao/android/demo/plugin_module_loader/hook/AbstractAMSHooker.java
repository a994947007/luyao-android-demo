package com.luyao.android.demo.plugin_module_loader.hook;

public abstract class AbstractAMSHooker implements AMSHooker{

    private final Chain chain;

    public AbstractAMSHooker(Chain chain) {
        this.chain = chain;
    }

    @Override
    public void hook() {
        hookBefore();
        boolean success = doHook();
        if (!success) {
            chain.proceed();
        }
        hookAfter();
    }

    protected abstract boolean doHook();
    protected void hookBefore() { }
    protected void hookAfter() { }
}
