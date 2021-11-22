package com.hc.android_demo.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.android_demo.R;
import com.hc.util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class VerticalDrawerRecyclerViewActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_drawer_recycler_view);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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