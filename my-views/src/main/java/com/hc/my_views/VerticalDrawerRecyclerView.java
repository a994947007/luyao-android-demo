package com.hc.my_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.util.ViewUtils;

/**
 * 支持窗垂直拖动的抽屉View，只能装两个元素，第二个元素可以被拖动，第一个元素不能拖动
 */
public class VerticalDrawerRecyclerView extends FrameLayout {
    private ViewDragHelper mDragHelper;
    private View recyclerView;
    private int mMenuHeight;
    public VerticalDrawerRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDrawerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDrawerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, mDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("VerticalDrawerRecyclerView 必须有两个child");
        }
        recyclerView = getChildAt(childCount - 1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mMenuHeight = getChildAt(0).getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMenuHeight == recyclerView.getTop()) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownY = ev.getY();
            mDragHelper.processTouchEvent(ev);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float move = ev.getY();
            if (move - mDownY > 0 && !recyclerView.canScrollVertically(-1)) { // 能向上滑，并且是向上滑动时，就拦截
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    private final ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            // 让所有孩子都可以拖动
            return recyclerView == child;
        }

        // 重写这个方法支持垂直拖动，拖动之后或调用这个方法，这个返回值会被作为view的位置
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            }
            if (top > mMenuHeight) {
                top = mMenuHeight;
            }
            return top;
        }

        // 滚动到一半能够自动收起
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top = recyclerView.getTop();
            if (top > mMenuHeight / 2) {
                mDragHelper.settleCapturedViewAt(0, mMenuHeight);
            } else {
                mDragHelper.settleCapturedViewAt(0, 0);
            }
            invalidate();
        }


    };

    // 计算完滚动，让其重绘
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
