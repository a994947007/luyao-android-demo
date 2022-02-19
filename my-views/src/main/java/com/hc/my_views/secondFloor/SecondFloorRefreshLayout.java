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
    private View mFirstFrameLayout;
    private int mTranslateY = 0;
    private boolean pointerUp = false;
    private boolean isOpen = false;
    private final List<OnSlideListener> mListeners = new ArrayList<>();
    private final List<OnSecondFloorListener> mOnSecondFloorListeners = new ArrayList<>();
    private ValueAnimator openSlideAnimator;
    private ValueAnimator closeSlideAnimator;
    private float mInitY;
    private int mScrollPointerId;   // 用于记录最后一个触摸点的id

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
        mFirstFrameLayout = (View) getChildAt(1);
        initSlideAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
                public void onAnimationEnd(Animator animation) {
                    updateState();
                }
            });
        }
    }

    private void updateState() {
        if (mTranslateY >= mFirstFrameLayout.getMeasuredHeight()) {
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
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            pointerUp = true;
            if (mTranslateY > AUTO_OPEN_OFFSET_THRESHOLD) {
                openSlideAnimator.setIntValues(mTranslateY, getMeasuredHeight());
                openSlideAnimator.start();
            } else {
                closeSlideAnimator.setIntValues(mTranslateY, 0);
                closeSlideAnimator.start();
            }
            updateState();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOpen) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOpen) {
            return false;
        }
        int action = event.getActionMasked();
        final int actionIndex = event.getActionIndex();
        if (action == MotionEvent.ACTION_DOWN) {
            mScrollPointerId = event.getPointerId(0);
            mInitY = event.getY();
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            mScrollPointerId = event.getPointerId(actionIndex);
            mInitY = event.getY(actionIndex);
        } else if (action == MotionEvent.ACTION_MOVE) {
            // move中只依据最后一根按下的手指
            int pointerIndex = event.findPointerIndex(mScrollPointerId);
            float y = event.getY(pointerIndex);
            float dy = mInitY - y;
            if (dy <= 0) {
                // 剩余的滑动距离
                if (mTranslateY - dy > mFirstFrameLayout.getMeasuredHeight()) {
                    // 向下滑最多不超过second的高度
                    mTranslateY = mFirstFrameLayout.getMeasuredHeight();
                } else {
                    mTranslateY -= dy;
                }
                processTranslateY(mTranslateY);
            } else {
                if (dy > 0 && mTranslateY > 0) {
                    // 向上滑并且是展开或者半展开态
                    if (mTranslateY - dy <= 0) {
                        mTranslateY = 0;
                        processTranslateY(mTranslateY);
                    } else {
                        mTranslateY -= dy;
                        processTranslateY(mTranslateY);
                    }
                }
            }
            mInitY = y;
        }
        return true;
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
    public void onStopNestedScroll(@NonNull View target, int type) {
        updateState();
    }

    private void processTranslateY(int translateY) {
        if (translateY > mFirstFrameLayout.getMeasuredHeight()) {
            translateY = mFirstFrameLayout.getMeasuredHeight();
        }
        for (OnSlideListener mListener : mListeners) {
            mListener.onSlide(translateY, (float) translateY / mFirstFrameLayout.getMeasuredHeight());
        }
        mTranslateY = translateY;
        mFirstFrameLayout.setTranslationY(translateY);
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
            if (mTranslateY - dyUnconsumed > mFirstFrameLayout.getMeasuredHeight()) {
                // 向下滑最多不超过second的高度
                mTranslateY = mFirstFrameLayout.getMeasuredHeight();
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
