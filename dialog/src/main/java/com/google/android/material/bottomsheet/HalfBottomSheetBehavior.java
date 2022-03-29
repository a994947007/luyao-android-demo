package com.google.android.material.bottomsheet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class HalfBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    private boolean mEnableHalfDrag = false;

    public HalfBottomSheetBehavior() {
        super();
    }

    public HalfBottomSheetBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableHalfDrag(boolean enableHalfDrag) {
        this.mEnableHalfDrag = enableHalfDrag;
    }

    private float mInitY;

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!mEnableHalfDrag && isScrollTop(event) && getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            return false;
        }
        return super.onInterceptTouchEvent(parent, child, event);
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if (!mEnableHalfDrag && isScrollTop(event) && getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            return false;
        }
        return super.onTouchEvent(parent, child, event);
    }

    private boolean isScrollTop(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mInitY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getY() - mInitY < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0 && !mEnableHalfDrag && getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            consumed[1] = 0;
            return;
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (dyUnconsumed > 0 && !mEnableHalfDrag && getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            return;
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
        if (!mEnableHalfDrag && getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            return false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
