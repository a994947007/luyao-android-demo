package com.hc.base.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

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

    public void register(final ActivityStarter activityStarter) {
        Fragment contentFragment = activityStarter.getContentFragment();
        contentFragment.getLifecycle().addObserver(new LifecycleBoundObserver(activityStarter.getStarterId()));
        activityStarterMap.put(activityStarter.getStarterId(), activityStarter);
    }

    class LifecycleBoundObserver implements LifecycleEventObserver {

        private final String key;

        private LifecycleBoundObserver(String key) {
            this.key = key;
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                activityStarterMap.remove(key);
                source.getLifecycle().removeObserver(this);
            }
        }
    }

    public Fragment get(String id) {
        if (!activityStarterMap.containsKey(id)) {
            throw new IllegalArgumentException("id should register with interface ActivityStart");
        }
        return activityStarterMap.get(id).getContentFragment();
    }

    public void unregister(ActivityStarter activityStarter) {
        activityStarterMap.remove(activityStarter.getStarterId());
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
