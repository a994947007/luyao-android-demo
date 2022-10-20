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
import com.hc.android_demo.fragment.content.framework.presenter.RoomDBPresenter;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.LuFragment;
import com.hc.support.mvps.Presenter;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.ROOM_DB_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class RoomDBFragment extends LuFragment {

    private Presenter mGroupPresenter = new Presenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_db_text_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupPresenter.add(new RoomDBPresenter());
        mGroupPresenter.create(view);
        mGroupPresenter.bind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGroupPresenter.destroy();
    }
}
