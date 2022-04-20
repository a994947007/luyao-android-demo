package com.hc.my_views.bottomsheet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.util.Pools;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.hc.my_views.R;
import com.hc.my_views.ViewGroupUtils;

/**
 * 可折叠BottomSheetView，解决了BottomSheetBehavior收起面板，无法滑动RecyclerView/ScrollView，以及遮挡底部内容和状态不可控等问题
 * 该View可以和Dialog一起使用，用于实现BottomSheetDialog效果
 * 在xml中设置maxHeight，即为设置该View的完全展开时高度，调用halfToFull()即可完全展开，调用fullToHalf()即可折叠
 * 使用方式：参考关注页二楼添加特关面板，FavoriteFollowDialogFragment、FavoriteFollowPanelPeekPresenter
 */

public class HalfBottomSheetView extends FrameLayout implements NestedScrollingParent2 {

    public static final int STATE_INIT = 0;
    public static final int STATE_HIDDEN = 1;
    public static final int STATE_HALF_EXPENDED = 2;
    public static final int STATE_FULL_EXPENDED = 3;
    public static final int STATE_SETTLING = 4;
    public static final int STATE_DRAGGING = 5;

    private int mCurrentState = STATE_INIT;
    private boolean isPointerUp = true;
    public boolean isFullExpanded = false;
    private boolean isToHalf = false;

    private @Nullable WeakReference<View> nestedScrollingChildRef;
    private ViewDragHelper viewDragHelper;
    private ValueAnimator halfToFullAnimator;
    private ValueAnimator fullToHalfAnimator;
    private int initHeight;
    private int mMaxHeight;
    // View偏移范围
    private int limitBottom;
    private int limitTop;
    // 记录目标偏移值
    private int mTargetOffsetY;
    private boolean isIntercept;

    private boolean mDirectHidden = false;  // 下拉是否直接关闭
    private boolean mIsQuickPullHidden = true; // 是否快速支持快速下滑关闭面板
    private int mQuickPullYvel = 2000; // 快速滑动关闭面板阈值
    private int mToHalfOffsetLimit;  // 面板折叠阈值

    private final List<OnStateChangeListener> mOnStateChangeListeners = new ArrayList<>();
    private final List<OnOffsetChangeListener> mOnOffsetChangeListeners = new ArrayList<>();
    private int mParentInitHeight;

    public HalfBottomSheetView(@NonNull Context context) {
        this(context, null);
    }

