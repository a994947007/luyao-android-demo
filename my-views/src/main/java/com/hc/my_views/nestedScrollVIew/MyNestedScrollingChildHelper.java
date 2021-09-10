package com.hc.my_views.nestedScrollVIew;

import android.view.View;
import android.view.ViewParent;

import androidx.core.view.NestedScrollingParent;

public class MyNestedScrollingChildHelper {
    private final View view;
    private MyNestedScrollingParent mNestedScrollingParent;
    public MyNestedScrollingChildHelper(View view) {
        this.view = view;
    }

    public boolean startNestedScroll(int axes) {
        if (hasNestedScrollingParent()) {
            return true;
        }
        ViewParent child = (ViewParent) view;
        ViewParent parent = child.getParent();
        while (parent != null) {
            if (parent instanceof MyNestedScrollingParent) {
                mNestedScrollingParent = (MyNestedScrollingParent) parent;
                if (!mNestedScrollingParent.onStartNestedScroll((View) child, view, axes)) {
                    return false;
                }
                mNestedScrollingParent.onNestedScrollAccepted((View) child, view, axes);
                return true;
            }
            child = (ViewParent) parent;
            parent = parent.getParent();
        }
        return false;
    }

    public boolean hasNestedScrollingParent() {
        return mNestedScrollingParent != null;
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed) {
        if (mNestedScrollingParent == null) {
            return false;
        }
        if (consumed == null) {
            consumed = new int[2];
        }
        mNestedScrollingParent.onNestedPreScroll(view, dx, dy, consumed);
        return consumed[0] != 0 && consumed[1] != 0;    // 证明parentView消费了滑动距离
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] consumed) {
        if (mNestedScrollingParent == null) {
            return false;
        }
        mNestedScrollingParent.onNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        return true;
    }
}
