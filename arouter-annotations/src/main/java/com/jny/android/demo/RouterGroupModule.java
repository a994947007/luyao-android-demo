package com.jny.android.demo;

public class RouterGroupModule {

    public String modulePath;

    public String moduleName;

    public RouterGroupModule(String modulePath, String moduleName) {
        this.modulePath = modulePath;
        this.moduleName = moduleName;
    }

    public static RouterGroupModule create(String modulePath, String groupName) {
        return new RouterGroupModule(modulePath, groupName);
    }
}
