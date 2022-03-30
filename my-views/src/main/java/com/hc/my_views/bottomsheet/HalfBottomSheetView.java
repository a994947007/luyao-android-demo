package com.hc.my_views.bottomsheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Printer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.hc.util.ViewUtils;

public class HalfBottomSheetView extends FrameLayout implements NestedScrollingParent2 {

    private ViewDragHelper viewDragHelper;

    private static final int STATE_DRAGGING = 1;
    private static final int STATE_SETTLING = 2;
    private int currentState;
    private boolean isPointerUp = true;

    private ValueAnimator halfToFullAnimator;
    private ValueAnimator fullToHalfAnimator;
    private int initHeight;

    public boolean isFullExpended = false;
    private boolean isToHalf = false;

    public HalfBottomSheetView(@NonNull Context context) {
        this(context, null);
    }

    public HalfBottomSheetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfBottomSheetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        initHeight = getMeasuredHeight();
        halfToFullAnimator = new ValueAnimator();
        halfToFullAnimator.setDuration(300);
        halfToFullAnimator.setFloatValues(0, 1);
        halfToFullAnimator.addUpdateListener(animation -> {
            getLayoutParams().height = (int) (initHeight + ViewUtils.dp2px(300) * (float)animation.getAnimatedValue());
            requestLayout();
        });
        halfToFullAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentState = STATE_SETTLING;
                isToHalf = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFullExpended = true;
            }
        });

        fullToHalfAnimator = new ValueAnimator();
        fullToHalfAnimator.setDuration(300);
        fullToHalfAnimator.setFloatValues(1, 0);
        fullToHalfAnimator.addUpdateListener(animation -> {
            getLayoutParams().height = (int) (initHeight + ViewUtils.dp2px(300) * (float)animation.getAnimatedValue());
            requestLayout();
        });
        fullToHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentState = STATE_SETTLING;
                isToHalf = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFullExpended = false;
            }
        });
    }

    public void halfToFull() {
        halfToFullAnimator.start();
    }

    public void fullToHalf() {
        fullToHalfAnimator.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(this, new Callback());
            init();
        }
    }

    private class Callback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return MathUtils.clamp(top, 0, getMeasuredHeight());
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) { }

    private void startAnimator() {
        if (currentState == STATE_SETTLING) {
            return;
        }
        isToHalf = false;
        if (getOffsetY() > getMeasuredHeight() * 0.7) {
            viewDragHelper.smoothSlideViewTo(this, getLeft(), ((View)getParent()).getMeasuredHeight());
        } else {
            if (isFullExpended && getOffsetY() > getMeasuredHeight() * 0.2) {
                isToHalf = true;
                viewDragHelper.smoothSlideViewTo(this, getLeft(), ((View)getParent()).getMeasuredHeight() - initHeight);
            } else {
                viewDragHelper.smoothSlideViewTo(this, getLeft(), ((View)getParent()).getMeasuredHeight() - getMeasuredHeight());
            }
        }
        currentState = STATE_SETTLING;
        ViewCompat.postOnAnimation(this ,new AnimationRunnable());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            isPointerUp = false;
            currentState = STATE_DRAGGING;
        } else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            isPointerUp = true;
        }
        boolean result = super.dispatchTouchEvent(ev);
        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            startAnimator();
        }
        return result;
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) { }

    private class AnimationRunnable implements Runnable {
        @Override
        public void run() {
            if (isPointerUp && !fullToHalfAnimator.isStarted() && !halfToFullAnimator.isStarted() && viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(HalfBottomSheetView.this, this);
            } else {
                if (isFullExpended && isToHalf && !fullToHalfAnimator.isStarted() && !halfToFullAnimator.isStarted()) {
                    getLayoutParams().height = initHeight;
                    requestLayout();
                    isFullExpended = false;
                }
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (isPointerUp) {
            return;
        }
        if (dy < 0 && !target.canScrollVertically(-1)) {
            int limitOffset = getMeasuredHeight() - getOffsetY();
            // 下滑动，并且子View不能再下滑动
            if (limitOffset < -dy) {
                ViewCompat.offsetTopAndBottom(this, limitOffset);
                consumed[1] = -limitOffset;
            } else {
                ViewCompat.offsetTopAndBottom(this, -dy);
                consumed[1] = dy;
            }
        } else if (dy > 0 && getOffsetY() > 0) {
            // 下滑动，并且子View不能再下滑动
            int limitOffset = getOffsetY();
            if (dy > getOffsetY()) {
                ViewCompat.offsetTopAndBottom(this, -limitOffset);
                consumed[1] = limitOffset;
            } else {
                ViewCompat.offsetTopAndBottom(this, -dy);
                consumed[1] =  dy;
            }
        }
    }

    private int getOffsetY() {
        return getTop() - ((ViewGroup)getParent()).getMeasuredHeight() + getMeasuredHeight();
    }
}
