package com.hc.base.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.hc.base.R;

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
        Fragment fragment = ContentActivityManager.getInstance().get(fragmentId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void doBindView() { }
}