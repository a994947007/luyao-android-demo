package com.hc.support.preload;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.Map;

public class PreloadManager {

    private final Map<Integer, PreloadAction> positionFragmentMap = new HashMap<>();
    private boolean [] isLoads;

    private static class Instance {
        static PreloadManager instance = new PreloadManager();
    }

    public static PreloadManager getInstance() {
        return Instance.instance;
    }

    private static final int NO_SCROLLING = 0;
    private static final int LEFT_SCROLLING = 1;
    private static final int RIGHT_SCROLLING = 2;
    private static final float OFFSET_PERCENT_THRESHOLD = 0.02f;
/*
    public void register(final PreloadAction preloadAction) {
        final int position = mViewPager.getAdapter().getItemPosition(preloadAction);
        positionFragmentMap.put(position, preloadAction);
        preloadAction.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    source.getLifecycle().removeObserver(this);
                    positionFragmentMap.remove(position);
                }
            }
        });
    }
*/

    private void register(final PreloadAction preloadAction, final int position) {
        positionFragmentMap.put(position, preloadAction);
        preloadAction.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    source.getLifecycle().removeObserver(this);
                    positionFragmentMap.remove(position);
                }
            }
        });
    }

    public void register(final ViewPager viewPager, final LifecycleOwner [] preloadActions) {
        for (int i = 0; i < preloadActions.length; i++) {
            if (preloadActions[i] instanceof PreloadAction) {
                register((PreloadAction) preloadActions[i], i);
            }
        }
        isLoads = new boolean[viewPager.getAdapter().getCount()];
        isLoads[viewPager.getCurrentItem()] = true;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean isScrolling = false;
            private float mPrePositionOffset = 0f;
            private int direction;

            private void dispatchLeftOffsetPercentScrollEvent(float positionOffset, int position) {
                if (positionOffset > OFFSET_PERCENT_THRESHOLD ) {
                    PreloadAction preloadAction =  positionFragmentMap.get(position);
                    if (preloadAction != null && !isLoads[position]) {
                        preloadAction.doLoad();
                        isLoads[position] = true;
                    }
                }
            }

            private void dispatchRightOffsetPercentScrollEvent(float positionOffset, int position) {
                if (positionOffset > OFFSET_PERCENT_THRESHOLD) {
                    PreloadAction preloadAction =  positionFragmentMap.get(position + 1);
                    if (preloadAction != null && !isLoads[position + 1]) {
                        preloadAction.doLoad();
                        isLoads[position + 1] = true;
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!isScrolling) {
                    return;
                }

                if (positionOffset - mPrePositionOffset < 0) {
                    direction = LEFT_SCROLLING;
                    dispatchLeftOffsetPercentScrollEvent(1 - positionOffset, position);
                } else if (positionOffset - mPrePositionOffset > 0) {
                    direction = RIGHT_SCROLLING;
                    dispatchRightOffsetPercentScrollEvent(positionOffset, position);
                } else {
                    direction = NO_SCROLLING;
                }

                mPrePositionOffset = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {
               // Log.d("PreloadManager", "position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) { // start scroll
                    mPrePositionOffset = 0;
                    isScrolling = true;
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    isScrolling = false;
                }
            }
        });
    }
}
