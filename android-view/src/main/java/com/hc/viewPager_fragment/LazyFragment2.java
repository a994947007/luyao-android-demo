package com.hc.viewPager_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 通过使用FragmentAdapter来使用这个类，它具有onCreateView，onResume，onPause，onDestroyView等生命周期
 */
public abstract class LazyFragment2 extends Fragment {
    private boolean isLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getResourceId(), container, false);
        onBindView(mRootView);
        return mRootView;
    }

    protected void onBindView(View rootView) { }

    protected abstract int getResourceId();

    /**
     * 这个方法只有被在显示界面的时候被调用
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && !isLoaded){
            onFragmentLoad();
            isLoaded = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden() && !isLoaded){
            onFragmentLoad();
            isLoaded = true;
        }
    }

    protected void onFragmentLoadStop() {}

    protected void onFragmentLoad() {}

    // 需要在这里重置状态，不在ViewPager缓存范围，需要重置状态，因为Image资源那些会被释放，需要下次onResume的时候重新展示
    // 这个方法的实现不能放在onDestroy中，因为不在ViewPager缓存范围中也没有被调用
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isLoaded) {
            onFragmentLoadStop();
            isLoaded = false;
        }
    }
}
