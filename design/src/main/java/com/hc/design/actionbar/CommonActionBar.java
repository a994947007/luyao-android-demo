package com.hc.design.actionbar;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.hc.design.R;

public class CommonActionBar {
    private View actionBar;
    private ImageView leftImageView;
    private ImageView rightImageView;
    private TextView titleView;

    public void addActionBar(ViewGroup container) {
        actionBar = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_common_action_bar, container, false);
        leftImageView = actionBar.findViewById(R.id.left);
        rightImageView = actionBar.findViewById(R.id.right);
        titleView = actionBar.findViewById(R.id.center);
        container.addView(actionBar, 0);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setLeftIcon(Drawable drawable) {
        leftImageView.setImageDrawable(drawable);
    }

    public void setLeftIcon(@DrawableRes int resId) {
        leftImageView.setImageResource(resId);
    }

    public void setLeftClickListener(View.OnClickListener onClickListener) {
        leftImageView.setOnClickListener(onClickListener);
    }

    public void setRightIcon(Drawable drawable) {
        rightImageView.setImageDrawable(drawable);
    }

    public void setRightIcon(@DrawableRes int resId) {
        rightImageView.setImageResource(resId);
    }

    public void setRightClickListener(View.OnClickListener onClickListener) {
        rightImageView.setOnClickListener(onClickListener);
    }
}
