package com.hc.android_demo.fragment.content.mvvm.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.BR;
import com.hc.android_demo.fragment.content.mvvm.model.UserModel;

public class UserItemViewHolder extends BaseViewHolder<UserModel>{
    public UserItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setData(UserModel userModel) {
        bind(BR.user, userModel);
    }

    @BindingAdapter("loadImgUrl")
    public static void setImgUrl(SimpleDraweeView imageView, String url) {
        imageView.setImageURI(url);
    }
}
