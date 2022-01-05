package com.hc.android_demo.activity.test.framework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.android_demo.R;
import com.hc.support.mvps.NameParam;
import com.hc.support.mvps.Presenter;
import com.hc.util.ToastUtils;

public class MvpsActivity extends AppCompatActivity {

    private static final String MVPS_CUSTOM_KEY = "MVPS_CUSTOM_KEY";

    private View rootView;
    private Presenter mPresenter = new Presenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvps);

        rootView = findViewById(R.id.contentView);
        mPresenter.add(new CustomPresenter());
        mPresenter.create(rootView);
        mPresenter.bind(new NameParam(MVPS_CUSTOM_KEY, "测试"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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