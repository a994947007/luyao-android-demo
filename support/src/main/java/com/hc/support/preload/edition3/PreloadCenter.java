package com.hc.support.preload.edition3;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.Map;

public class PreloadCenter {
    private final Map<Fragment, PreloadManager> preloadManagerMap = new HashMap<>();

    public void listeningAllPreloadAction(ViewPager viewPager, Fragment[] fragments, float defaultOffsetPercent, int defaultOffsetPixes) {
        PreloadManager preloadManager = PreloadManager.newInstance(defaultOffsetPercent, defaultOffsetPixes);
        for (Fragment fragment : fragments) {
            preloadManagerMap.put(fragment, preloadManager);
        }
        preloadManager.listeningAllPreloadAction(viewPager, fragments);
    }


    private static class Instance {
        static PreloadCenter preloadCenter;
        static {
            preloadCenter = new PreloadCenter();
        }
    }

    public static PreloadCenter getInstance() {
        return Instance.preloadCenter;
    }
}
