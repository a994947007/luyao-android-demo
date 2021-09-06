package com.hc.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.android_view.R;

import java.util.ArrayList;
import java.util.List;

public class MoreTypeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private final List<MoreTypeBean> dataList = new ArrayList<>();
    {
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img1, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img1, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img1, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img1, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img1, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_2, 0, "分组title"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img2, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img2, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img2, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img2, "哈哈哈"));
        dataList.add(new MoreTypeBean(MoreTypeBean.TYPE_1, R.drawable.img2, "哈哈哈"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_type);

        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerAdapter adapter = new RecyclerAdapter(dataList);
        mRecyclerView.setAdapter(adapter);
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final List<MoreTypeBean> dataList;

        private RecyclerAdapter(List<MoreTypeBean> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case MoreTypeBean.TYPE_1:
                    View view1 = LayoutInflater.from(MoreTypeActivity.this).inflate(R.layout.recycler_item_more_type_1, parent, false);
                    return new RecyclerHolderType1(view1);
                case MoreTypeBean.TYPE_2:
                    View view2 = LayoutInflater.from(MoreTypeActivity.this).inflate(R.layout.recycler_item_more_type_2, parent, false);
                    return new RecyclerHolderType2(view2);
            }
            View view1 = LayoutInflater.from(MoreTypeActivity.this).inflate(R.layout.recycler_item_more_type_1, parent, false);
            return new RecyclerHolderType1(view1);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RecyclerHolderType1) {
                ((RecyclerHolderType1) holder).setData(dataList.get(position));
            } else if (holder instanceof RecyclerHolderType2){
                ((RecyclerHolderType2) holder).setData(dataList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return dataList.get(position).type;
        }
    }

    private static class RecyclerHolderType1 extends RecyclerView.ViewHolder {

        public RecyclerHolderType1(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(MoreTypeBean itemBean) {
            TextView title = itemView.findViewById(R.id.title);
            ImageView icon = itemView.findViewById(R.id.icon);
            title.setText(itemBean.title);
            icon.setImageResource(itemBean.icon);
        }
    }

    private static class RecyclerHolderType2 extends RecyclerView.ViewHolder {

        public RecyclerHolderType2(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(MoreTypeBean itemBean) {
            TextView title = itemView.findViewById(R.id.title);
            title.setText(itemBean.title);
        }
    }
}