package com.jny.android.demo;

public interface ProcessorConfig {
    String OPTIONS = "moduleName";

    String APT_PACKAGE = "packageNameForAPT";

    String ACTIVITY_PACKAGE = "android.app.Activity";

    String FRAGMENT_PACKAGE = "androidx.fragment.app.Fragment";

    String RES_PACKAGE = "com.jny.android.demo.api.Res";

    String ROUTER_TAB_CLASS_PACKAGE = "com.jny.android.demo.router";

    String ROUTER_TAB_CLASS = "$RouterTab";

    String ROUTER_TAB_METHOD_NAME = "getRouterTab";

    String ROUTER_TAB_MAP_NAME = "router";

    String DEFAULT_GROUP = "default";

    String ROUTER_API_PATH = "com.jny.android.demo.api.ARouterPath";

    String ROUTER_API_GROUP = "com.jny.android.demo.api.ARouterGroup";

    String PATH_METHOD_NAME = "getPathMap";

    String GROUP_METHOD_NAME = "getGroupMap";

    String PATH_MAP_NAME = "pathMap";

    String GROUP_MAP_NAME = "groupMap";

    String PREFIX_PATH_CLASS_NAME = "ARouter$$Path$$";

    String PREFIX_GROUP_CLASS_NAME = "ARouter$$Group$$";

    String LOAD_PARAM_METHOD_PARAM_NAME = "entity";

    String LOAD_PARAM_METHOD_NAME = "load";

    String LOAD_PARAM_METHOD_TEMP_ARG = "t";

    String STRING = "java.lang.String";

    String PARAMETER_LOADER_CLASS_NAME = "$Parameter";
}
