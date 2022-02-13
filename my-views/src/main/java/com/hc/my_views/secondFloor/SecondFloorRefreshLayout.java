package com.hc.my_views.secondFloor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.my_views.R;

public class SecondFloorRefreshLayout extends FrameLayout implements NestedScrollingParent2 {
    private FrameLayout mSecondFrameLayout;
    private FrameLayout mFirstFrameLayout;
    private int mTranslateY = 0;
    private RecyclerView mRecyclerView;
    private View mShadowView;
    private View mSecondContainer;
    private boolean pointerUp = false;
    private boolean isOpen = false;

    public SecondFloorRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public SecondFloorRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondFloorRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSecondFrameLayout = (FrameLayout) getChildAt(0);
        mFirstFrameLayout = (FrameLayout) getChildAt(1);
        mShadowView = mSecondFrameLayout.getChildAt(1);
        mSecondContainer = mSecondFrameLayout.getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSecondContainer.setPivotY(0);
        mSecondContainer.setPivotX(mSecondFrameLayout.getMeasuredWidth() / 2f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            pointerUp = false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            pointerUp = true;
            if (mTranslateY > 500) {
                ValueAnimator animator = new ValueAnimator();
                animator.setDuration(200);
                animator.setIntValues(mTranslateY, getMeasuredHeight());
                animator.addUpdateListener(animation -> {
                    mTranslateY = (int) animation.getAnimatedValue();
                    processTranslateY(mTranslateY);
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mShadowView.setVisibility(View.GONE);
                    }
                });
                animator.start();
            } else {
                ValueAnimator animator = new ValueAnimator();
                animator.setDuration(200);
                animator.setIntValues(mTranslateY, 0);
                animator.addUpdateListener(animation -> {
                    mTranslateY = (int) animation.getAnimatedValue();
                    processTranslateY(mTranslateY);
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mShadowView.setVisibility(View.VISIBLE);
                    }
                });
                animator.start();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mRecyclerView = (RecyclerView) target;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) { }

    private void processTranslateY(int translateY) {
        if (translateY > mSecondFrameLayout.getMeasuredHeight()) {
            translateY = mSecondFrameLayout.getMeasuredHeight();
        }
        mFirstFrameLayout.setTranslationY(translateY);
        if (translateY <= 1000f) {
            float ratio = (1000f - translateY) / 1000f;
            if (translateY > 500f) {
                mShadowView.setAlpha((1 - (translateY - 500f) / 500f));
            }
            if (1 - ratio < 0.618) {
                ratio = 0.382f;
            }
            mSecondContainer.setScaleX(1 - ratio);
            mSecondContainer.setScaleY(1 - ratio);
        } else {
            mShadowView.setAlpha(0);
            mSecondContainer.setScaleX(1);
            mSecondContainer.setScaleY(1);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0 && !pointerUp) {
            // 剩余的滑动距离
            if (mTranslateY - dyUnconsumed > mSecondFrameLayout.getMeasuredHeight()) {
                // 向下滑最多不超过second的高度
                mTranslateY = mSecondFrameLayout.getMeasuredHeight();
            } else {
                mTranslateY -= dyUnconsumed;
            }
            processTranslateY(mTranslateY);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0 && mTranslateY > 0) {
            // 向上滑并且是展开或者半展开态
            if (mTranslateY - dy <= 0) {
                consumed[1] = -mTranslateY;
                mTranslateY = 0;
                processTranslateY(mTranslateY);
            } else {
                mTranslateY -= dy;
                processTranslateY(mTranslateY);
                consumed[1] = dy;
            }
        }
        // 向下滑需要先让子View先滑
    }
}
