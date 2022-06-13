package com.hc.android_demo.fragment.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.drawable.SwipeShadow;
import com.hc.util.ViewUtils;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class SwipeGuideFragment extends Fragment implements ActivityStarter {

    private View mShadowLayout;

    private TranslateAnimation translateAnimation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_guide_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShadowLayout = view.findViewById(R.id.swipe_shadow);
        mShadowLayout.setBackgroundDrawable(new SwipeShadow());
        translateAnimation = new TranslateAnimation(0, -ViewUtils.dp2px(59f), 0, 0);
        translateAnimation.setDuration(2000);
        mShadowLayout.setAnimation(translateAnimation);
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.SWIPE_GUIDE_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new SwipeGuideFragment();
    }
}
