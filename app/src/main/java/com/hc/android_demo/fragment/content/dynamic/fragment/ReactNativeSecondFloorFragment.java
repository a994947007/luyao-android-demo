package com.hc.android_demo.fragment.content.dynamic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.ui.fragment.SecondFloorRefreshLayoutFragment;
import com.hc.android_demo.fragment.content.ui.presenter.SecondFloorOnSlideTipPresenter;
import com.hc.android_demo.fragment.content.ui.presenter.SecondFloorRNContainerPresenter;
import com.hc.base.activity.ActivityStarter;
import com.hc.support.mvps.Presenter;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.REACT_NATIVE_SECOND_FLOOR,
        group = FragmentConstants.APP_DYNAMIC)
public class ReactNativeSecondFloorFragment extends SecondFloorRefreshLayoutFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rn_second_floor_refresh_layout, container, false);
    }

    @Override
    protected void addPresenters(Presenter presenterGroup) {
        presenterGroup.add(new SecondFloorRNContainerPresenter());
        presenterGroup.add(new SecondFloorOnSlideTipPresenter());
    }
}
