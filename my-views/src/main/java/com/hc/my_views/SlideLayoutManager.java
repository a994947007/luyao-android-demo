package com.hc.my_views;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.util.ViewUtils;

/**
 * 折叠卡片布局
 */
public class SlideLayoutManager extends RecyclerView.LayoutManager{
    private static final int MAX_SHOW_COUNT = 4;    // 最多显示4张
    private static final int TRANSLATION_Y = ViewUtils.dp2px(10);
    private static final float SCALE = 0.05f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 1、回收复用
        detachAndScrapAttachedViews(recycler);
        // 2、显示布局的卡片，0、1、2、3、4、5、6、7（显示4、5、6、7）
        int itemCount = getItemCount();
        int itemPosition = itemCount - MAX_SHOW_COUNT;
        if (itemPosition >= 0) {
            for (int i = itemPosition; i < itemCount; i++) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                // 测量孩子
                measureChildWithMargins(view, 0, 0);
                // 获取view的宽度，这里计算了decoration的宽度
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                // 获取view的宽度，这里计算了decoration的宽度
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // 布局，这个布局方法计算了Decoration
                layoutDecoratedWithMargins(view, widthSpace/2, heightSpace/2,
                        widthSpace/2 + getDecoratedMeasuredWidth(view), heightSpace/2 + getDecoratedMeasuredHeight(view));
                int level = itemCount - i - 1;
                if (level <= 0) {
                    continue;
                }
                if (MAX_SHOW_COUNT - 1 > level) {
                    view.setTranslationY(TRANSLATION_Y * level);
                    view.setScaleX(1 - SCALE * level);
                    view.setScaleY(1 - SCALE * level);
                } else {
                    view.setTranslationY(TRANSLATION_Y * (level - 1));
                    view.setScaleX(1 - SCALE * (level - 1));
                    view.setScaleY(1 - SCALE * (level - 1));
                }

            }
        }
    }
}
