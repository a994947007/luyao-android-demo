package com.hc.android_demo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.my_views.SlideCardView;
import com.hc.recyclerView.ItemBean;
import com.hc.util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomLayoutActivity extends AppCompatActivity {

    private SlideCardView mRecyclerView;
    private List<ItemBean> itemBeans = new ArrayList<>();
    {
        itemBeans.add(new ItemBean("aaa", R.drawable.img1));
        itemBeans.add(new ItemBean("bbb", R.drawable.img2));
        itemBeans.add(new ItemBean("ccc", R.drawable.img3));
        itemBeans.add(new ItemBean("ddd", R.drawable.img4));
        itemBeans.add(new ItemBean("eee", R.drawable.img5));
        itemBeans.add(new ItemBean("fff", R.drawable.img6));
        itemBeans.add(new ItemBean("ggg", R.drawable.img7));
        itemBeans.add(new ItemBean("hhh", R.drawable.img8));
        itemBeans.add(new ItemBean("uuu", R.drawable.img9));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layout);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new SlideAdapter(itemBeans));
    }

    protected static class RecyclerSlideHolder extends RecyclerView.ViewHolder {
        public RecyclerSlideHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(ItemBean itemBean) {
            TextView textView = itemView.findViewById(com.hc.android_view.R.id.textView);
            textView.setText(itemBean.title);
            ImageView imageView = itemView.findViewById(com.hc.android_view.R.id.icon);
            imageView.setImageResource(itemBean.iconResId);
        }
    }

    private static class SlideAdapter extends SlideCardView.Adapter<RecyclerSlideHolder> {
        private List<ItemBean> itemBeans;

        public SlideAdapter(List<ItemBean> itemBeans) {
            this.itemBeans = itemBeans;
        }

        @Override
        public List<?> getItems() {
            return itemBeans;
        }

        @NonNull
        @Override
        public RecyclerSlideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerSlideHolder(InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_grid_item));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerSlideHolder holder, int position) {
            ItemBean itemBean = itemBeans.get(position);
            holder.setData(itemBean);
        }
    }
}