package com.hc.recycler;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.hc.recycler.refresh.UserRecyclerAdapter;
import com.hc.recycler.refresh.UserViewHold;
import com.jny.android.demo.base_util.TextUtils;

import java.util.List;

public class UserRecyclerAdapterForElement extends UserRecyclerAdapter {
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
}
