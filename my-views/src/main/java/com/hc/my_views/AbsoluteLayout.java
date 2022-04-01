package com.hc.my_views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

/**
 * 绝对布局，不受键盘弹起影响
 */
public class AbsoluteLayout extends ViewGroup {

  private int mInitWidth;
  private int mInitHeight;

  public AbsoluteLayout(@NonNull Context context) {
    this(context, null);
  }

  public AbsoluteLayout(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AbsoluteLayout(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setupForInsets();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mInitWidth == 0) {
      mInitWidth = MeasureSpec.getSize(widthMeasureSpec);
      mInitHeight = MeasureSpec.getSize(heightMeasureSpec);
    }
    int parentPaddingLeft = getPaddingLeft();
    int parentPaddingRight = getPaddingRight();
    int parentPaddingTop = getPaddingTop();
    int parentPaddingBottom = getPaddingBottom();
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
      final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
          parentPaddingLeft + parentPaddingRight + lp.leftMargin + lp.rightMargin,
          lp.width);
      final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
          parentPaddingTop + parentPaddingBottom +lp.topMargin + lp.bottomMargin,
          lp.height);
      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    setMeasuredDimension(mInitWidth, mInitHeight);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final int layoutDirection = ViewCompat.getLayoutDirection(this);
    int childCount = getChildCount();
    int parentPaddingLeft = getPaddingLeft();
    int parentPaddingTop = getPaddingTop();
    int parentPaddingBottom = getPaddingBottom();
    int parentPaddingRight = getPaddingRight();
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      final AbsoluteLayoutParams lp = (AbsoluteLayoutParams) child.getLayoutParams();

      final Rect parent = acquireTempRect();
      parent.set(parentPaddingLeft + lp.leftMargin,
          parentPaddingTop + lp.topMargin,
          getWidth() - parentPaddingRight - lp.rightMargin,
          getHeight() - parentPaddingBottom - lp.bottomMargin);

      final Rect out = acquireTempRect();
      GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
          child.getMeasuredHeight(), parent, out, layoutDirection);
      child.layout(out.left, out.top, out.right, out.bottom);

      releaseTempRect(parent);
      releaseTempRect(out);
    }
  }

  private static int resolveGravity(int gravity) {
    if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.NO_GRAVITY) {
      gravity |= GravityCompat.START;
    }
    if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.NO_GRAVITY) {
      gravity |= Gravity.TOP;
    }
    return gravity;
  }

  private static final Pools.Pool<Rect> sRectPool = new Pools.SynchronizedPool<>(12);

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

  @Override
  protected LayoutParams generateLayoutParams(LayoutParams p) {
    return new AbsoluteLayoutParams(p);
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new AbsoluteLayoutParams(getContext(), attrs);
  }

  public static class AbsoluteLayoutParams extends FrameLayout.LayoutParams {

    public AbsoluteLayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
    }

    public AbsoluteLayoutParams(int width, int height) {
      super(width, height);
    }

    public AbsoluteLayoutParams(LayoutParams source) {
      super(source);
    }
  }

  private void setupForInsets() {
    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }
}
