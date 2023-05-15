package com.hc.android_demo.fragment.content.dynamic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.base.fragment.LuFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.DYNAMIC_WRAP_FRAGMENT_VIEW_ID,
        group = FragmentConstants.APP_DYNAMIC)
public class DynamicWrapViewFragment extends LuFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_wrap_view_content, container, false);
        ViewGroup containerView = (ViewGroup) LayoutInflater.from(contentView.getContext()).inflate(R.layout.fragment_scrollview_content, null, false);
        containerView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        containerView.addView(contentView);
        return containerView;
    }
}
