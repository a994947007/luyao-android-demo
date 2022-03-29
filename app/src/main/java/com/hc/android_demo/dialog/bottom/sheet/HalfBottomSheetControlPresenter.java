package com.hc.android_demo.dialog.bottom.sheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.HalfBottomSheetBehavior;
import com.hc.android_demo.R;
import com.hc.my_views.SimpleRecyclerView;
import com.hc.support.mvps.Presenter;
import com.hc.util.ViewUtils;

public class HalfBottomSheetControlPresenter extends Presenter {
    private View headerView;
    private HalfBottomSheetBehavior<View> mBehavior;
    private HalfBottomSheetDialogFragmentTest dialogFragmentTest;
    private SimpleRecyclerView homeRecyclerView;
    private View containerView;
    private boolean isExpanded;
    private float mCurrentOffsetPix;
    private float mInitOffsetPix = ViewUtils.dp2px(450);

    @Override
    protected void doInject() {
        super.doInject();
        dialogFragmentTest = inject(HalfBottomSheetDialogFragmentTest.class);
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        headerView = rootView.findViewById(R.id.header);
        homeRecyclerView = rootView.findViewById(R.id.recyclerView);
        containerView = rootView.findViewById(R.id.container);
    }

    @Override
    protected void onBind() {
        super.onBind();
        View containerView = dialogFragmentTest.getDialog().getWindow().findViewById(R.id.design_bottom_sheet);
        mBehavior = (HalfBottomSheetBehavior<View>) BottomSheetBehavior.from(containerView);
        mCurrentOffsetPix = mInitOffsetPix;
        mBehavior.setPeekHeight(ViewUtils.dp2px(900));
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialogFragmentTest.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset < -0.5f) {
                    dialogFragmentTest.dismissAllowingStateLoss();
                }
                if (isExpanded && slideOffset <= 0 && slideOffset >= -1) {
                    float slideOffsetPx = getSlideOffsetPx(slideOffset);
                    if (slideOffsetPx < -ViewUtils.dp2px(200)) {

                    }
                }
                Log.d("HalfBottomSheetControlPresenter", "" + slideOffset);
            }
        });
        headerView.setOnClickListener(v -> {
            if (!isExpanded) {
                expendAnimator.start();
                isExpanded = true;
            } else {
                collapseAnimator.start();
                isExpanded = false;
            }
        });
    }

    private float getCurrentOffsetPx() {
        return homeRecyclerView.getHeight() - ViewUtils.dp2px(450);
    }

    private static float getSlideOffsetPx(float slideOffset) {
        return ViewUtils.dp2px(900) * slideOffset;
    }

    private ValueAnimator expendAnimator = new ValueAnimator();
    {
        expendAnimator.setFloatValues(0, 1);
        expendAnimator.setDuration(300);
        expendAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float)animation.getAnimatedValue();
                homeRecyclerView.getLayoutParams().height = (int) (ViewUtils.dp2px(450) + ViewUtils.dp2px(400) * ratio);
                homeRecyclerView.requestLayout();
            }
        });
        expendAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentOffsetPix = getCurrentOffsetPx();
            }
        });
    }

    private ValueAnimator collapseAnimator = new ValueAnimator();
    {
        collapseAnimator.setFloatValues(1, 0);
        collapseAnimator.setDuration(300);
        collapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float)animation.getAnimatedValue();
                homeRecyclerView.getLayoutParams().height = (int) (ViewUtils.dp2px(450) + ViewUtils.dp2px(400) * ratio);
                homeRecyclerView.requestLayout();
            }
        });
    }
}
