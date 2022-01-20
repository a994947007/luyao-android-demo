package com.hc.android_demo.fragment.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.fragment.content.FishFragment;
import com.hc.base.activity.ActivityStarter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class FishActivityStarter implements ActivityStarter {
    @Override
    public String getStarterId() {
        return FragmentConstants.FISH_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new FishFragment();
    }
}
