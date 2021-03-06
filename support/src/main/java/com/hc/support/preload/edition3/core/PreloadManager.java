package com.hc.support.preload.edition3.core;

import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

public class PreloadManager {

    private final ViewPagerPreloadContext mViewPagerPreloadContext = new ViewPagerPreloadContext();

    private PreloadFilter mDefaultPreloadFilter;

    private boolean mHasRegisterPageScrollListener;

    public static final int NO_SCROLLING = 0;
    public static final int LEFT_SCROLLING = 1;
    public static final int RIGHT_SCROLLING = 2;


    private PreloadManager() { }

    public static PreloadManager newInstance(final float defaultOffsetPercent, final int defaultOffsetPixes) {
        PreloadManager preloadManager = new PreloadManager();
        preloadManager.mDefaultPreloadFilter = new PreloadFilter() {
            @Override
            public boolean filter(int direction, float offsetPercent, int offsetPixes) {
                return offsetPercent >= defaultOffsetPercent || offsetPixes >= defaultOffsetPixes  ;
            }
        };
        return preloadManager;
    }

    public void register(ViewPager viewPager, LifecycleOwner lifecycleOwner, int position ,PreloadAction preloadAction, PreloadFilter preloadFilter) {
        if (!mHasRegisterPageScrollListener) {
            viewPager.addOnPageChangeListener(new PreloadManager.ViewPagerChangeListener(viewPager));
            mHasRegisterPageScrollListener = true;
        }
        if (preloadFilter == null) {
            preloadFilter = mDefaultPreloadFilter;
        }
        mViewPagerPreloadContext.add(lifecycleOwner, position, preloadAction, preloadFilter);
    }

    private void dispatchLeftOffsetPercentScrollEvent(float offsetPercent, int offsetPixels,int position) {
        PreloadActionProxy preloadAction =  mViewPagerPreloadContext.get(position);
        if (preloadAction == null) {
            return;
        }
        if (preloadAction.filter(LEFT_SCROLLING, offsetPercent, offsetPixels)) {
            preloadAction.preload();
        }
    }

    private void dispatchRightOffsetPercentScrollEvent(float offsetPercent, int offsetPixels, int position) {
        PreloadActionProxy preloadAction =  mViewPagerPreloadContext.get(position);
        if (preloadAction == null) {
            return;
        }
        if (preloadAction.filter(RIGHT_SCROLLING, offsetPercent, offsetPixels)) {
            preloadAction.preload();
        }
    }

    private static int getViewPagerMaxOffset(ViewPager viewPager) {
        return viewPager.getMeasuredWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight() + viewPager.getPageMargin();
    }


    private class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        private boolean mIsScrolling = false;
        private float mPrePositionOffset;
        private int mDirection = NO_SCROLLING;
        private int mPrePosition;
        private final int VIEW_PAGER_MAX_OFFSET;

        public ViewPagerChangeListener(ViewPager viewPager) {
            VIEW_PAGER_MAX_OFFSET = getViewPagerMaxOffset(viewPager);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (!mIsScrolling) {
                return;
            }

            if (positionOffset - mPrePositionOffset < 0 && position <= mPrePosition) {
                mDirection = LEFT_SCROLLING;
                dispatchLeftOffsetPercentScrollEvent(1 - positionOffset, VIEW_PAGER_MAX_OFFSET - positionOffsetPixels, position);
            } else if (positionOffset - mPrePositionOffset > 0 && position >= mPrePosition) {
                mDirection = RIGHT_SCROLLING;
                dispatchRightOffsetPercentScrollEvent(positionOffset, positionOffsetPixels, position + 1);
            } else {
                mDirection = NO_SCROLLING;
            }

            mPrePositionOffset = positionOffset;
            mPrePosition = position;
        }

        @Override
        public void onPageSelected(int position) { }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) { // start scroll
                mPrePositionOffset = 0f;
                mIsScrolling = true;
            }
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mIsScrolling = false;
            }
        }
    }
}
