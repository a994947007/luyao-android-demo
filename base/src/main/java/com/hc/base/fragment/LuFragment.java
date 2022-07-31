package com.hc.base.fragment;

import android.content.Intent;

import com.hc.base.activity.Constants;
import com.hc.base.activity.ContentActivity;

public class LuFragment extends BaseFragment{
    protected void startContentActivity(String id) {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra(Constants.CONTENT_FRAGMENT_KEY, id);
        startActivity(intent);
    }
}
