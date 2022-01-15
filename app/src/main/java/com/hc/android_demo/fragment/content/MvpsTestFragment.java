package com.hc.android_demo.fragment.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.support.mvps.NameParam;
import com.hc.support.mvps.Presenter;
import com.hc.util.ToastUtils;

public class MvpsTestFragment extends Fragment {
    private static final String MVPS_CUSTOM_KEY = "MVPS_CUSTOM_KEY";

    private View rootView;
    private Presenter mPresenter = new Presenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mvps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view.findViewById(R.id.contentView);
        mPresenter.add(new CustomPresenter());
        mPresenter.create(rootView);
        mPresenter.bind(new NameParam(MVPS_CUSTOM_KEY, "测试"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unBind();
        mPresenter.destroy();
    }

    private static class CustomPresenter extends Presenter{
        private TextView mTextView;
        private String content;
        @Override
        public void doBindView(View rootView) {
            super.doBindView(rootView);
            mTextView = rootView.findViewById(R.id.textView);
            ToastUtils.show("doBindView", Toast.LENGTH_SHORT);
        }

        @Override
        protected void doInject() {
            super.doInject();
            content = inject(MVPS_CUSTOM_KEY);
            ToastUtils.show("doInject", Toast.LENGTH_SHORT);
        }

        @Override
        protected void onCreate() {
            super.onCreate();
            ToastUtils.show("onCreate", Toast.LENGTH_SHORT);
        }

        @Override
        protected void onBind() {
            super.onBind();
            mTextView.setText(content);
            ToastUtils.show("onBind", Toast.LENGTH_SHORT);
        }

        @Override
        protected void onUnBind() {
            super.onUnBind();
            ToastUtils.show("onUnBind", Toast.LENGTH_SHORT);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            ToastUtils.show("onDestroy", Toast.LENGTH_SHORT);
        }
    }

}
