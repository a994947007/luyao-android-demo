package com.hc.my_views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * 九宫格，将ViewGroup分层九个单元
 * 一共四种模式：
 *  1、1张图片，则maxWidth=2单元宽度，maxHeight=2单元的高度
 *  2、4张图片，每张图片的宽度和高度都等于1个单元格的宽度和高度，2x2的方式
 *  3、9张图片，图片宽度和高度等同1个单元格的宽度和高度，3x3的方式
 *  4、其他数量的图片，同FlowLayout的摆放方式，从上到下依次摆放
 *  注意：使用该布局，宽度尽量不要使用wrap_content来设置它的宽度，以免引起测量不正确
 */
public class ImageGridLayout extends ViewGroup {
    private final int mHorizontalSpacing = dp2px(4);   // 组件水平间距
    private final int mVerticalSpacing = dp2px(4);      // 组件垂直间距

    private static final int TYPE_1_1 = 1;      // 1x1图片
    private static final int TYPE_2_2 = 2;      // 2x2图片
    private static final int TYPE_DEFAULT = 0;    // 其他数量的图片

    private int type = TYPE_DEFAULT;

    private int cellWidth = 0;      // 一个单元的估计宽度
    private int cellWidthMode = 0;

    private int cellHeight = 0;     // 一个单元的估计高度
    private int cellHeightMode = 0;

    public ImageGridLayout(Context context) {
        super(context);
    }

    public ImageGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 计算当前的模式
     */
    private void measureType() {
       if (getChildCount() == 1) {
           type = TYPE_1_1;
       } else if (getChildCount() == 4) {
           type = TYPE_2_2;
       }
    }

    /**
     * 计算当前一个单元的宽度和高度
     */
    private void measureCell(int widthMeasureSpec, int heightMeasureSpec) {
        int estimateWidth = MeasureSpec.getSize(widthMeasureSpec);
        int estimateHeight = MeasureSpec.getSize(heightMeasureSpec);
        cellWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        cellHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        cellWidth = (estimateWidth - getPaddingLeft() - getPaddingRight() - 2 * mHorizontalSpacing) / 3;
        if (cellHeightMode == MeasureSpec.EXACTLY) {
            cellHeight = (estimateHeight - getPaddingTop() - getPaddingBottom() - 2 * mVerticalSpacing) / 3;
        } else {
            cellHeight = cellWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureType();
        measureCell(widthMeasureSpec, heightMeasureSpec);
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);  // 父亲给出的参考宽度
        int parentNeedWidth = getPaddingLeft() + getPaddingRight();
        int parentNeedHeight = getPaddingTop() + getPaddingBottom();
        if (type == TYPE_1_1) {
            View child = getChildAt(0);
            measureType_1_1(child);
            parentNeedWidth += getChildAt(0).getMeasuredWidth();
            parentNeedHeight += getChildAt(0).getMeasuredHeight();
        } else if (type == TYPE_2_2) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                measureType_2_2(child);
            }
            parentNeedWidth += getChildAt(0).getMeasuredWidth() * 2 + mHorizontalSpacing;
            parentNeedHeight += getChildAt(0).getMeasuredHeight() * 2 + mVerticalSpacing;
        } else {
            int lineWidthUsed = getPaddingLeft() + getPaddingRight();
            int lineCount = getPaddingTop() + getPaddingBottom();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                measureTypeDefault(child);
                if (lineCount== 3) {
                    parentNeedHeight += getChildAt(0).getMeasuredHeight() + mVerticalSpacing;
                    lineCount = 0;
                    lineWidthUsed = 0;
                    lineWidthUsed -= mHorizontalSpacing;
                    parentNeedWidth = Math.max(lineWidthUsed, parentNeedWidth);
                }
                lineWidthUsed += getChildAt(0).getMeasuredWidth() + mHorizontalSpacing;
                lineCount ++;
                if (i == getChildCount() - 1) {
                    parentNeedHeight += getChildAt(0).getMeasuredHeight();
                }
            }
        }
        parentNeedWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? selfWidth : parentNeedWidth;
        setMeasuredDimension(parentNeedWidth, parentNeedHeight);
    }

    private void measureType_1_1(View child) {
        int maxWidth = 2 * cellWidth + mHorizontalSpacing;
        int maxHeight = 2 * cellHeight + mVerticalSpacing;
        int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(maxWidth, cellWidthMode), 0, maxWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(maxHeight, cellHeightMode), 0, maxHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private void measureType_2_2(View child) {
        measureTypeDefault(child);
    }

    private void measureTypeDefault(View child) {
        int childWidthMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(cellWidth, cellWidthMode), 0, cellWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(MeasureSpec.makeMeasureSpec(cellHeight, cellHeightMode), 0, cellHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int maxLineCount;
        if (type == TYPE_1_1) {
            maxLineCount = 1;
        } else if (type == TYPE_2_2) {
            maxLineCount = 2;
        } else {
            maxLineCount = 3;
        }
        int curLeft = getPaddingLeft();
        int curTop = getPaddingTop();
        for (int i = 0, j = 0; i < getChildCount(); i++,j++) {
            View view = getChildAt(i);
            if (j == maxLineCount) {
                curLeft = getPaddingLeft();
                curTop = curTop + view.getMeasuredWidth() + mVerticalSpacing;
                j = 0;
            }
            int curRight = curLeft + view.getMeasuredWidth();
            int curBottom = curTop + view.getMeasuredHeight();
            view.layout(curLeft, curTop, curRight, curBottom);
            curLeft = curRight + mHorizontalSpacing;
        }
    }

    /**
     * 将dp转换成px
     */
    public int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue, Resources.getSystem().getDisplayMetrics());
    }

    public abstract static class ViewHolder {
        private final View itemView;
        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    public abstract static class Adapter<V extends ViewHolder> {
        public abstract V onCreateViewHolder(ViewGroup parent);
        public abstract void onBindViewHolder(V holder, int position);
        public abstract int getItemCount();
    }

    public void setAdapter(Adapter adapter) {
        int itemCount = adapter.getItemCount();
        for (int i = 0;i < itemCount; i++) {
            ViewHolder viewHolder = adapter.onCreateViewHolder(this);
            adapter.onBindViewHolder(viewHolder, i);
            addView(viewHolder.itemView);
        }
    }
}
