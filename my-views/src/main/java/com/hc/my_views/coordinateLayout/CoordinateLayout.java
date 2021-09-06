package com.hc.my_views.coordinateLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.NestedScrollingParent;

import com.hc.my_views.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * CoordinateLayout的主要思想是绑定Behavior给对应的子View的LayoutParams
 * 在事件分发时，将事件分发给所有子View对应的Behavior，子View可以选择处理该事件，也可以不处理
 */
public class CoordinateLayout extends ConstraintLayout implements NestedScrollingParent, ViewTreeObserver.OnGlobalLayoutListener {
    public CoordinateLayout(Context context) {
        super(context);
    }

    public CoordinateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 在子View被添加到parent的时候，会调用该方法。也就意味着，parent可以在这个位置获取到子View上对应的Behavior
     * @param attrs 子View的attrs
     * @return coordinateLayout的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取所有的子View，执行子View绑定的Behavior的对应的方法
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.behavior != null) {
                lp.behavior.onTouchEvent(this, child, event);
            }
        }
        return false;
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.behavior != null) {
                lp.behavior.onFinishInflate(this, child);
            }
        }
    }

    /**
     * NestedScrolling机制，这里必须为true
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    // 事件直接分发给子View，parentView不做处理
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.behavior != null) {
                lp.behavior.onSizeChanged(this, child, w, h, oldw, oldh);
            }
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.behavior != null) {
                lp.behavior.onNestedPreScroll(this, child, target, dx, dy, consumed);
            }
        }
    }

    /**
     *
     * @param target 滑动的parent view
     * @param dxConsumed 已经滑掉的x距离
     * @param dyConsumed 已经滑掉的y距离
     * @param dxUnconsumed 剩余的x距离
     * @param dyUnconsumed 剩余的y距离
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.behavior != null) {
                lp.behavior.onNestedScroll(target, child, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            }
        }
    }

    static class LayoutParams extends ConstraintLayout.LayoutParams {

        Behavior behavior;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            @SuppressLint("CustomViewStyleable")
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.BehaviorCoordinateLayout);
            String behaviorClassName = typedArray.getString(R.styleable.BehaviorCoordinateLayout_layout_behavior);
            behavior = parseBehavior(behaviorClassName, c, attrs);
            typedArray.recycle();
        }

        private Behavior parseBehavior(String behaviorClassName, Context context, AttributeSet attrs) {
            if (TextUtils.isEmpty(behaviorClassName)) {
                return null;
            }
            try {
                Class<?> clazz = Class.forName(behaviorClassName);
                Constructor<?> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
                constructor.setAccessible(true);
                return (Behavior) constructor.newInstance(context, attrs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
