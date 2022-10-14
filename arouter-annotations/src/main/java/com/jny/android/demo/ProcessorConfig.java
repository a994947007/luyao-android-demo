package com.jny.android.demo;

public interface ProcessorConfig {
    String OPTIONS = "moduleName";

    String APT_PACKAGE = "packageNameForAPT";

    String ACTIVITY_PACKAGE = "android.app.Activity";

    String DEFAULT_GROUP = "/group/default";

    String ROUTER_API_PATH = "com.jny.android.demo.api.ARouterPath";

    String ROUTER_API_GROUP = "com.jny.android.demo.api.ARouterGroup";

    String PATH_METHOD_NAME = "getPathMap";

    String GROUP_METHOD_NAME = "getGroupMap";

    String PATH_MAP_NAME = "pathMap";

    String GROUP_MAP_NAME = "groupMap";

    String PREFIX_PATH_CLASS_NAME = "ARouter$$Path$$";

    String PREFIX_GROUP_CLASS_NAME = "ARouter$$Group$$";
}
