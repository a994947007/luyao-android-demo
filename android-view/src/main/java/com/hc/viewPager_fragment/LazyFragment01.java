package com.hc.viewPager_fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.List;

public class LazyFragment01 extends Fragment {

    private boolean isCreated;

    protected boolean isPreVisible = false;   // 之前是否可见

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
        if (!isPreVisible && isVisibleToUser) {
            dispatchVisibleState(true);
        } else if(isPreVisible && !isVisibleToUser){
            dispatchVisibleState(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreated = true;
        initView();
        if (getUserVisibleHint()) {
            dispatchVisibleState(true);
        }
    }

    protected void initView() { }

    private void dispatchVisibleState(boolean isVisibleToUser) {
        if (isPreVisible == isVisibleToUser) {
            return;
        }
        isPreVisible = isVisibleToUser;

        // 解决在initView中嵌套子Fragment的情况，导致还不可见就被加载的情况
        // 有以下情形，在该Fragment的隔壁的LazyFragment中嵌套了ViewPager，在initView的时候初始化了ViewPager，进而使得子Fragment被提前加载，
        // 需要增加如下判断
        // 只有parentFragment可见时才加载
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof LazyFragment01 && !parentIsVisible()) {
                return;
            }
        }

        if (isVisibleToUser) {
            onFragmentLoad();
            dispatchChildVisibleState(true);    // Fragment嵌套时，切过去子Fragment无感知，需要手动分发一下
        } else {
            onFragmentLoadStop();
            dispatchChildVisibleState(false);
        }
    }

    private void dispatchChildVisibleState(boolean state) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FragmentManager fragmentManager = getChildFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof LazyFragment01 && !fragment.isHidden() && fragment.getUserVisibleHint()) {
                        ((LazyFragment01)fragment).dispatchVisibleState(state);
                    }
                }
            }
        }
    }

    private boolean parentIsVisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof LazyFragment01) {
                return ((LazyFragment01) parentFragment).isPreVisible;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPreVisible && getUserVisibleHint()) {
            dispatchVisibleState(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isPreVisible && !getUserVisibleHint()) {
            dispatchVisibleState(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
        isPreVisible = false;
    }

    protected void onFragmentLoad() {}

    protected void onFragmentLoadStop() {}
}
