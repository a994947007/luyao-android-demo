package com.hc.android_demo.fragment.content.framework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.mvi.UserPresenter;
import com.hc.support.mvi.ActionBus;
import com.hc.support.mvps.Presenter;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.MVI_TEST_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class MVITestFragment extends Fragment {

    private final Presenter presenterGroup = new Presenter();
    private final ActionBus actionBus = new ActionBus(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mvi_test_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreatePresenter(presenterGroup);
        presenterGroup.create(view);
        presenterGroup.bind(this, actionBus);
    }

    protected void onCreatePresenter(Presenter presenter) {
        presenter.add(new UserPresenter(this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterGroup.destroy();
    }
}
