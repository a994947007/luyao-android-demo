package com.hc.recycler;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.recycler.refresh.UserRecyclerAdapter;
import com.hc.recycler.refresh.UserRecyclerDiffCallback;

import java.util.ArrayList;
import java.util.List;

public final class RecyclerUserHelper {
    public static void updateAndDiffItem(RecyclerView recyclerView, List<UserModel> list) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new UserRecyclerAdapter();
            recyclerView.setAdapter(adapter);
        }
        if (adapter instanceof RecyclerAdapter) {
            RecyclerAdapter<UserModel> recyclerAdapter = (RecyclerAdapter<UserModel>) adapter;
            List<UserModel> oldItems = recyclerAdapter.getData();
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UserRecyclerDiffCallback(oldItems, list));
            recyclerAdapter.setData(list);
            diffResult.dispatchUpdatesTo(adapter);
        }
    }
}
