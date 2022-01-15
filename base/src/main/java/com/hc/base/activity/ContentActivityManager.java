package com.hc.base.activity;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ContentActivityManager {

    private final Map<String, ActivityStarter> activityStarterMap = new HashMap<>();

    private static class Instance {
        private static ContentActivityManager instance = null;
        static {
            instance = new ContentActivityManager();
        }
    }

    public static ContentActivityManager getInstance() {
        return Instance.instance;
    }

    public ContentActivityManager() {
        ServiceLoader<ActivityStarter> loads = ServiceLoader.load(ActivityStarter.class);
        for (ActivityStarter activityStarter : loads) {
            register(activityStarter);
        }
    }

    public void register(ActivityStarter activityStarter) {
        activityStarterMap.put(activityStarter.getId(), activityStarter);
    }

    public Fragment get(String id) {
        return activityStarterMap.get(id).getContentFragment();
    }

    public void unregister(ActivityStarter activityStarter) {
        activityStarterMap.remove(activityStarter.getId());
    }

    public void load() {
        ServiceLoader<ActivityStarter> loads = ServiceLoader.load(ActivityStarter.class);
        for (ActivityStarter activityStarter : loads) {
            register(activityStarter);
        }
    }

    public void clear() {
        activityStarterMap.clear();
    }
}
