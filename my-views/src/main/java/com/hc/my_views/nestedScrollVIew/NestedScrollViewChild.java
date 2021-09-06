package com.hc.my_views.nestedScrollVIew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.ViewCompat;

public class NestedScrollViewChild extends FrameLayout implements NestedScrollingChild {
    private float mLastX;
    private float mLastY;
    public NestedScrollViewChild(Context context) {
        super(context);
    }

    public NestedScrollViewChild(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) (mLastX - event.getRawX());
            int dy = (int) (mLastY - event.getRawY());
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        }
        return super.onTouchEvent(event);
    }
}
