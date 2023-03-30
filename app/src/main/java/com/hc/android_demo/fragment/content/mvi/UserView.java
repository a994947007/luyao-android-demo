package com.hc.android_demo.fragment.content.mvi;

import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hc.android_demo.R;
import com.hc.support.mvi.IView;
import com.hc.support.mvi.MultiState2;

public class UserView implements IView<UserViewModel> {

    private TextView usernameTv;
    private SimpleDraweeView imageView;

    @Override
    public void onCreateView(View rootView) {
        imageView = rootView.findViewById(R.id.header_iv);
        usernameTv = rootView.findViewById(R.id.user_name_tv);
    }

    @Override
    public void onObserveData(UserViewModel userViewModel) {
        userViewModel.uiState
                .get(user -> MultiState2.of(user.url, user.username))
                .observe(t -> {
                    imageView.setImageURI(t.t1);
                    usernameTv.setText(t.t2);
                } );
    }
}
