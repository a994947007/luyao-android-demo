package com.hc.android_demo.fragment.content.framework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.framework.util.ThemeUtils;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.LuFragment;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class DynamicThemeFragment extends LuFragment implements ActivityStarter {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(ThemeUtils.getInstance().getStyle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dynamic_theme_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.switch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeUtils.getInstance().setStyle(R.style.activity_style1);
                getActivity().recreate();
            }
        });
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.DYNAMIC_THEME_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new DynamicThemeFragment();
    }
}
