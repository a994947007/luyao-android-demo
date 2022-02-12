package com.hc.android_demo.fragment.content.presenter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.mvp.model.UserModel;
import com.hc.android_demo.fragment.content.mvp.network.ApiService;
import com.hc.base.AppEnvironment;
import com.hc.support.mvps.Presenter;
import com.hc.support.rxJava.schedule.Schedules;
import com.hc.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

// https://www.cnblogs.com/wjtaigwh/p/6543354.html
// https://blog.csdn.net/yin13753884368/article/details/78477840
public class SecondFloorPresenter extends Presenter {

    private ViewStub contentViewStub;
    private View contentView;
    private RecyclerView recyclerView;
    private static final int SPAN_COUNT = 4;
    private UserItemsAdapter userItemsAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private TextView bottomDeleteContainer;

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        contentViewStub = rootView.findViewById(R.id.secondContainer);
    }

    @Override
    protected void onBind() {
        super.onBind();
        if (contentView == null) {
            contentView = contentViewStub.inflate();
            recyclerView = contentView.findViewById(R.id.recyclerView);
            bottomDeleteContainer = contentView.findViewById(R.id.bottom_delete_container);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, RecyclerView.VERTICAL, false));
            recyclerView.addItemDecoration(new UserItemDecoration());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mItemTouchHelper = new ItemTouchHelper(new UserItemTouchHelper());
            mItemTouchHelper.attachToRecyclerView(recyclerView);
            userItemsAdapter = new UserItemsAdapter();
            recyclerView.setAdapter(userItemsAdapter);
        }
        bindFetchUsersObserver();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void processDeleteUser(int position) {
        userItemsAdapter.mUserList.remove(position);
        userItemsAdapter.notifyItemRemoved(position);
        userItemsAdapter.notifyItemRangeChanged(position, userItemsAdapter.getItemCount());
    }

    private class UserItemTouchHelper extends ItemTouchHelper.Callback {

        private final Vibrator mVibrator;
        private final ValueAnimator bigScaleAnimator;
        private UserItemViewHolder userItemViewHolder;
        private static final int STATE_MOVING_OR_IDLE = 0;
        private static final int STATE_DELETING = 1;
        private static final int STATE_DELETED = 2;
        private int currentState = STATE_MOVING_OR_IDLE;
        private boolean deleteVibrator = false;

        public UserItemTouchHelper() {
            mVibrator = (Vibrator) AppEnvironment.getAppContext().getSystemService(Context.VIBRATOR_SERVICE) ;//震动
            bigScaleAnimator = new ValueAnimator();
            bigScaleAnimator.setFloatValues(1f, 1.2f);
            bigScaleAnimator.setDuration(200);
            bigScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    userItemViewHolder.userIcon.setScaleX((Float) animation.getAnimatedValue());
                    userItemViewHolder.userIcon.setScaleY((Float) animation.getAnimatedValue());
                }
            });
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            float x = (recyclerView.getLeft() + dX + userItemViewHolder.itemView.getLeft() + (userItemViewHolder.itemView.getRight() - userItemViewHolder.itemView.getLeft()) / 2f);
            float y = (recyclerView.getTop() + dY + userItemViewHolder.itemView.getTop() + (userItemViewHolder.itemView.getBottom() - userItemViewHolder.itemView.getTop()) / 2f);
            Log.d("UserItemTouchHelper", "x:" + dX + ",y:" + dY);
            if (currentState == STATE_DELETED) {
                return;
            }
            if (x >= bottomDeleteContainer.getLeft() && x <= bottomDeleteContainer.getRight() && y >= bottomDeleteContainer.getTop() && y <= bottomDeleteContainer.getBottom()) {
                if (currentState != STATE_DELETING) {
                    currentState = STATE_DELETING;
                    bottomDeleteContainer.setText("松手即可删除");
                    bottomDeleteContainer.setBackgroundColor(Color.BLUE);
                    if (!deleteVibrator) {
                        deleteVibrator = true;
                        mVibrator.vibrate(60);
                    }
                }
            } else {
                if (currentState != STATE_MOVING_OR_IDLE) {
                    deleteVibrator = false;
                    currentState = STATE_MOVING_OR_IDLE;
                    bottomDeleteContainer.setText("删除");
                    bottomDeleteContainer.setBackgroundColor(Color.RED);
                }
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                userItemViewHolder = (UserItemViewHolder) viewHolder;
                mVibrator.vibrate(60);
                userItemViewHolder.usernameTv.setVisibility(View.GONE);
                bigScaleAnimator.start();
                bottomDeleteContainer.setText("删除");
                bottomDeleteContainer.setBackgroundColor(Color.RED);
                bottomDeleteContainer.setVisibility(View.VISIBLE);
                currentState = STATE_MOVING_OR_IDLE;
                deleteVibrator = false;
            } else {
                if (currentState == STATE_DELETING) {
                    int adapterPosition = userItemViewHolder.getAdapterPosition();
                    processDeleteUser(adapterPosition);
                    currentState = STATE_DELETED;
                    userItemViewHolder.itemView.setVisibility(View.GONE);
                } else {
                    bigScaleAnimator.reverse();
                }
                bottomDeleteContainer.setVisibility(View.GONE);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            UserItemViewHolder itemViewHolder = (UserItemViewHolder) viewHolder;
            itemViewHolder.usernameTv.setVisibility(View.VISIBLE);
        }
    }

    private void bindFetchUsersObserver() {
        addToAutoDispose(ApiService.requestUserListResponse()
                .observeOn(Schedules.MAIN)
                .subscribe(userListResponse -> userItemsAdapter.setData(userListResponse.userModelList)));
    }

    private static class UserItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int childLayoutPosition = parent.getChildLayoutPosition(view);
            if (childLayoutPosition / SPAN_COUNT > 0) {
                outRect.top = ViewUtils.dp2px(18);
            }
        }
    }

    private class UserItemsAdapter extends RecyclerView.Adapter<UserItemViewHolder>{
        private final List<UserModel> mUserList = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged")
        public void setData(List<UserModel> userList) {
            mUserList.clear();
            mUserList.addAll(userList);
            notifyDataSetChanged();
        }

        public List<UserModel> getDataList() {
            return mUserList;
        }

        @NonNull
        @Override
        public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item_layout, parent, false);
            return new UserItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
            holder.setData(mUserList.get(position));
        }

        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }

    private class UserItemViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView userIcon;
        private final TextView usernameTv;
        private boolean isRemoved = false;

        public UserItemViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.userIcon);
            usernameTv = itemView.findViewById(R.id.user_name_tv);
        }

        public void setData(UserModel user) {
            userIcon.setImageURI(user.avatar);
            usernameTv.setText(user.lastName);
        }
    }


}
