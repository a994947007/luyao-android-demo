package com.jny.android.demo.api;

import java.util.Map;

public interface ARouterGroup {
    Map<String, Class<? extends ARouterPath>> getGroupMap();
}
