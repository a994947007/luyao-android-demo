package com.hc.android_demo.fragment.content.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hc.android_demo.R;
import com.hc.android_demo.databinding.FragmentMvvmLayoutBinding;
import com.hc.android_demo.fragment.content.mvvm.network.ApiService;
import com.hc.android_demo.fragment.content.mvvm.view.RecyclerUserListAdapter;
import com.hc.support.rxJava.schedule.Schedules;

public class MvvmTestFragment extends Fragment {

    private RecyclerUserListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMvvmLayoutBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mvvm_layout, container, false);
        dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new RecyclerUserListAdapter();
        dataBinding.recyclerView.setAdapter(mAdapter);
        ApiService.requestUserListResponse()
                .observeOn(Schedules.MAIN)
                .subscribe(userListResponse -> mAdapter.update(userListResponse.userModelList));
        return dataBinding.getRoot();
    }
}
