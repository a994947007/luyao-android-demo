package com.hc.android_demo.fragment.content.mvvm.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.mvvm.model.UserModel;
import com.jny.android.demo.base_util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerUserListAdapter extends RecyclerView.Adapter<UserItemViewHolder> {

    private final List<UserModel> mItems = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<UserModel> modelList) {
        mItems.clear();
        mItems.addAll(modelList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_item_user_mvvm_layout);
        return new UserItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
