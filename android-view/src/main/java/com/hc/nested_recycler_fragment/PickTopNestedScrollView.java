package com.hc.nested_recycler_fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * 1、为了支持嵌套滑动，需要使用NestedScrollView，它实现了NestedScrollingParent3接口
 * 2、为了支持吸顶，需要将bodyView的height设置成ScrollView的height，这样化到底会卡着，自定吸顶
 * 3、为了在RecyclerView开始滑的时候，让parent先滑，这就需要重写onNestedPreScroll，因为NestedScrollView中又去询问父亲是否可以滑，这使得ScrollView没有消费滑动事件
 * 4、为了支持scrollView的滑动后的余力可以带着RecyclerView继续要，我们需要重写其fling函数，将scroll滑动的剩余速度给recyclerView，致辞惯性滑动
 */
public class PickTopNestedScrollView extends NestedScrollView {
    private View headerView;
    private ViewGroup bodyView;
    private View bodyHeader;
    private ViewPager viewPager;
    public PickTopNestedScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public PickTopNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PickTopNestedScrollView(@NonNull  Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView = ((ViewGroup)getChildAt(0)).getChildAt(0);
        bodyView = (ViewGroup) ((ViewGroup)getChildAt(0)).getChildAt(1);
        bodyHeader = bodyView.getChildAt(0);
    }

    private FlingHelper mFlingHelper;

    private void init() {
        mFlingHelper = new FlingHelper(getContext());
    }

    private int recentVelocityY;

    /**
     * 子View传递上来的View fling 不能通过重写这个方法来实现滑动，有时候会不太灵敏
     */
/*    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY <= 0) {
            startFling = false;
            return super.onNestedFling(target, velocityX, velocityY, consumed);
        }
        if (startFling) {
            return super.onNestedFling(target, velocityX, velocityY, consumed);
        }
        startFling = true;
        double splineFlingDistance = mFlingHelper.getSplineFlingDistance((int) velocityY);
        if (splineFlingDistance > headerView.getMeasuredHeight() - getScrollY()) {
            childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - (headerView.getMeasuredHeight() - getScrollY())));
        }
        return true;
    }*/

    private void childFling(int velY) {
        RecyclerView recyclerView = getCurrentNestedRecyclerView();
        if (recyclerView != null) {
            recyclerView.fling(0, velY);
        }
    }

    private RecyclerView getCurrentNestedRecyclerView() {
        RecyclerView recyclerView;
        if (viewPager == null) {
            viewPager = getTargetChildView(bodyView, ViewPager.class);
        }
        recyclerView = getTargetChildView(viewPager.findViewWithTag(viewPager.getCurrentItem()), RecyclerView.class);
        return recyclerView;
    }

