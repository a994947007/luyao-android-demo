package com.hc.my_views.nestedScrollVIew;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * 滑动事件的消费者，消费完成，还需要还给子View
 */
public interface MyNestedScrollingParent {
    /**
     * 开始滑动，如果返回false，表示该parent不参与滑动距离的消费，axes是方向
     * target是某个子或者孙View，滑动事件的发起者
     * child当前parent的直接子View，有可能就是target
     */
    boolean onStartNestedScroll(View child, View target, int axes);

    /**
     * 通过onStartNestedScroll，target View已经知道parentView是否接收滑动，如果接收，这里进行后续的初始化工作
     */
    void onNestedScrollAccepted(View child, View target, int axes);

    /**
     * target通过onStopNestedScroll发送停止事件
     */
    void onStopNestedScroll(View target);

    /**
     * targetView传递给父View已消费和未消费的距离
     */
    void onNestedPreScroll(View target, int dx, int dy, int [] consumed);

    /**
     * targetView传递给父View已消费和未消费的距离
     */
    void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnConsumed, int dyUnConsumed);

    /**
     * targetView传递给parent的速度
     */
    boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY);

    /**
     * target fling后的剩余速度
     */
    boolean onNestedFling(View target, float velocityX, float velocityY);

    /**
     * 获取滑动的方向
     */
    int getNestedScrollAxes();
}
