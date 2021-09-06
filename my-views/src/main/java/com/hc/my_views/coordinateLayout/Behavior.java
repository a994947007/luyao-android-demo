package com.hc.my_views.coordinateLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 用于执行对应View的事件
 */
public class Behavior {

    public Behavior(Context context, AttributeSet attrs) {}

    public void onTouchEvent(View parent, View child, MotionEvent ev) { }

    public void onSizeChanged(View parent, View child, int w, int h, int oldW ,int oldH) { }

    public void onNestedPreScroll(View parent, View child, View target, int dx, int dy, int[] consumed) { }

    public void onNestedScroll(View target, View child, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) { }

    public void onFinishInflate(View parent, View child) { }
}
