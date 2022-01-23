package com.hc.support.preload.edition1;

import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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
    private int cacheCount = 0;

    private void register(final PreloadAction preloadAction, final int position) {
        if (positionFragmentMap.get(position) != null) {
            return;
        }
        positionFragmentMap.put(position, preloadAction);
        preloadAction.getLifecycle().addObserver(new LifecycleEventObserver() {
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
            PreloadAction preloadAction =  positionFragmentMap.get(position);
            if (preloadAction != null && !isLoads[position]) {
                preloadAction.doLoad();
                isLoads[position] = true;
                cacheCount++;
            }
        }
    }

    private void dispatchRightOffsetPercentScrollEvent(float positionOffset, int position) {
        if (positionOffset > OFFSET_PERCENT_THRESHOLD) {
            PreloadAction preloadAction =  positionFragmentMap.get(position + 1);
            if (preloadAction != null && !isLoads[position + 1]) {
                preloadAction.doLoad();
                isLoads[position + 1] = true;
                cacheCount++;
            }
        }
    }

    private void cachePreloadAction(int position, Object obj) {
        if (positionFragmentMap.get(position) != null) {
            return;
        }
        if (!(obj instanceof PreloadAction)) {
            return;
        }
        PreloadAction preloadAction = (PreloadAction) obj;
        register(preloadAction, position);
    }

    private void cacheCurrentItems(ViewPager viewPager) {
        if (cacheCount == viewPager.getAdapter().getCount()) {
            return;
        }
        Class<ViewPager> viewPagerClass = ViewPager.class;
        try {
            Field mItemsField = viewPagerClass.getDeclaredField("mItems");
            mItemsField.setAccessible(true);
            Object itemsObject = mItemsField.get(viewPager);
            if (itemsObject != null) {
                List<Object> items = (List<Object>) itemsObject;
                if (items.size() > 0) {
                    Field objField = null;
                    Field positionField = null;
                    for (Object item : items) {
                        if (objField == null) {
                            objField = item.getClass().getDeclaredField("object");
                        }
                        if (positionField == null) {
                            positionField = item.getClass().getDeclaredField("position");
                        }
                        objField.setAccessible(true);
                        positionField.setAccessible(true);
                        Object obj = objField.get(item);
                        int position = (int) positionField.get(item);
                        cachePreloadAction(position, obj);
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void listeningAllPreloadAction(ViewPager viewPager) {
        isLoads = new boolean[viewPager.getAdapter().getCount()];
        isLoads[viewPager.getCurrentItem()] = true;
        cacheCount ++;
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

    private void listeningAllPreloadAction(ViewPager2 viewPager2) {
        isLoads = new boolean[viewPager2.getAdapter().getItemCount()];
        isLoads[viewPager2.getCurrentItem()] = true;
        cacheCount ++;
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

    public void listeningPagerScroll(final ViewPager viewPager) {
        if (viewPager.getOffscreenPageLimit() < viewPager.getAdapter().getCount() - 1) {
            throw new IllegalArgumentException("viewPager OffscreenPageLimit must more than fragments count");
        }
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                cacheCurrentItems(viewPager);
            }
        });
        listeningAllPreloadAction(viewPager);
    }

    public void listeningPager2Scroll(final ViewPager2 viewPager2) {
        if (viewPager2.getOffscreenPageLimit() < viewPager2.getAdapter().getItemCount() - 1) {
            throw new IllegalArgumentException("viewPager OffscreenPageLimit must more than fragments count");
        }
        viewPager2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int itemCount = viewPager2.getAdapter().getItemCount();
                for (int i = 0; i < itemCount; i++) {
                    // TODO
                }
            }
        });
        listeningAllPreloadAction(viewPager2);
    }

    public void listeningPagerScroll(final ViewPager viewPager, final LifecycleOwner [] preloadActions) {
        for (int i = 0; i < preloadActions.length; i++) {
            if (preloadActions[i] instanceof PreloadAction) {
                register((PreloadAction) preloadActions[i], i);
            }
        }
        listeningAllPreloadAction(viewPager);
    }

    public void listeningPager2Scroll(final ViewPager2 viewPager2, final LifecycleOwner [] preloadActions) {
        for (int i = 0; i < preloadActions.length; i++) {
            if (preloadActions[i] instanceof PreloadAction) {
                register((PreloadAction) preloadActions[i], i);
            }
        }
        listeningAllPreloadAction(viewPager2);
    }
}
