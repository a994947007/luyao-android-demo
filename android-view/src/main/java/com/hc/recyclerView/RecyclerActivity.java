package com.hc.recyclerView;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.hc.android_view.R;
import com.hc.recyclerView.listView.RecyclerGridView;
import com.hc.recyclerView.listView.RecyclerItemDecorationListView;
import com.hc.recyclerView.listView.RecyclerListView;
import com.hc.recyclerView.listView.RecyclerStaggerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerListView recyclerListView;
    RecyclerGridView recyclerGridView;
    RecyclerStaggerView recyclerStaggerView;
    RecyclerItemDecorationListView recyclerItemDecorationListView;
    List<ItemBean> itemBeans = new ArrayList<ItemBean>();

    {
        itemBeans.add(new ItemBean("123", R.drawable.img1));
        itemBeans.add(new ItemBean("124", R.drawable.img2));
        itemBeans.add(new ItemBean("125", R.drawable.img3));
        itemBeans.add(new ItemBean("126", R.drawable.img4));
        itemBeans.add(new ItemBean("127", R.drawable.img5));
        itemBeans.add(new ItemBean("228", R.drawable.img6));
        itemBeans.add(new ItemBean("229", R.drawable.img7));
        itemBeans.add(new ItemBean("230", R.drawable.img8));
        itemBeans.add(new ItemBean("231", R.drawable.img9));
        itemBeans.add(new ItemBean("232", R.drawable.img1));
        itemBeans.add(new ItemBean("233", R.drawable.img1));
        itemBeans.add(new ItemBean("234", R.drawable.img1));
        itemBeans.add(new ItemBean("335", R.drawable.img1));
        itemBeans.add(new ItemBean("336", R.drawable.img1));
        itemBeans.add(new ItemBean("337", R.drawable.img1));
        itemBeans.add(new ItemBean("338", R.drawable.img1));
        itemBeans.add(new ItemBean("339", R.drawable.img1));
        itemBeans.add(new ItemBean("340", R.drawable.img1));
        itemBeans.add(new ItemBean("441", R.drawable.img1));
        itemBeans.add(new ItemBean("442", R.drawable.img1));
        itemBeans.add(new ItemBean("443", R.drawable.img1));
        itemBeans.add(new ItemBean("444", R.drawable.img1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        this.mRecyclerView = findViewById(R.id.recycler_view);
        this.recyclerListView = new RecyclerListView(this, mRecyclerView, itemBeans);
        this.recyclerListView.show();
        this.recyclerGridView = new RecyclerGridView(this, mRecyclerView, itemBeans);
        this.recyclerStaggerView = new RecyclerStaggerView(this, mRecyclerView, itemBeans);
        this.recyclerItemDecorationListView = new RecyclerItemDecorationListView(this, mRecyclerView, itemBeans);
        setRecyclerAvailable(LIST);

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            // 这里一般通过一个异步线程去服务端拉取数据
            ItemBean itemBean = new ItemBean("145", R.drawable.img2);
            itemBeans.add(0, itemBean);
            // 通过Handler切换到主线程刷新UI
            new Handler(Looper.getMainLooper()).post(() -> {
                if (recyclerListView.available()) {
                    recyclerListView.notifyDataSetChanged();
                }
                if (recyclerGridView.available()) {
                    recyclerGridView.notifyDataSetChanged();
                }
                if (recyclerStaggerView.available()) {
                    recyclerStaggerView.notifyDataSetChanged();
                }
                if (recyclerItemDecorationListView.available()) {
                    recyclerItemDecorationListView.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            });
        });

        recyclerListView.setRefreshListener(holder -> {
            // 这里应该异步去加载，然后通过Handler切换到Main线程，这里就不模拟了，同上
            new Handler(Looper.getMainLooper()).postDelayed(() -> { // 需要放到Handler中，因为recycler view在滑动的过程中可能处于锁定状态
                if (new Random().nextBoolean()) {
                    itemBeans.add(new ItemBean("145", R.drawable.img2));
                    recyclerListView.notifyDataSetChanged();
                    holder.update(RecyclerListView.RecyclerLoadHolder.NORMAL);
                } else {
                    holder.update(RecyclerListView.RecyclerLoadHolder.LOAD_FAIlER);
                }

            },1000);

        });
    }

    private static final int LIST = 0;
    private static final int GRID = 1;
    private static final int STAGGER = 2;
    private static final int ITEM_DECORATION = 3;

    private void setRecyclerAvailable(int type) {
        switch (type) {
            case LIST:
                recyclerListView.setAvailable(true);
                recyclerGridView.setAvailable(false);
                recyclerStaggerView.setAvailable(false);
                recyclerItemDecorationListView.setAvailable(false);
                break;
            case GRID:
                recyclerListView.setAvailable(false);
                recyclerGridView.setAvailable(true);
                recyclerStaggerView.setAvailable(false);
                recyclerItemDecorationListView.setAvailable(false);
                break;
            case STAGGER:
                recyclerListView.setAvailable(false);
                recyclerGridView.setAvailable(false);
                recyclerStaggerView.setAvailable(true);
                recyclerItemDecorationListView.setAvailable(false);
                break;
            case ITEM_DECORATION:
                recyclerListView.setAvailable(false);
                recyclerGridView.setAvailable(false);
                recyclerStaggerView.setAvailable(false);
                recyclerItemDecorationListView.setAvailable(true);
                break;
        }
    }

    /**
     * 用于构建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单被选中
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.list_view_horizontal) {
            recyclerListView.setOrientation(false, false);
            recyclerListView.show();
            setRecyclerAvailable(LIST);
        } else if (itemId == R.id.list_view_horizontal_reverse) {
            recyclerListView.setOrientation(false, true);
            recyclerListView.show();
            setRecyclerAvailable(LIST);
        } else if (itemId == R.id.list_view_vertical) {
            recyclerListView.setOrientation(true, false);
            recyclerListView.show();
            setRecyclerAvailable(LIST);
        } else if (itemId == R.id.list_view_vertical_reverse) {
            recyclerListView.setOrientation(true, true);
            recyclerListView.show();
            setRecyclerAvailable(LIST);
        } else if (itemId == R.id.grid_view_horizontal) {
            recyclerGridView.setOrientation(false, false);
            recyclerGridView.show();
            setRecyclerAvailable(GRID);
        } else if (itemId == R.id.grid_view_horizontal_reverse) {
            recyclerGridView.setOrientation(false, true);
            recyclerGridView.show();
            setRecyclerAvailable(GRID);
        } else if (itemId == R.id.grid_view_vertical) {
            recyclerGridView.setOrientation(true, false);
            recyclerGridView.show();
            setRecyclerAvailable(GRID);
        } else if (itemId == R.id.grid_view_vertical_reverse) {
            recyclerGridView.setOrientation(true, true);
            recyclerGridView.show();
            setRecyclerAvailable(GRID);
        } else if (itemId == R.id.stagger_view_horizontal) {
            recyclerStaggerView.setOrientation(false, false);
            recyclerStaggerView.show();
            setRecyclerAvailable(STAGGER);
        } else if (itemId == R.id.stagger_view_horizontal_reverse) {
            recyclerStaggerView.setOrientation(false, true);
            recyclerStaggerView.show();
            setRecyclerAvailable(STAGGER);
        } else if (itemId == R.id.stagger_view_vertical) {
            recyclerStaggerView.setOrientation(true, false);
            recyclerStaggerView.show();
            setRecyclerAvailable(STAGGER);
        } else if (itemId == R.id.stagger_view_vertical_reverse) {
            recyclerStaggerView.setOrientation(true, true);
            recyclerStaggerView.show();
            setRecyclerAvailable(STAGGER);
        } else if (itemId == R.id.more_type_view) {
            Intent intent = new Intent(this, MoreTypeActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.list_view_item_decoration) {
            recyclerItemDecorationListView.setOrientation(false, true);
            recyclerItemDecorationListView.show();
            setRecyclerAvailable(ITEM_DECORATION);
        }
        return super.onOptionsItemSelected(item);
    }
}