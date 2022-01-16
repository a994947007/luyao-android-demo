package com.hc.android_demo.fragment.content.mvp.view;

import com.hc.android_demo.fragment.content.mvp.model.UserModel;

import java.util.List;

public interface UserItemView {

    void updateUserList(List<UserModel> userModelList);

    void showAddUserSuccess();
}
