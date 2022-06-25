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
import com.hc.android_demo.fragment.content.framework.presenter.DSLTestHTMLPresenter;
import com.hc.android_demo.fragment.content.framework.presenter.DSLTestPersonPresenter;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.BaseFragment;
import com.hc.support.mvps.Presenter;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class DSLTestFragment extends BaseFragment implements ActivityStarter {

    private final Presenter presenterGroup = new Presenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dsl_test_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreatePresenter(presenterGroup);
        presenterGroup.create(view);
        presenterGroup.bind(this);
    }

    protected void onCreatePresenter(Presenter presenter) {
        presenter.add(new DSLTestPersonPresenter());
        presenter.add(new DSLTestHTMLPresenter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterGroup.destroy();
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.KT_DSL_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new DSLTestFragment();
    }
}

