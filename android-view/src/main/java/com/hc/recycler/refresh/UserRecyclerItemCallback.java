package com.hc.recycler.refresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.hc.recycler.UserModel;

public class UserRecyclerItemCallback extends DiffUtil.ItemCallback<UserModel> {
    @Override
    public boolean areItemsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
        return super.getChangePayload(oldItem, newItem);
    }
}
