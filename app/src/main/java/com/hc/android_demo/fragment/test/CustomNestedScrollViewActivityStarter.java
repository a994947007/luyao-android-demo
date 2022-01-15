package com.hc.android_demo.fragment.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.fragment.content.CustomNestedScrollViewFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class CustomNestedScrollViewActivityStarter implements ActivityStarter {
    @Override
    public String getId() {
        return FragmentConstants.CUSTOM_NESTED_SCROLL_VIEW_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new CustomNestedScrollViewFragment();
    }
}
