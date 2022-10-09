package com.hc.my_views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.hc.util.ViewUtils;

import java.util.List;

public class SlideCardView extends RecyclerView {
    private static final int MAX_SHOW_COUNT = 4;    // 最多显示4张
    private static final int TRANSLATION_Y = ViewUtils.dp2px(10);
    private static final float SCALE = 0.05f;

    public SlideCardView(@NonNull Context context) {
        this(context, null);
    }

    public SlideCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutManager(new SlideLayoutManager());
        SlideCallBack slideCallBack = new SlideCallBack();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(slideCallBack);
        itemTouchHelper.attachToRecyclerView(this);
    }

    private class SlideCallBack extends ItemTouchHelper.SimpleCallback {
        public SlideCallBack() {
            super(0, ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN);
        }

        // 拖拽视图
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder target) {
            return false;
        }

        // 滑动视图
        @Override
        public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {
            Adapter<ViewHolder> adapter = (Adapter<ViewHolder>) getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                List items = adapter.getItems();
                if (items.size() > 1) {
                    Object remove = items.remove(viewHolder.getLayoutPosition());
                    items.add(0, remove);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            double maxDistance = recyclerView.getWidth() * 0.2f;
            double distance = Math.sqrt(dX * dX + dY * dY);
            double fraction = distance / maxDistance;
            if (fraction > 1) {
                fraction = 1;
            }
            int itemCount = recyclerView.getChildCount();
            for (int i = 0; i < itemCount; i++) {
                View view = recyclerView.getChildAt(i);
                int level = itemCount - i - 1;
                if (level > 0) {
                    if (level < MAX_SHOW_COUNT - 1) {
                        view.setTranslationY((float) (TRANSLATION_Y * level - fraction * TRANSLATION_Y));
                        view.setScaleX((float) (1 - SCALE * level + fraction * SCALE));
                        view.setScaleY((float) (1 - SCALE * level + fraction * SCALE));
                    }
                }
            }
        }
    }

    public static abstract class Adapter<HV extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<HV> {
        @Override
        public int getItemCount() {
            return getItems().size();
        }

        public abstract List<?> getItems();
    }
}