    public HalfBottomSheetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfBottomSheetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HalfBottomSheetView);
        mMaxHeight = (int) typedArray.getDimension(R.styleable.HalfBottomSheetView_android_maxHeight, 0);
        typedArray.recycle();
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    public void addOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        if (mOnStateChangeListeners.contains(onStateChangeListener)) {
            return;
        }
        mOnStateChangeListeners.add(onStateChangeListener);
    }

    public void addOnOffsetChangeListener(OnOffsetChangeListener offsetChangeListener) {
        if (mOnOffsetChangeListeners.contains(offsetChangeListener)) {
            return;
        }
        mOnOffsetChangeListeners.add(offsetChangeListener);
    }

    /**
     * 下拉直接关闭面板，不进入折叠态，默认进入折叠态
     */
    public void setDirectHidden(boolean directHidden) {
        mDirectHidden = directHidden;
    }

    /**
     * 快速下滑滑动关闭/折叠面板
     */
    public void setQuickPullHidden(boolean quickPullHidden) {
        mIsQuickPullHidden = quickPullHidden;
    }

    public void setQuickPullYvel(int quickPullYvel) {
        mQuickPullYvel = quickPullYvel;
    }

    public void setToHalfOffsetLimit(int toHalfOffsetLimit) {
        mToHalfOffsetLimit = toHalfOffsetLimit;
    }

    private void init() {
        initHeight = getMeasuredHeight();
        mParentInitHeight = getParentContainerHeight();
        if (mToHalfOffsetLimit == 0) {
            mToHalfOffsetLimit = (int) (initHeight * 0.2);
        }
        if (mMaxHeight < initHeight) {
            mMaxHeight = initHeight;
        }
        int limitOffset = mMaxHeight - initHeight;

        halfToFullAnimator = new ValueAnimator();
        halfToFullAnimator.setDuration(300);
        halfToFullAnimator.setFloatValues(0, 1);
        halfToFullAnimator.addUpdateListener(animation -> {
            getLayoutParams().height = (int) (initHeight + limitOffset * (float)animation.getAnimatedValue());
            requestLayout();
        });
        halfToFullAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isToHalf = false;
                dispatchState(STATE_SETTLING);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setFullExpanded(true);
                dispatchState(STATE_FULL_EXPENDED);
            }
        });

        fullToHalfAnimator = new ValueAnimator();
        fullToHalfAnimator.setDuration(300);
        fullToHalfAnimator.setFloatValues(1, 0);
        fullToHalfAnimator.addUpdateListener(animation -> {
            getLayoutParams().height = (int) (initHeight + limitOffset * (float)animation.getAnimatedValue());
            requestLayout();
        });
        fullToHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                dispatchState(STATE_SETTLING);
                isToHalf = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setFullExpanded(false);
                dispatchState(STATE_HALF_EXPENDED);
            }
        });
        ensureLimitOffset();
        nestedScrollingChildRef = new WeakReference<>(findScrollingChild(this));
    }

    private final ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return MathUtils.clamp(top, 0, getMeasuredHeight());
        }

        @Override
        public void onViewPositionChanged(
                @NonNull View changedView, int left, int top, int dx, int dy) {
            if (isIntercept) {
                dispatchOnSlide(top);
            } else {
                dispatchOnSlide(top - (mParentInitHeight - getMeasuredHeight()));
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top = releasedChild.getTop(); // 滑动距离
            // settleCapturedViewAt()第2个参数是View基于当前位置向下偏移距离
            isToHalf = false;
            if (top > getMeasuredHeight() * 0.5 || (yvel > mQuickPullYvel && mIsQuickPullHidden)) {
                // 收起
                mTargetOffsetY = mParentInitHeight; // 记录目标偏移值
                viewDragHelper.settleCapturedViewAt(getLeft(), getMeasuredHeight());
            } else  {
                if (isFullExpanded && top > mToHalfOffsetLimit) {
                    // 半展开
                    isToHalf = true;
                    mTargetOffsetY = mParentInitHeight - initHeight;   // 记录目标偏移值
                    viewDragHelper.settleCapturedViewAt(getLeft(), mMaxHeight - initHeight);
                } else {
                    // 回弹
                    mTargetOffsetY = mParentInitHeight - getMeasuredHeight();
                    viewDragHelper.settleCapturedViewAt(getLeft(), 0);
                }
            }
            dispatchState(STATE_SETTLING);
            ViewCompat.postOnAnimation(HalfBottomSheetView.this, new SettleRunnable());
        }
    };

    private void dispatchState(int state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        for (OnStateChangeListener onStateChangeListener : mOnStateChangeListeners) {
            onStateChangeListener.onStateChanged(mCurrentState);
        }
    }

    private void dispatchOnSlide(int offset) {
        for (OnOffsetChangeListener onOffsetChangeListener : mOnOffsetChangeListeners) {
            onOffsetChangeListener.onOffsetChanged(offset);
        }
    }

    public void halfToFull() {
        if (mCurrentState == STATE_FULL_EXPENDED
                || halfToFullAnimator.isRunning()
                || fullToHalfAnimator.isRunning()
                || viewDragHelper == null) {
            return;
        }
        halfToFullAnimator.start();
    }

    public void fullToHalf() {
        if (mCurrentState == STATE_HALF_EXPENDED
                || halfToFullAnimator.isRunning()
                || fullToHalfAnimator.isRunning()
                || viewDragHelper == null) {
            return;
        }
        fullToHalfAnimator.start();
    }

    public void hide() {
        if (mCurrentState == STATE_HIDDEN) {
            return;
        }
        smoothSlideViewTo(getLeft(), getMeasuredHeight());
        ViewCompat.postOnAnimation(HalfBottomSheetView.this, new SettleRunnable());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isShown()) {
            return false;
        }
        int initX = (int) ev.getX();
        int initY = (int) ev.getY();
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            isIntercept = false;
        }
        View scroll = nestedScrollingChildRef != null ? nestedScrollingChildRef.get() : null;
        if (scroll != null && isPointInChildBounds(scroll, initX, initY)) {
            return false;
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (isPointInScrollableChildrenBounds(initX, initY)) {
                return false;
            }
        } else {
            if (!isPointInScrollableChildrenBounds(initX, initY) && viewDragHelper != null) {
                viewDragHelper.processTouchEvent(ev);
            }
            if (isPointInChildrenBounds(initX, initY) || !viewDragHelper.shouldInterceptTouchEvent(ev)) {
                return false;
            }
        }
        isIntercept = true;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isShown()) {
            return false;
        }
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(ev);
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isShown()) {
            return false;
        }
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            isPointerUp = false;
        } else if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            dispatchState(STATE_DRAGGING);
        } else if (ev.getActionMasked() == MotionEvent.ACTION_UP
                || ev.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            isPointerUp = true;
        }
        boolean result = super.dispatchTouchEvent(ev);
        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            startAnimator();
        }
        return result;
    }

    private int getParentContainerHeight() {
        return ((View)getParent()).getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (viewDragHelper == null) {
            viewDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
            init();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) { }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) { }

    private void startAnimator() {
        // smoothSlideViewTo()第2个参数是View距离parent顶部的目标距离
        if (mCurrentState == STATE_SETTLING || getOffsetY() == 0) {
            return;
        }
        isToHalf = false;
        if (getOffsetY() > getMeasuredHeight() * 0.5) {
            smoothSlideViewTo(getLeft(), mParentInitHeight);
        } else {
            if (isFullExpanded && getOffsetY() > mToHalfOffsetLimit) {
                isToHalf = true;
                smoothSlideViewTo(getLeft(), limitBottom);
            } else {
                smoothSlideViewTo(getLeft(), limitTop);
            }
        }
        dispatchState(STATE_SETTLING);
        ViewCompat.postOnAnimation(this ,new SettleRunnable());
    }

    private void smoothSlideViewTo(int left, int offsetY) {
        mTargetOffsetY = offsetY;
        viewDragHelper.smoothSlideViewTo(this, left, offsetY);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) { }

    private class SettleRunnable implements Runnable {
        @Override
        public void run() {
            if (isPointerUp  && !fullToHalfAnimator.isStarted() && !halfToFullAnimator.isStarted()
                    && viewDragHelper != null && viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(HalfBottomSheetView.this, this);
            } else {
                if (isFullExpanded && isToHalf && !fullToHalfAnimator.isStarted() && !halfToFullAnimator.isStarted()) {
                    getLayoutParams().height = initHeight;
                    requestLayout();
                    setFullExpanded(false);
                }
                if (mTargetOffsetY == mParentInitHeight) {
                    dispatchState(STATE_HIDDEN);
                } else if (mTargetOffsetY == mParentInitHeight - initHeight) {
                    if (initHeight == mMaxHeight) {
                        dispatchState(STATE_FULL_EXPENDED);
                    } else {
                        dispatchState(STATE_HALF_EXPENDED);
                    }
                } else if (mTargetOffsetY == mParentInitHeight - mMaxHeight) {
                    dispatchState(STATE_FULL_EXPENDED);
                }
            }
        }
    }

    private void setFullExpanded(boolean fullExpanded) {
        this.isFullExpanded = fullExpanded;
        ensureLimitOffset();
    }

    private void ensureLimitOffset() {
        if (isFullExpanded) {
            limitTop = mParentInitHeight - mMaxHeight;
            limitBottom = mParentInitHeight - initHeight;
        } else {
            limitTop = mParentInitHeight - initHeight;
            limitBottom = mParentInitHeight;
        }
        if (mDirectHidden) {
            limitBottom = mParentInitHeight;
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
            dispatchOnSlide(getOffsetY());
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
            dispatchOnSlide(getOffsetY());
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (velocityY < mQuickPullYvel * -1 && mIsQuickPullHidden && !target.canScrollVertically(-1) && mCurrentState != STATE_SETTLING) {
            dispatchState(STATE_SETTLING);
            isToHalf = true;
            smoothSlideViewTo(getLeft(), limitBottom);
            ViewCompat.postOnAnimation(this, new SettleRunnable());
        }
        return getOffsetY() != 0;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    private int getOffsetY() {
        return getTop() - mParentInitHeight + getMeasuredHeight();
    }

    private static final Pools.Pool<Rect> sRectPool = new Pools.SynchronizedPool<>(12);

    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0, count = group.getChildCount(); i < count; i++) {
                View scrollingChild = findScrollingChild(group.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }

    public boolean isPointInChildrenBounds(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            if (isPointInChildBounds(getChildAt(i), x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPointInScrollableChildBounds(View view, int x, int y) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                boolean flag = isPointInScrollableChildBounds(child, x, y);
                if (flag) {
                    return true;
                }
            }
        }
        return isPointInChildBounds(view, x, y) && (view.canScrollVertically(1) || view.canScrollVertically(-1));
    }

    public boolean isPointInScrollableChildrenBounds(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (isPointInScrollableChildBounds(child, x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPointInChildBounds(@NonNull View child, int x, int y) {
        final Rect r = acquireTempRect();
        getDescendantRect(child, r);
        try {
            return r.contains(x, y);
        } finally {
            releaseTempRect(r);
        }
    }

    private static void releaseTempRect(@NonNull Rect rect) {
        rect.setEmpty();
        sRectPool.release(rect);
    }

    @NonNull
    private static Rect acquireTempRect() {
        Rect rect = sRectPool.acquire();
        if (rect == null) {
            rect = new Rect();
        }
        return rect;
    }

    void getDescendantRect(View descendant, Rect out) {
        ViewGroupUtils.getDescendantRect(this, descendant, out);
    }

    public interface OnStateChangeListener {
        void onStateChanged(int state);
    }

    public interface OnOffsetChangeListener {
        void onOffsetChanged(int state);
    }
}
