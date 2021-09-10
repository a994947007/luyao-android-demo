package com.hc.my_views.nestedScrollVIew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class NestedScrollViewChild extends FrameLayout implements MyNestedScrollingChild {
    private float mLastX;
    private float mLastY;
    private int [] mScrollConsumed = new int[2];
    private MyNestedScrollingChildHelper mNestedChildHelper;
    private OverScroller overScroller;
    public NestedScrollViewChild(Context context) {
        super(context);
    }

    public NestedScrollViewChild(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNestedChildHelper = new MyNestedScrollingChildHelper(this);
        overScroller = new OverScroller(getContext());
    }

    /**
     * 这里所有Nested事件的起点
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = event.getRawX();
            mLastY = event.getRawY();
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (mLastX - event.getRawX());
            int dy = (int) (mLastY - event.getRawY());
            mLastY = event.getRawY();
            dispatchNestedPreScroll(dx, dy, mScrollConsumed);
            dy -= mScrollConsumed[1];
            int oldY = getScrollY();
            overScrollBy(dy);

            final int scrolledDeltaY = getScrollY() - oldY;
            final int unConsumedY = dy - scrolledDeltaY;
            dispatchNestedScroll(0, scrolledDeltaY, 0, unConsumedY, mScrollConsumed);
            // 自己再消费
            // ...
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            stopNestedScroll();
        }
        return true;
    }

    private void overScrollBy(int dy) {
        if (getChildCount() == 0) {
            return;
        }
        View child = getChildAt(0);
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int spaceHeight = child.getHeight() + lp.bottomMargin + lp.topMargin - getHeight() - getPaddingTop() - getPaddingBottom();
        if (dy < 0) {
            if (Math.abs(dy) - Math.abs(getScrollY()) >= 0) {
                scrollBy(0, -getScrollY());
            } else {
                scrollBy(0, dy);
            }
        } else if (dy > 0) {
            if (spaceHeight - getScrollY() - dy >=0) {
                scrollBy(0, dy);
            } else {
                scrollBy(0, spaceHeight - getScrollY());
            }
        }

    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] mScrollConsumed) {
        return mNestedChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mScrollConsumed);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedChildHelper.startNestedScroll(axes);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed) {
        return mNestedChildHelper.dispatchNestedPreScroll(dx, dy, consumed);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft()
                + getPaddingRight(), lp.width);

        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
}
