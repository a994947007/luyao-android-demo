package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.LuFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.BACKGROUND_TINT_TEST_FRAGMENT,
        group = FragmentConstants.CUSTOM_VIEW)
public class BackgroundMutateTestFragment extends LuFragment{

    private Button mButton;
    private Button mButton2;
    private View view1;
    private View view2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_tint_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButton = view.findViewById(R.id.test);
        mButton2 = view.findViewById(R.id.test2);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);

        mButton.setOnClickListener(v -> view1.getBackground().setAlpha(0));
        mButton2.setOnClickListener(v -> view1.getBackground().mutate().setAlpha(0));
    }
}
