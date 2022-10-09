package com.hc.android_demo.fragment.content.framework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.jny.android.demo.base_util.TextUtils;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class ViewPostTestFragment extends Fragment implements ActivityStarter {

    private TextView mTestPostTv;
    private TextView mTestPostOnAnimationTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_post_test_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTestPostTv = view.findViewById(R.id.test_post_tv);
        mTestPostOnAnimationTv = view.findViewById(R.id.test_post_onAnimation_tv);
        view.post(() -> mTestPostTv.setText(mTestPostTv.getText() + "\n" + getSizeMsg()));
        view.postOnAnimation(() -> mTestPostOnAnimationTv.setText(mTestPostOnAnimationTv.getText() + "\n" + getSizeMsg()));
    }

    private String getSizeMsg() {
        if (getView() == null) {
            return TextUtils.emptyString();
        }
        return "w:" + getView().getWidth() + " h:" + getView().getHeight();
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.VIEW_POST_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new ViewPostTestFragment();
    }
}
