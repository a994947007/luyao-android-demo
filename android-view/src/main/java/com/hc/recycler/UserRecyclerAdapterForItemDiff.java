package com.hc.recycler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_view.R;
import com.hc.recycler.refresh.UserRecyclerItemCallback;
import com.hc.recycler.refresh.UserViewHold;
import com.jny.android.demo.base_util.TextUtils;

import java.util.List;

public class UserRecyclerAdapterForItemDiff extends RecyclerView.Adapter<UserViewHold> implements RecyclerAdapter<UserModel> {

    private final AsyncListDiffer<UserModel> asyncListDiffer;

    public UserRecyclerAdapterForItemDiff() {
        asyncListDiffer = new AsyncListDiffer<>(this, new UserRecyclerItemCallback());
    }

    @NonNull
    @Override
    public UserViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item, parent, false);
        return new UserViewHold(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHold holder, int position) {
        holder.bindData(asyncListDiffer.getCurrentList().get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHold holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }
        Bundle bundle = (Bundle) payloads.get(0);
        String username = (String) bundle.get("username");
        if (!TextUtils.isEmpty(username)) {
            holder.setUsername(username);
        }
        String avatar = (String) bundle.get("avatar");
        if (!TextUtils.isEmpty(avatar)) {
            holder.setAvatar(avatar);
        }
        String msg = (String) bundle.get("msg");
        if (!TextUtils.isEmpty(msg)) {
            holder.setMsg(msg);
        }
    }

    @Override
    public int getItemCount() {
        return asyncListDiffer.getCurrentList().size();
    }

    @Override
    public void setData(List<UserModel> list) {
        asyncListDiffer.submitList(list);
    }

    @Override
    public List<UserModel> getData() {
        return asyncListDiffer.getCurrentList();
    }
}
