package com.hc.nested_recycler_fragment;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_view.R;
import com.jny.android.demo.base_util.InflaterUtils;

public class ViewFragment extends Fragment {
    private int tag;

    public ViewFragment(int tag) {
        this.tag = tag;
    }

    public ViewFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_view, container, false);
        inflate.setTag(tag);
        RecyclerView recyclerView = inflate.findViewById(R.id.fragment_recyclerView);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(new HeaderAdapter());
        return inflate;
    }

    private static class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.HeaderHolder>{
        private static final String [] itemValues = {
                "header1", "header2", "header3", "header4", "header5", "header6", "header7", "header8",
                "header8", "header10", "header11", "header12", "header13", "header14", "header15", "header16",
                "header8", "header10", "header11", "header12", "header13", "header14", "header15", "header16",
                "header8", "header10", "header11", "header12", "header13", "header14", "header15", "header16"
        };

        @Override
        public HeaderAdapter.HeaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View recyclerItemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_header_item);
            return new HeaderAdapter.HeaderHolder(recyclerItemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HeaderAdapter.HeaderHolder holder, int position) {
            TextView recyclerItemTextView = holder.itemView.findViewById(R.id.recycler_item);
            recyclerItemTextView.setText(itemValues[position]);
        }

        @Override
        public int getItemCount() {
            return itemValues.length;
        }

        private static class HeaderHolder extends RecyclerView.ViewHolder {
            public HeaderHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}