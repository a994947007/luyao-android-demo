package com.hc.android_demo.fragment.content.mvp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.mvp.model.UserModel;
import com.hc.android_demo.fragment.content.mvp.presenter.UserPresenter;
import com.hc.util.ToastUtils;
import com.jny.android.demo.base_util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class MvpTestFragment extends Fragment implements UserItemView{

    private RecyclerView mRecyclerView;
    private UserRecyclerAdapter mAdapter;
    private UserPresenter mUserPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mvp_test_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new UserRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mUserPresenter = new UserPresenter();
        mUserPresenter.create(view);
        mUserPresenter.bind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUserPresenter.destroy();
    }

    @Override
    public void updateUserList(List<UserModel> userModelList) {
        mAdapter.update(userModelList);
    }

    @Override
    public void showAddUserSuccess() {
        ToastUtils.show("添加成功", Toast.LENGTH_LONG);
        // 其他UI逻辑
    }

    private static class UserRecyclerAdapter extends RecyclerView.Adapter<UserViewHolder>{

        private final List<UserModel> mItems = new ArrayList<>();

        public UserRecyclerAdapter(List<UserModel> items) {
            mItems.addAll(items);
        }

        public UserRecyclerAdapter() { }

        @SuppressLint("NotifyDataSetChanged")
        public void update(List<UserModel> items) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_item_user_layout);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            holder.setData(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView mAvatarIv;
        private final TextView mUserNameTv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarIv = itemView.findViewById(R.id.user_avatar_iv);
            mUserNameTv = itemView.findViewById(R.id.user_name_tv);
        }

        public void setData(UserModel userModel) {
            mAvatarIv.setImageURI(userModel.avatar);
            mUserNameTv.setText(userModel.lastName);
        }
    }

}
