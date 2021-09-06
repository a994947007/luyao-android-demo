package com.hc.my_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局，类似于swing中的FlowLayout
 */
public class FlowLayout extends ViewGroup {
    private final int mHorizontalSpacing = dp2px(16);   // 组件水平间距
    private final int mVerticalSpacing = dp2px(8);      // 组件垂直间距

    private final List<List<View>> allLines = new ArrayList<>();      // 记录每一行有哪些组件
    private final List<Integer> allLinesHeights = new ArrayList<>();   // 记录每一行的最大高度

    /**
     * 用于new
     */
    public FlowLayout(Context context) {
        super(context);
    }

    /**
     * 用于xml布局
     */
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * xml布局且支持自定义样式
     */
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void clearAllLinesRecord() {
        allLines.clear();
        allLinesHeights.clear();
    }

    /**
     * 测量FlowLayout的大小
     * @param widthMeasureSpec 父容器给出的参考宽度Spec
     * @param heightMeasureSpec 父容器给出的参考高度Spec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        clearAllLinesRecord();      // 由于onMeasure可能会重复调用，每次调用需要先清理一下记录
        int parentNeedWidth = 0;
        int parentNeedHeight = 0;
        int lineWidthUsed = 0;      // 由于宽度在遍历child时是变动的，所以需要用一个变量记录累加的宽度
        int lineMaxHeight = 0;      // 由于各个组件高度不一定一致，需要统计一行中最高的那个高度

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);  // 父亲给出的参考宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

        @SuppressLint("DrawAllocation") List<View> lineViews = new ArrayList<>();   //记录当前行有哪些组件

        /*
         * 1、先度量孩子，度量的同时计算FlowLayout的大小
         */
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams childLayoutParams = child.getLayoutParams();
            // 计算child的MeasureSpec，度量孩子
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), childLayoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), childLayoutParams.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // 计算FlowLayout的宽度和高度
            int measuredChildWidth = child.getMeasuredWidth();  // 注意这里纯宽度值，区别于上述MeasureSpec
            int measuredChildHeight = child.getMeasuredHeight();

            // 超出容器范围，换行
            if (lineWidthUsed + measuredChildWidth + mHorizontalSpacing > selfWidth) {
                parentNeedWidth = Math.max(lineWidthUsed + mHorizontalSpacing, parentNeedWidth);
                parentNeedHeight += lineMaxHeight + mVerticalSpacing;
                allLines.add(lineViews);
                allLinesHeights.add(lineMaxHeight);
                lineViews = new ArrayList<>();  // 新起一行记录
                lineMaxHeight = 0;
                lineWidthUsed = 0;
            }

            // 累计宽度，同时计算行中最大高度
            lineWidthUsed += measuredChildWidth + mHorizontalSpacing;
            lineMaxHeight = Math.max(lineMaxHeight, measuredChildHeight);
            lineViews.add(child);

            // 当测量到最后一个组件时，后续不会再走换行逻辑，也就无法将其加到parentNeedWidth和parentNeedHeight，因此需要单独处理
            if (i == childCount - 1) {
                parentNeedWidth = Math.max(lineWidthUsed + mHorizontalSpacing, parentNeedWidth);
                parentNeedHeight += lineMaxHeight;
                allLines.add(lineViews);
                allLinesHeights.add(lineMaxHeight);
            }
        }

        /*
         * 2、度量自己，根据上述计算得到的宽度和高度进行度量吗，由于FlowLayout可能被直接被指定了具体的dp值，这种情况可直接设置成对应的值
         */
        parentNeedWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? selfWidth : parentNeedWidth;
        parentNeedHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ? selfHeight : parentNeedHeight;
        setMeasuredDimension(parentNeedWidth, parentNeedHeight);
    }

    /**
     * 布局，对每一个child进行布局，在此之前，需要保存每个child的位置，这一步可以在onMeasure时完成。当然也可以只记录每行有哪些组件，在onLayout时计算即可
     * 要理解这个布局，需要先理解layout布局时参考的坐标系
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int curLeft = getPaddingLeft();
        int curTop = getPaddingTop();
        for (int i = 0; i < allLines.size(); i++) {
            List<View> lineVies = allLines.get(i);
            int lineMaxHeight = allLinesHeights.get(i);
            for (View child : lineVies) {
                int curRight = curLeft + child.getMeasuredWidth();
                int curBottom = curTop + child.getMeasuredHeight();
                child.layout(curLeft, curTop, curRight, curBottom);
                curLeft = curRight + mHorizontalSpacing;
            }
            curLeft = getPaddingLeft(); // 换行，从左边重新开始
            curTop = curTop + lineMaxHeight + mVerticalSpacing;
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