package com.hc.viewPager_fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import com.hc.android_view.R;

public class ConcreteFragment extends LazyFragment2 {
    private View mLoadText;
    private View mContentView;
    private ImageView mImageView;

    @Override
    protected void onBindView(View view) {
        mContentView = view.findViewById(R.id.contentView);
        mLoadText = view.findViewById(R.id.loadText);
        mImageView = view.findViewById(R.id.imageView);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_concrete;
    }

    @Override
    protected void onFragmentLoad() {
        // 获取网络请求或者其他加载
        // 通过Handler模拟网络请求
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mImageView.setImageResource(R.drawable.img1);
            mContentView.setVisibility(View.VISIBLE);
            mLoadText.setVisibility(View.GONE);
        }, 500);
    }
}