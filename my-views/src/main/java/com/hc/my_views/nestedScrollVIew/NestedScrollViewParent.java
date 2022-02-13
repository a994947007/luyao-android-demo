package com.hc.my_views.nestedScrollVIew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class
NestedScrollViewParent extends FrameLayout implements MyNestedScrollingParent {
    private View child;
    public NestedScrollViewParent(Context context) {
        super(context);
    }

    public NestedScrollViewParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.child = child;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int spaceHeight = child.getHeight() + lp.bottomMargin + lp.topMargin - getHeight() - getPaddingTop() - getPaddingBottom();
        int oldY = getScrollY();
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
        final int scrolledDeltaY = getScrollY() - oldY;
        consumed[1] = scrolledDeltaY;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
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
