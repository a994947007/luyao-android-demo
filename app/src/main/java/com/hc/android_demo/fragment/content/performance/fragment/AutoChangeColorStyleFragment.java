package com.hc.android_demo.fragment.content.performance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.LuFragment;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class AutoChangeColorStyleFragment extends LuFragment implements ActivityStarter {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_change_style_layout, container, false);
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.PERFORMANCE_AUTO_CHANGE_STYLE_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new AutoChangeColorStyleFragment();
    }
}
