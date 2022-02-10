package com.hc.my_views.secondFloor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.customview.widget.ViewDragHelper;

public class SecondFloorBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    public SecondFloorBehavior() { }

    public SecondFloorBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        Log.d("SecondFloorBehavior", "constructor");
    }

    private ViewDragHelper mViewDragHelper;
    private float mDownX;
    private float mDownY;

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
        if (mViewDragHelper == null) {
            mViewDragHelper = ViewDragHelper.create(parent, dragCallback);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (mViewDragHelper != null) {
            mViewDragHelper.processTouchEvent(ev);
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = ev.getX();
            mDownY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE){
            if (ev.getY() - mDownY > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return false;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    };
}
