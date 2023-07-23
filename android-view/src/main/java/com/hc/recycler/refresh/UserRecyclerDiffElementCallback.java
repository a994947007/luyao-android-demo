package com.hc.recycler.refresh;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hc.recycler.UserModel;
import com.jny.android.demo.base_util.TextUtils;

import java.util.List;

public class UserRecyclerDiffElementCallback extends UserRecyclerDiffCallback{
    public UserRecyclerDiffElementCallback(List<UserModel> oldItems, List<UserModel> newItems) {
        super(oldItems, newItems);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        UserModel oldItem = oldItems.get(oldItemPosition);
        UserModel newItem = newItems.get(newItemPosition);
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
