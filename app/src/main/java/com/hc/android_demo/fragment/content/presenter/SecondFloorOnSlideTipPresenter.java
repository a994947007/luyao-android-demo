package com.hc.android_demo.fragment.content.presenter;

import android.view.View;
import android.widget.TextView;

import com.hc.android_demo.R;
import com.hc.my_views.PullAnimatedView;
import com.hc.my_views.secondFloor.SecondFloorRefreshLayout;
import com.hc.support.mvps.Presenter;
import com.hc.util.ViewUtils;

public class SecondFloorOnSlideTipPresenter extends Presenter {

    private TextView refreshTip;
    private TextView secondFloorTip;
    private SecondFloorRefreshLayout secondFloorRefreshLayout;
    private PullAnimatedView pullAnimatedView;
    private View mShadowView;

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        refreshTip = rootView.findViewById(R.id.refreshTip);
        secondFloorTip = rootView.findViewById(R.id.secondFloorTip);
        secondFloorRefreshLayout = rootView.findViewById(R.id.secondFloorRefreshLayout);
        pullAnimatedView = rootView.findViewById(R.id.pullAnimatedView);
        mShadowView = rootView.findViewById(R.id.shadowView);
    }

    @Override
    protected void onBind() {
        secondFloorRefreshLayout.addOnSlideListener(new SecondFloorRefreshLayout.OnSlideListener() {
            @Override
            public void onSlide(float offset, float offsetPercent) {
                if (offset < ViewUtils.dp2px(50)) {
                    refreshTip.setVisibility(View.GONE);
                    secondFloorTip.setVisibility(View.GONE);
                }else if (offset >= ViewUtils.dp2px(50) && offset < ViewUtils.dp2px(65)) {
                    if (refreshTip.getVisibility() != View.VISIBLE) {
                        refreshTip.setVisibility(View.VISIBLE);
                    }
                    float scaleRatio = ((offset - ViewUtils.dp2px(50)) / ViewUtils.dp2px(15));
                    refreshTip.setScaleX(scaleRatio);
                    refreshTip.setScaleY(scaleRatio);
                } else if (offset >= ViewUtils.dp2px(65) && offset < ViewUtils.dp2px(95)) {
                    refreshTip.setTranslationY(offset - ViewUtils.dp2px(65));
                    refreshTip.setScaleX(1f);
                    refreshTip.setScaleY(1f);
                } else if (offset >= ViewUtils.dp2px(95) && offset < ViewUtils.dp2px(120)) {
                    if (refreshTip.getVisibility() != View.VISIBLE) {
                        refreshTip.setVisibility(View.VISIBLE);
                    }
                    refreshTip.setTranslationY(offset - ViewUtils.dp2px(65));
                    float scaleRatio = ((offset - ViewUtils.dp2px(95)) / ViewUtils.dp2px(25));
                    refreshTip.setScaleX(1 - scaleRatio);
                    refreshTip.setScaleY(1 - scaleRatio);
                } else {
                    if (refreshTip.getVisibility() != View.GONE) {
                        refreshTip.setVisibility(View.GONE);
                    }
                }

                if (offset < ViewUtils.dp2px(95)) {
                    secondFloorTip.setVisibility(View.GONE);
                }else if (offset >= ViewUtils.dp2px(95) && offset < ViewUtils.dp2px(120)) {
                    if (secondFloorTip.getVisibility() != View.VISIBLE) {
                        secondFloorTip.setVisibility(View.VISIBLE);
                    }
                    float ratio = ((offset - ViewUtils.dp2px(95)) / ViewUtils.dp2px(25));
                    secondFloorTip.setAlpha(ratio);
                    secondFloorTip.setTranslationY(offset - ViewUtils.dp2px(95));
                } else if (offset >= ViewUtils.dp2px(120) && offset < ViewUtils.dp2px(150)) {
                    secondFloorTip.setTranslationY(offset - ViewUtils.dp2px(95));
                } else if (offset >= ViewUtils.dp2px(150) && offset < ViewUtils.dp2px(175)){
                    if (secondFloorTip.getVisibility() != View.VISIBLE) {
                        secondFloorTip.setVisibility(View.VISIBLE);
                    }
                    secondFloorTip.setTranslationY(offset - ViewUtils.dp2px(95));
                    float ratio = ((offset - ViewUtils.dp2px(150)) / ViewUtils.dp2px(25));
                    secondFloorTip.setAlpha(1 - ratio);
                } else {
                    if (secondFloorTip.getVisibility() != View.GONE) {
                        secondFloorTip.setVisibility(View.GONE);
                    }
                }

                float ratio;
                if (offset < ViewUtils.dp2px(20)) {
                    ratio = offset / ViewUtils.dp2px(20);
                } else {
                    ratio = 1;
                }

                int animatedOffsetY = (int) ((int) offset + ViewUtils.dp2px(50) * ratio);
                pullAnimatedView.show(0, animatedOffsetY, pullAnimatedView.getMeasuredWidth(),
                        animatedOffsetY,  pullAnimatedView.getMeasuredWidth() / 2, (int) ((int) animatedOffsetY + 120 * ratio));
                mShadowView.setAlpha(1 - offsetPercent);
            }
        });
    }
}
