package com.hc.android_demo.fragment.content.mvp.presenter;

import android.view.View;

import com.hc.android_demo.fragment.content.mvp.model.UserModel;
import com.hc.android_demo.fragment.content.mvp.network.ApiService;
import com.hc.android_demo.fragment.content.mvp.view.MvpTestFragment;
import com.hc.android_demo.fragment.content.mvp.view.UserItemView;
import com.hc.support.mvps.Presenter;
import com.jny.android.demo.rxandroid.schedule.Schedules;
import com.hc.support.util.TextUtils;

public class UserPresenter extends Presenter {

    private UserItemView mUserItemView;

    @Override
    protected void doInject() {
        super.doInject();
        mUserItemView = inject(MvpTestFragment.class);
    }

    @Override
    public void create(View rootView) {
        super.create(rootView);
    }

    /**
     * 添加用户的业务处理
     * @param userModel
     */
    public boolean addUser(UserModel userModel) {
        if (TextUtils.isEmpty(userModel.email)
            || TextUtils.isEmpty(userModel.lastName)
            || TextUtils.isEmpty(userModel.firstName)
            || TextUtils.isEmpty(userModel.avatar)) {
            return false;
        }

        ApiService.addUser(userModel);
        mUserItemView.showAddUserSuccess();
        return true;
    }

    @Override
    protected void onBind() {
        super.onBind();
        bindUserList();
    }

    public void bindUserList() {
        addToAutoDispose(ApiService.requestUserListResponse()
                .observeOn(Schedules.MAIN)
                .subscribe(userListResponse -> mUserItemView.updateUserList(userListResponse.userModelList)));
    }
}
