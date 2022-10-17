package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.android.demo.base_util.InflaterUtils;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

@ARouter(path = FragmentConstants.VERTICAL_DRAWER_RECYCLERVIEW_TEST_FRAGMENT_ID,
        group = FragmentConstants.CUSTOM_VIEW)
public class VerticalDrawerRecyclerViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private static List<String> items = new ArrayList<>();

    static {
        items.add("123");
        items.add("124");
        items.add("125");
        items.add("321");
        items.add("321");
        items.add("321");
        items.add("321");
        items.add("123");
        items.add("124");
        items.add("125");
        items.add("321");
        items.add("321");
        items.add("321");
        items.add("321");
        items.add("123");
        items.add("124");
        items.add("125");
        items.add("321");
        items.add("321");
        items.add("321");
        items.add("321");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vertical_drawer_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerAdapter(items));
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerTextHolder> {

        private final List<String> items = new ArrayList<>();

        public RecyclerAdapter(List<String> items) {
            this.items.addAll(items);
        }

        @NonNull
        @Override
        public RecyclerTextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_item);
            return new RecyclerTextHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerTextHolder holder, int position) {
            holder.setData(this.items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class RecyclerTextHolder extends RecyclerView.ViewHolder {

        public RecyclerTextHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(String text) {
            TextView textView = itemView.findViewById(R.id.recycler_item_text_view);
            textView.setText(text);
        }
    }
}
