package com.hc.my_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import com.hc.util.ViewUtils;

public class SlidingMenu extends HorizontalScrollView {
    private float mMenuMarginRight;
    private int mMenuWidth;
    private View mMenuView;
    private View mContentView;
    private View mShadowView;
    private ViewGroup mContentContainer;
    private final GestureDetector mGestureDetector;

    private boolean isMenuOpen = false;
    private boolean mCartoon = false;
    private boolean shadow = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttr(attrs);
        mMenuWidth = (int) (ViewUtils.getDisplayWidth(context) - mMenuMarginRight);
        mGestureDetector = new GestureDetector(context, new GestureDetectorListener());
    }

    private void loadAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        mMenuMarginRight = ViewUtils.dp2px(typedArray.getDimension(R.styleable.SlidingMenu_menuMarginRight, 0));
        mCartoon = typedArray.getBoolean(R.styleable.SlidingMenu_cartoon, false);
        shadow = typedArray.getBoolean(R.styleable.SlidingMenu_shadow, false);
        typedArray.recycle();
        mMenuWidth = (int) (ViewUtils.getDisplayWidth(getContext()) - mMenuMarginRight);
    }

    /**
     * 设置menu和content的宽度
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup container = (ViewGroup) getChildAt(0);
        if (container != null) {
            int childCount = container.getChildCount();
            if (childCount != 2) {
                throw new RuntimeException("You have to have two children");
            }
            mMenuView = container.getChildAt(0);
            ViewGroup.LayoutParams menuParams = mMenuView.getLayoutParams();
            menuParams.width = mMenuWidth;
            mMenuView.setLayoutParams(menuParams);

            mContentView = container.getChildAt(1);
            ViewGroup.LayoutParams contentParams = mContentView.getLayoutParams();
            contentParams.width = ViewUtils.getDisplayWidth(getContext());
            mContentView.setLayoutParams(contentParams);

            if (shadow) {
                container.removeView(mContentView);
                mContentContainer = new RelativeLayout(getContext());
                mContentContainer.addView(mContentView);
                mShadowView = new View(getContext());
                mShadowView.setBackgroundColor(Color.parseColor("#55000000"));
                mContentContainer.addView(mShadowView);
                mContentContainer.setLayoutParams(contentParams);
                container.addView(mContentContainer);
            }
        }
    }

    /**
     * 随着滑动距离的变化，来缩放以及渐变 平移menuView 和contentView
     * 来达到视觉上的效果
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (shadow) {
            float alpha = 1 - 1f * l / mMenuWidth;
            mShadowView.setAlpha(alpha);
        }
        if (mCartoon) {
            //以0.7为基数 缩放contentView
            float contentScale = (float) (0.7 + 0.3 * l*1f/mMenuView.getMeasuredWidth());
            // 以屏幕左边的中心位置缩放，设置缩放轴
            mContentContainer.setPivotX(0f);
            mContentContainer.setPivotY(mContentContainer.getMeasuredHeight() / 2f);
            // 开始缩放
            mContentContainer.setScaleX(contentScale);
            mContentContainer.setScaleY(contentScale);

            //给MenuView设置渐变 基数为0.5
            float menuAlpha = (float) (1 - 0.5*l*1f/mMenuView.getMeasuredWidth());
            mMenuView.setAlpha(menuAlpha);
            //给MenuView设置缩放
            float menuScale = (float) (1 - 0.3 * l * 1f/mMenuView.getMeasuredWidth());
            mMenuView.setScaleX(menuScale);
            mMenuView.setScaleY(menuScale);

            //设置menuView的抽屉效果
            mMenuView.setTranslationX(0.25f*l);
        }
    }


    /**
     * 在这里实现了BottomSheetDialog等一样的效果，如拖动到一半，能够收起来或者展开
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetector.onTouchEvent(ev)) {        // 将事件传递给GestureDetector
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int scrollX = getScrollX();
            if (scrollX > mMenuView.getMeasuredWidth() / 2) {
                closeMenu();
            } else {
                openMenu();
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMenuView.getMeasuredWidth() < ev.getX() && isMenuOpen) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenuWidth, 0);
    }

    /**
     * 通过smoothScrollTo平滑滑动
     */
    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isMenuOpen = false;
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }

    /**
     * 这里不能通过重写fling等方法，因为这些方法没有被调用，实现快速滑动
     */
    private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) < Math.abs(velocityY)) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            if (isMenuOpen) {
                if (velocityX < 0) {
                    closeMenu();
                    return true;
                }
            } else {
                if (velocityX > 0) {
                    openMenu();
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
