package com.hc.my_views.secondFloor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;

import java.util.ArrayList;
import java.util.List;

public class SecondFloorRefreshLayout extends FrameLayout implements NestedScrollingParent2 {
    private FrameLayout mSecondFrameLayout;
    private FrameLayout mFirstFrameLayout;
    private int mTranslateY = 0;
    private View mShadowView;
    private View mSecondContainer;
    private boolean pointerUp = false;
    private boolean isOpen = false;
    private final List<OnSlideListener> mListeners = new ArrayList<>();
    private final List<OnSecondFloorListener> mOnSecondFloorListeners = new ArrayList<>();
    private ValueAnimator openSlideAnimator;
    private ValueAnimator closeSlideAnimator;

    public interface OnSlideListener {
        void onSlide(float offset, float offsetPercent);
    }

    public interface OnSecondFloorListener {
        void onStateChange(boolean isOpen);
    }

    public void addOnSlideListener(OnSlideListener onSlideListener) {
        mListeners.add(onSlideListener);
    }

    public void removeOnSlideListener(OnSlideListener onSlideListener) {
        mListeners.remove(onSlideListener);
    }

    public void addOnSecondFloorListener(OnSecondFloorListener onSecondFloorListener) {
        mOnSecondFloorListeners.add(onSecondFloorListener);
    }

    public void removeOnSecondFloorListener(OnSecondFloorListener onSecondFloorListener) {
        mOnSecondFloorListeners.remove(onSecondFloorListener);
    }

    private static final float AUTO_OPEN_OFFSET_THRESHOLD = 500f;

    public SecondFloorRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public SecondFloorRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondFloorRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void close() {
        if (mTranslateY == 0) {
            return;
        }
        closeSlideAnimator.setIntValues(mTranslateY, 0);
        closeSlideAnimator.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSecondFrameLayout = (FrameLayout) getChildAt(0);
        mFirstFrameLayout = (FrameLayout) getChildAt(1);
        mShadowView = mSecondFrameLayout.getChildAt(1);
        mSecondContainer = mSecondFrameLayout.getChildAt(0);
        mSecondFrameLayout.setEnabled(false);
        initSlideAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSecondContainer.setPivotY(0);
        mSecondContainer.setPivotX(mSecondFrameLayout.getMeasuredWidth() / 2f);
    }

    private void initSlideAnimator() {
        if (openSlideAnimator == null) {
            openSlideAnimator = new ValueAnimator();
            openSlideAnimator.setDuration(200);
            openSlideAnimator.addUpdateListener(animation -> {
                mTranslateY = (int) animation.getAnimatedValue();
                processTranslateY(mTranslateY);
            });
            openSlideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mShadowView.setVisibility(View.GONE);
                    updateState();
                }
            });
        }
        if (closeSlideAnimator == null) {
            closeSlideAnimator = new ValueAnimator();
            closeSlideAnimator.setDuration(200);
            closeSlideAnimator.addUpdateListener(animation -> {
                mTranslateY = (int) animation.getAnimatedValue();
                processTranslateY(mTranslateY);
            });
            closeSlideAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mShadowView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void updateState() {
        if (mTranslateY >= mSecondFrameLayout.getMeasuredHeight()) {
            if (!isOpen) {
                isOpen = true;
                dispatchSecondFloorState(true);
            }
        } else {
            if (isOpen) {
                isOpen = false;
                dispatchSecondFloorState(false);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            pointerUp = false;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            pointerUp = true;
            if (mTranslateY > AUTO_OPEN_OFFSET_THRESHOLD) {
                openSlideAnimator.setIntValues(mTranslateY, getMeasuredHeight());
                openSlideAnimator.start();
            } else {
                closeSlideAnimator.setIntValues(mTranslateY, 0);
                closeSlideAnimator.start();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return child == mFirstFrameLayout;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) { }

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
        for (OnSlideListener mListener : mListeners) {
            mListener.onSlide(translateY, (float) translateY / mSecondFrameLayout.getMeasuredHeight());
        }
        mTranslateY = translateY;
        mFirstFrameLayout.setTranslationY(translateY);
        updateState();
        if (translateY <= 1000f) {
            float ratio = (1000f - translateY) / 1000f;
            if (translateY > AUTO_OPEN_OFFSET_THRESHOLD) {
                mShadowView.setAlpha((1 - (translateY - AUTO_OPEN_OFFSET_THRESHOLD) / AUTO_OPEN_OFFSET_THRESHOLD));
            } else {
                mShadowView.setAlpha(1);
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

    protected void dispatchSecondFloorState(boolean isOpen) {
        for (OnSecondFloorListener onSecondFloorListener : mOnSecondFloorListeners) {
            onSecondFloorListener.onStateChange(isOpen);
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
