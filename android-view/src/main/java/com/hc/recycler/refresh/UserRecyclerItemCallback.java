package com.hc.recycler.refresh;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.hc.recycler.UserModel;
import com.jny.android.demo.base_util.TextUtils;

public class UserRecyclerItemCallback extends DiffUtil.ItemCallback<UserModel> {
    @Override
    public boolean areItemsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        return oldItem == newItem || TextUtils.equals(oldItem.id, newItem.id);
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        return TextUtils.equals(oldItem.avatar, newItem.avatar)
                && TextUtils.equals(oldItem.username, newItem.username)
                && TextUtils.equals(oldItem.msg, newItem.msg);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        Bundle bundle = new Bundle();
        if (!TextUtils.equals(oldItem.username, newItem.username)) {
            bundle.putString("username", newItem.username);
        }
        if (!TextUtils.equals(oldItem.avatar, newItem.avatar)) {
            bundle.putString("avatar", newItem.avatar);
        }
        if (!TextUtils.equals(oldItem.msg, newItem.msg)) {
            bundle.putString("msg", newItem.msg);
        }
        return bundle;
    }
}
