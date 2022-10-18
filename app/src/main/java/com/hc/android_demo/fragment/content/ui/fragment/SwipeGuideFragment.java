package com.hc.android_demo.fragment.content.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.drawable.SwipeShadow;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.SWIPE_GUIDE_FRAGMENT_ID,
        group = FragmentConstants.CUSTOM_VIEW)
public class SwipeGuideFragment extends Fragment {

    private ViewPager mViewPage;
    private View mShadowLayout;
    private ValueAnimator translateAnimation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_guide_layout, container, false);
    }

    private int mAnimateX = 0;
    private int mDragX = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPage = view.findViewById(R.id.viewPager);
        mViewPage.setAdapter(new MyPageAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mDragX = -positionOffsetPixels;
                int current = mAnimateX + mDragX;
                mShadowLayout.setTranslationX(current);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) { // start scroll
                    mShadowLayout.setVisibility(View.VISIBLE);
                    translateAnimation.start();
                } else {
                    mShadowLayout.setTranslationX(0);
                    mShadowLayout.setVisibility(View.GONE);
                }
            }
        });
        // 动画相关
        mShadowLayout = view.findViewById(R.id.swipe_shadow);
        mShadowLayout.setBackgroundDrawable(new SwipeShadow());
        translateAnimation = ValueAnimator.ofFloat(0, 1);
        translateAnimation.setDuration(500);
        translateAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                mAnimateX = -(int) (ratio * mShadowLayout.getMeasuredWidth());
                int currentX = (int) (mDragX + mAnimateX);
                mShadowLayout.setTranslationX(currentX);
            }
        });
    }

    private static class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new SwipeGuideItemFragment(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
