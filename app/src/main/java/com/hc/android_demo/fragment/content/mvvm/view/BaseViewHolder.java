package com.hc.android_demo.fragment.content.mvvm.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    ViewDataBinding mBinding;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    protected void bind(int bindId, T model) {
        mBinding.setVariable(bindId, model);
        mBinding.executePendingBindings();
    }
}
