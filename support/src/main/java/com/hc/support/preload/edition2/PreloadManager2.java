package com.hc.support.preload.edition2;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.HashMap;
import java.util.Map;

public class PreloadManager2 {
    private final Map<Integer, PreloadAction2> positionFragmentMap = new HashMap<>();
    private boolean [] isLoads;

    private static class Instance {
        static PreloadManager2 instance =new PreloadManager2();
    }

    public static PreloadManager2 getInstance() {
        return Instance.instance;
    }

    private static final int NO_SCROLLING = 0;
    private static final int LEFT_SCROLLING = 1;
    private static final int RIGHT_SCROLLING = 2;
    private static final float OFFSET_PERCENT_THRESHOLD = 0.02f;
    private int cacheCount = 0;

    private void register(LifecycleOwner lifecycleOwner, final PreloadAction2 preloadAction, final int position) {
        if (positionFragmentMap.get(position) != null) {
            return;
        }
        positionFragmentMap.put(position, preloadAction);
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    source.getLifecycle().removeObserver(this);
                    positionFragmentMap.remove(position);
                    cacheCount --;
                    isLoads[position] = false;
                }
            }
        });
    }

    private void dispatchLeftOffsetPercentScrollEvent(float positionOffset, int position) {
        if (positionOffset > OFFSET_PERCENT_THRESHOLD ) {
            PreloadAction2 preloadAction =  positionFragmentMap.get(position);
            if (preloadAction != null && !isLoads[position]) {
                preloadAction.doPreload();
                isLoads[position] = true;
                cacheCount++;
            }
        }
    }

    private void dispatchRightOffsetPercentScrollEvent(float positionOffset, int position) {
        if (positionOffset > OFFSET_PERCENT_THRESHOLD) {
            PreloadAction2 preloadAction =  positionFragmentMap.get(position + 1);
            if (preloadAction != null && !isLoads[position + 1]) {
                preloadAction.doPreload();
                isLoads[position + 1] = true;
                cacheCount++;
            }
        }
    }

    private static int getContentViewPosition(ViewPager viewPager, View fragmentContentView) {
        int childCount = viewPager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewPager.getChildAt(i) == fragmentContentView) {
                return i;
            }
        }
        return -1;
    }

    public void observe(Fragment fragment, PreloadAction2 preloadAction) {
        final View fragmentContentView = fragment.getView();
        if (fragmentContentView == null) {
            return;
        }

        final ViewPager viewPager = findViewPager(fragmentContentView);
        if (viewPager == null) {
            throw new IllegalArgumentException("fragment must attach in viewPager");
        }
        if (isLoads == null) {
            isLoads = new boolean[viewPager.getAdapter().getCount()];
        }
        int position = viewPager.getAdapter().getItemPosition(fragment);
        if (position != PagerAdapter.POSITION_UNCHANGED) {
            register(fragment, preloadAction, position);
        } else {
            if (viewPager.getOffscreenPageLimit() < viewPager.getAdapter().getCount() - 1) {
                throw new IllegalArgumentException("viewPager OffscreenPageLimit must more than Adapter count");
            }
            position = getContentViewPosition(viewPager, fragmentContentView);
            register(fragment, preloadAction, position);
        }
    }

    private void listeningPagerScroll(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean isScrolling = false;
            private float mPrePositionOffset = 0f;
            private int direction;
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
            public void onPageSelected(int position) { }

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

    private void listeningPager2Scroll(ViewPager2 viewPager2) {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean isScrolling = false;
            private float mPrePositionOffset = 0f;
            private int direction;

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
            public void onPageSelected(int position) { }

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

    private static ViewPager findViewPager(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewPager) {
            return (ViewPager) parent;
        }
        return null;
    }

    private static ViewPager2 findViewPager2(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewPager2) {
            return (ViewPager2) parent;
        }
        return null;
    }
}
