package com.hc.my_views.secondFloor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;

public class SecondFloorContainerView extends FrameLayout implements NestedScrollingParent2 {

    private View mChildView;
    private float mTranslateY = 0;
    private ValueAnimator animator;
    private boolean isPointerUp;
    private SecondFloorRefreshLayout refreshLayout;

    public SecondFloorContainerView(@NonNull Context context) {
        super(context);
    }

    public SecondFloorContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondFloorContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (animator == null) {
            animator = new ValueAnimator();
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translateY = (float) animation.getAnimatedValue();
                    processTranslateY(translateY);
                }
            });
        }
    }

    private void processTranslateY(float translateY) {
        mTranslateY = translateY;
        mChildView.setTranslationY(translateY);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mChildView = child;
        findSecondFloorRefreshLayout((ViewGroup) getRootView());
    }

    private void findSecondFloorRefreshLayout(ViewGroup parent) {
        int childCount = parent.getChildCount();
        if (refreshLayout != null) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof SecondFloorRefreshLayout) {
                refreshLayout = (SecondFloorRefreshLayout) child;
                return;
            } else {
                if (child instanceof ViewGroup) {
                    findSecondFloorRefreshLayout((ViewGroup) child);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isPointerUp = false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            isPointerUp = true;
            if (mTranslateY <= -300) {
                refreshLayout.close();
            }
            animator.setFloatValues(mTranslateY, 0);
            animator.start();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (isPointerUp) {
            return;
        }
        if (dyUnconsumed > 0) {
            float translate = (float) (mTranslateY - dyUnconsumed * 0.5);
            processTranslateY(translate);
        } else if (dyUnconsumed < 0 && mTranslateY <= 0) {
            if (mTranslateY - dyUnconsumed * 0.5 > 0) {
                processTranslateY(0);
            } else {
                float translate = (float) (mTranslateY - dyUnconsumed * 0.5);
                processTranslateY(translate);
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

    }
}
