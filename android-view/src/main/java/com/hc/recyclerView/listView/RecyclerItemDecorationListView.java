package com.hc.recyclerView.listView;

import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.android_view.R;
import com.hc.recyclerView.ItemBean;
import com.hc.util.ViewUtils;

import java.util.List;

public class RecyclerItemDecorationListView extends BaseRecyclerView{

    private int headerHeight = 50;
    private Paint paint;
    private Paint textPaint;
    private Rect rect;
    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildLayoutPosition(view);
            if (Helper.isHeader(itemBeans, position)) {
                outRect.set(0, ViewUtils.dp2px(headerHeight), 0, 0);
            } else {
                outRect.set(0, ViewUtils.dp2px(1), 0, 0);
            }
        }

        // 先绘制outRect，后绘制itemView，如果没有绘制在outRect区域，会被itemView挡住
        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int count = parent.getChildCount();     // 拿到可见的child个数
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                if (view.getTop() - ViewUtils.dp2px(headerHeight) - parent.getPaddingTop() >= 0) {
                    int position = parent.getChildLayoutPosition(view);
                    boolean isHeader = Helper.isHeader(itemBeans, position);
                    if (isHeader) {
                        c.drawRect(left, view.getTop() - ViewUtils.dp2px(headerHeight), right, view.getTop(), paint);
                        String headerString = Helper.getHeaderString(itemBeans, position);
                        textPaint.getTextBounds(headerString, 0, headerString.length(), rect);
                        c.drawText(headerString, (right - left) / 2 - rect.width() / 2, view.getTop() - ViewUtils.dp2px(headerHeight) / 2 + rect.height() / 2f, textPaint);
                    } else {
                        c.drawRect(left, view.getTop() - ViewUtils.dp2px(1), right, view.getTop(), paint);
                    }
                }
            }
        }

        // 先绘制itemView，后绘制outRect，如果绘制在itemView区域，会把itemView挡住
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            LinearLayoutManager layoutManager = (LinearLayoutManager)parent.getLayoutManager();
            if (layoutManager != null) {
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                RecyclerView.ViewHolder item = parent.findViewHolderForAdapterPosition(firstItemPosition);  // 获取可见的第一个元素
                if (item != null) {
                    int left = parent.getPaddingLeft();
                    int right = parent.getWidth() - parent.getPaddingRight();
                    int top = parent.getPaddingTop();
                    String headerString = Helper.getHeaderString(itemBeans, firstItemPosition);
                    if (Helper.isBottom(itemBeans, firstItemPosition)) {    // 如果展示出来是最底部的item，item的item带着header往上
                        c.save();
                        int bottom = Math.min(item.itemView.getBottom() - parent.getPaddingTop(), ViewUtils.dp2px(headerHeight));
                        c.drawRect(left, top, right, top + bottom, paint);
                        textPaint.getTextBounds(headerString, 0, headerString.length(), rect);
                        c.restore();
                        c.save();
                        Rect textRect = new Rect(left, top, right,top + bottom);
                        c.clipRect(textRect);
                        c.drawText(headerString, (right - left) / 2 - rect.width() / 2, top + bottom - ViewUtils.dp2px(headerHeight) / 2 + rect.height() / 2f, textPaint);
                        c.restore();
                    } else {    // 如果被展示出来的item不是最底部的item
                        c.drawRect(left, top, right, top + ViewUtils.dp2px(headerHeight), paint);
                        textPaint.getTextBounds(headerString, 0, headerString.length(), rect);
                        c.drawText(headerString, (right - left) / 2 - rect.width() / 2, top + ViewUtils.dp2px(headerHeight) / 2 + rect.height() / 2f, textPaint);
                    }
                }
            }
        }
    };

    public RecyclerItemDecorationListView(Context context, RecyclerView recyclerView, List<ItemBean> itemBeans) {
        super(context, recyclerView, itemBeans);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(ViewUtils.dp2px(20));
        rect = new Rect();
    }

    @Override
    public void setAvailable(boolean available) {
        super.setAvailable(available);
        if (available) {
            recyclerView.addItemDecoration(itemDecoration);
        } else {
            recyclerView.removeItemDecoration(itemDecoration);
        }
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.recycler_item_decoration_item;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    private static class Helper {
        public static boolean isHeader(List<ItemBean> itemBeans, int position) {
            if (position == 0) {
                return true;
            } else {
                ItemBean itemBean = itemBeans.get(position);
                if (!TextUtils.isEmpty(itemBean.title)) {
                    return !itemBean.title.substring(0, 1).equals(itemBeans.get(position - 1).title.substring(0, 1));
                }
                return false;
            }
        }

        public static boolean isBottom(List<ItemBean> itemBeans, int position) {
            if (itemBeans.isEmpty()) {
                return false;
            }
            if (itemBeans.size() == 1) {
                return true;
            } else {
                ItemBean itemBean = itemBeans.get(position);
                if (!TextUtils.isEmpty(itemBean.title)) {
                    return !itemBean.title.substring(0, 1).equals(itemBeans.get(position + 1).title.substring(0, 1));
                }
                return false;
            }
        }

        public static String getHeaderString(List<ItemBean> itemBeans, int position) {
            ItemBean itemBean = itemBeans.get(position);
            if (!TextUtils.isEmpty(itemBean.title)) {
                return itemBean.title.substring(0, 1);
            }
            return "";
        }
    }
}
