package com.hc.recycler.refresh;

import androidx.recyclerview.widget.DiffUtil;

import com.hc.recycler.UserModel;
import com.jny.android.demo.base_util.TextUtils;

import java.util.List;

public class UserRecyclerDiffCallback extends DiffUtil.Callback {

    protected final List<UserModel> oldItems;
    protected final List<UserModel> newItems;

    public UserRecyclerDiffCallback(List<UserModel> oldItems, List<UserModel> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        UserModel oldItem = oldItems.get(oldItemPosition);
        UserModel newItem = newItems.get(newItemPosition);
        return oldItem == newItem || TextUtils.equals(oldItem.id, newItem.id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        UserModel oldItem = oldItems.get(oldItemPosition);
        UserModel newItem = newItems.get(newItemPosition);
        return TextUtils.equals(oldItem.avatar, newItem.avatar)
                && TextUtils.equals(oldItem.username, newItem.username)
                && TextUtils.equals(oldItem.msg, newItem.msg);
    }
}
