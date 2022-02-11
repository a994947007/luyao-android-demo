package com.hc.android_demo.fragment.content.presenter;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.mvp.model.UserModel;
import com.hc.android_demo.fragment.content.mvp.network.ApiService;
import com.hc.support.mvps.Presenter;
import com.hc.support.rxJava.schedule.Schedules;
import com.hc.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class SecondFloorPresenter extends Presenter {

    private ViewStub contentViewStub;
    private View contentView;
    private RecyclerView recyclerView;
    private static final int SPAN_COUNT = 4;
    private UserItemsAdapter userItemsAdapter;

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
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, RecyclerView.VERTICAL, false));
            recyclerView.addItemDecoration(new UserItemDecoration());
            userItemsAdapter = new UserItemsAdapter();
            recyclerView.setAdapter(userItemsAdapter);
        }
        bindFetchUsersObserver();
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

    private static class UserItemsAdapter extends RecyclerView.Adapter<UserItemViewHolder>{
        private final List<UserModel> mUserList = new ArrayList<>();

        @SuppressLint("NotifyDataSetChanged")
        public void setData(List<UserModel> userList) {
            mUserList.clear();
            mUserList.addAll(userList);
            notifyDataSetChanged();
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

    private static class UserItemViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView userIcon;
        private final TextView usernameTv;

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
