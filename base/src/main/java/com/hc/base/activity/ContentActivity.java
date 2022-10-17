package com.hc.base.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.hc.base.R;
import com.jny.android.demo.RouterBean;
import com.jny.android.demo.arouter_api.RouterManager;

public class ContentActivity extends LuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        doBindView();
        bindFragment();
    }

    private void bindFragment() {
        Intent intent = getIntent();
        String fragmentId = intent.getStringExtra(Constants.CONTENT_FRAGMENT_KEY);
        RouterBean routerBean = RouterManager.getInstance().loadRouterBean("/app/" + fragmentId);
        Fragment fragment = null;
        if (routerBean != null) {
            try {
                fragment = (Fragment) routerBean.getMyClass().newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        if (fragment == null) {
            fragment = ContentActivityManager.getInstance().get(fragmentId);
        }
        fragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void doBindView() { }
}