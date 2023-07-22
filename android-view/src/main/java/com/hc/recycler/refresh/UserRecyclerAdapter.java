package com.hc.recycler.refresh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_view.R;
import com.hc.recycler.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserViewHold> {

    private final List<UserModel> userModelList = new ArrayList<>();

    @NonNull
    @Override
    public UserViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item, parent, false);
        return new UserViewHold(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHold holder, int position) {
        holder.bindData(userModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.userModelList.size();
    }

    public void setData(List<UserModel> userModelList) {
        this.userModelList.clear();
        this.userModelList.addAll(userModelList);
    }
}