    /**
     * 重写这个方法，当scrollView停止滑动时，子View才开始滑动
     */
    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        if (scrollY - headerView.getMeasuredHeight() == 0) {
            childFling(recentVelocityY);
            recentVelocityY = 0;
        } else if (scrollY < headerView.getMeasuredHeight()) {  // 已经滑出
            if (viewPager == null) {
                viewPager = getTargetChildView(bodyView, ViewPager.class);
            }
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        boolean hideTop = dy > 0 && getScrollY() < headerView.getMeasuredHeight();  // 如果是上滑，并且滑动的距离 < headerView的高度时，先让scrollView滑
        if (hideTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    /**
     * scrollView自己的fling，不能直接将自己剩余的滑动距离传给孩子，这使得孩子和ScrollView一起滑，导致孩子滑不了多远
     * 应该设置一个监听，在ScrollView停止滑动时，子View开始滑动
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        double splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY);
        if (splineFlingDistance > headerView.getMeasuredHeight() - getScrollY()) {
            recentVelocityY = mFlingHelper.getVelocityByDistance(splineFlingDistance - (headerView.getMeasuredHeight() - getScrollY()));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = bodyView.getLayoutParams();
        layoutParams.height = getMeasuredHeight();      // 将bodyView的高度设置成scrollView
        bodyView.setLayoutParams(layoutParams);
    }

    public static class FlingHelper {
        private static float                                                                                                                                                                                              DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
        private static float mFlingFriction = ViewConfiguration.getScrollFriction();
        private static float mPhysicalCoeff;

        public FlingHelper(Context context) {
            mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        }

        private double getSplineDeceleration(int i) {
            return Math.log((double) ((0.35f * ((float) Math.abs(i))) / (mFlingFriction * mPhysicalCoeff)));
        }

        private double getSplineDecelerationByDistance(double d) {
            return ((((double) DECELERATION_RATE) - 1.0d) * Math.log(d / ((double) (mFlingFriction * mPhysicalCoeff)))) / ((double) DECELERATION_RATE);
        }

        public double getSplineFlingDistance(int i) {
            return Math.exp(getSplineDeceleration(i) * (((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d))) * ((double) (mFlingFriction * mPhysicalCoeff));
        }

        public int getVelocityByDistance(double d) {
            return Math.abs((int) (((Math.exp(getSplineDecelerationByDistance(d)) * ((double) mFlingFriction)) * ((double) mPhysicalCoeff)) / 0.3499999940395355d));
        }
    }

    private static  <T extends View> T getTargetChildView(View parent, Class<T> viewClass) {
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                T grandson;
                if(viewClass.isAssignableFrom(child.getClass())){
                    return (T) child;
                } else if (child instanceof ViewPager) {
                    ViewPager viewPager = (ViewPager) child;
                    int childIndex = viewPager.getCurrentItem();
                    if (viewPager.getCurrentItem() > 1) {
                        childIndex = 1;
                    }
                    grandson = getTargetChildView(viewPager.getChildAt(childIndex), viewClass);
                } else {
                    grandson = getTargetChildView(child, viewClass);
                }
                if (grandson != null) {
                    return grandson;
                }
            }
        }
        return null;
    }

    /**
     * 如果点击在header区域和tabLayout区域，就拦截调，自己处理滑动事件
     * 如果已经吸顶了，此时也不能通过点击Layout向下滑
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction()== MotionEvent.ACTION_MOVE) {
            int recentHeader = headerView.getMeasuredHeight() + bodyHeader.getMeasuredHeight() - getScrollY();
            // 如果点击在header区域和tabLayout区域，就拦截调，自己处理滑动事件
            if (recentHeader > 0 && ev.getY() < recentHeader) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean clickedHeader = false;

    /**
     * 不能直接在onInterceptTouchEvent中拦截：
     * 1、如果在这里使用onInterceptTouchEvent，并计算是否点击tab header来返回true来拦截，达不到效果，原因是如果返回为true，
     *  当前scrollView会将Move Event分发给子Layout，由于TabLayout没有能力处理move事件，最终还是由ScrollView自己处理，没有阻断
     *  吸顶时滑动。
     * 2、也不能在onInterceptorTouchEvent或dispatchTouchEvent申请拦截requestDisallowInterceptTouchEvent，
     *  原因是父ViewGroup记录了Down事件的分发者为ScrollView，ScrollView又记录了为TabLayout的item，此时，
     *  父ViewGroup直接将Event交给了ScrollView，我们不能申请DOWN的时候让父ViewGroup拦截，否则TabLayout的Item将不能被点击，
     *  如果在Move的时候根据是否是吸顶来申请拦截也是达不到效果的，原因是父ViewGroup的firstTarget在Down的时候已经记录了，就算是拦截
     *  标志位为true，最终还是会走将MoveEvent分发给子View的逻辑。
     * 因此：重写onTouchEvent是最终的解决方案，它会在当前ScrollView消费Move事件的时候拦截。
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!clickedHeader) {   // 如果没有点击过，需要判断是否点击了tab header
                int recentHeader = headerView.getMeasuredHeight() + bodyHeader.getMeasuredHeight() - getScrollY();
                // 如果已经吸顶了，此时也不能通过点击Layout向下滑，类似于美团的吸顶效果
                if (headerView.getMeasuredHeight() - getScrollY() == 0 && ev.getY() < recentHeader) {
                    clickedHeader = true;
                }
            }
            // 如果已经吸顶了，此时也不能通过点击Layout向下滑，类似于美团的吸顶效果
            if (clickedHeader) {
                RecyclerView recyclerView = getCurrentNestedRecyclerView();
                // 吸顶，且Fragment内部的Recycler有滑动
                if (getRecyclerScrolledY(recyclerView) > 0) {
                    return true;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            clickedHeader = false;
        }
        return super.onTouchEvent(ev);
    }

    private int getRecyclerScrolledY(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }
}
