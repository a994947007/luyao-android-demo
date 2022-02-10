package com.hc.my_views.secondFloor;

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
    private int childConsumedY = 0;
    private View mShadowView;

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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mTranslateY > 500) {
                ValueAnimator animator = new ValueAnimator();
                animator.setDuration(500);
                animator.setIntValues(mTranslateY, getMeasuredHeight());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mTranslateY = (int) animation.getAnimatedValue();
                        mFirstFrameLayout.setTranslationY(mTranslateY);
                    }
                });
                animator.start();
            } else {
                ValueAnimator animator = new ValueAnimator();
                animator.setDuration(500);
                animator.setIntValues(mTranslateY, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mTranslateY = (int) animation.getAnimatedValue();
                        mFirstFrameLayout.setTranslationY(mTranslateY);
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
    public void onStopNestedScroll(@NonNull View target, int type) {

    }

    private void processTranslateY(int translateY) {
        mFirstFrameLayout.setTranslationY(translateY);
        if (translateY <= 500f) {
            mShadowView.setAlpha((500 - translateY) / 500f);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        childConsumedY += dyConsumed;
        if (dyUnconsumed < 0) {
            mTranslateY -= dyUnconsumed;
            processTranslateY(mTranslateY);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0) {
            if (mTranslateY > 0) {
                if (mTranslateY - dy <= 0) {
                    consumed[1] = -mTranslateY;
                    mTranslateY = 0;
                    processTranslateY(mTranslateY);
                    childConsumedY += consumed[1];
                } else {
                    mTranslateY -= dy;
                    processTranslateY(mTranslateY);
                    consumed[1] = dy;
                    childConsumedY += consumed[1];
                }
            }
        } else {
            if (childConsumedY + dy > 0) {
                childConsumedY += dy;
            } else {
                childConsumedY = 0;
            }
        }
    }
}
