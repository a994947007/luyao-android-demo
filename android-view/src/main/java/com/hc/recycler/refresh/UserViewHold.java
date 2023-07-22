package com.hc.recycler.refresh;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_view.R;
import com.hc.recycler.UserModel;

public class UserViewHold extends RecyclerView.ViewHolder {

    private final SimpleDraweeView avatarView;
    private final TextView usernameTv;
    private final TextView msgTv;

    public UserViewHold(@NonNull View itemView) {
        super(itemView);
        avatarView = itemView.findViewById(R.id.avatar);
        usernameTv = itemView.findViewById(R.id.usernameTv);
        msgTv = itemView.findViewById(R.id.msgTv);
    }

    public void bindData(UserModel userModel) {
        avatarView.setImageURI(userModel.avatar);
        usernameTv.setText(userModel.username);
        msgTv.setText(userModel.msg);
    }

    public void setAvatar(String avatar) {
        avatarView.setImageURI(avatar);
    }

    public void setUsername(String username) {
        usernameTv.setText(username);
    }

    public void setMsg(String msg) {
        msgTv.setText(msg);
    }
}
