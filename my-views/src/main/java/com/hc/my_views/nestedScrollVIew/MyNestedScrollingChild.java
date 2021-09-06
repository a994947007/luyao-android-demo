package com.hc.my_views.nestedScrollVIew;

/**
 * 滑动事件的主要发起者，滑动距离的分发者
 */
public interface MyNestedScrollingChild {

    /**
     * 开始滑动，该过程希望找到可滑动的parentView
     */
    boolean startNestedScroll(int orientation);

    void stopNestedScroll();

    /**
     * 找到parent应该直接记录在当前View，便于后续直接让parent处理
     */
    boolean hasNestedScrollingParent();

    /**
     * 子View一旦找到可以滑动的parent，先通过该方法将滑动距离分发给parent，consumed用于记录parent消费的距离
     */
    boolean dispatchNestedPreScroll(int dx, int dy, int [] consumed);

    /**
     * 当dispatchNestedPreScroll没消费完，子View继续消费，子View还没消费完时，将距离继续分发给parent
     * 四个参数分别记录了当前子View消费和未消费的距离
     */
    boolean dispatchNestedScroll(int consumedX, int consumedY, int unConsumedX, int unConsumedY, int [] consumed);

    /**
     * 手抬起时，速度剩余，实现惯性滑动，将剩余速度发送给parent
     */
    boolean dispatchNestedPreFling(float velocityX, float velocityY);

    /**
     * 手抬起时，当dispatchNestedPreFling没消费完，子View消费完成之后，将剩余速度发送给parent，consumed标记是否已经被消费
     */
    boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);
}
