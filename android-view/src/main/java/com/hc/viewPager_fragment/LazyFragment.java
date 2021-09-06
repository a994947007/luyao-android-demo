package com.hc.viewPager_fragment;

import android.app.Fragment; // 注意这是android的Fragment，因此只能通过调用add、attach、remove、detach、show、hide来控制Fragment
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * setUserVisibleHint -> onCreateView -> onResume() -> onPause -> onDestroyView()
 */
public abstract class LazyFragment extends Fragment {

    private boolean isViewCreated = false;
    // 当前的状态
    private boolean currentVisibleState = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourceId(), container, false);
        onBindView(view);
        isViewCreated = true;
        // 由于setUserVisibleHint的生命周期在onCreateView之前，因此onFragmentLoad被拦截了，需要这里补充一个事件
        if (getUserVisibleHint()) {
            dispatchVisibleHint(true);
        }
        return view;
    }

    /**
     * 懒加载实现方案：ViewPager切换到当前Fragment时，会将设置当前Fragment的setUserVisibleHint(true)，离开的Fragment将设置setUserVisibleHint(false)
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !currentVisibleState) {  // 设置的状态可见，当前状态不可见就分发可见事件
            dispatchVisibleHint(true);
        } else if (!isVisibleToUser && currentVisibleState) { // 设置的状态不可见，当前状态可见，就分发不可见事件
            dispatchVisibleHint(false);
        }
    }


    protected void dispatchVisibleHint(boolean isVisibleToUser) {
        if (currentVisibleState == isVisibleToUser) {
            return;
        }
        currentVisibleState = isVisibleToUser;
        if (isViewCreated) {
            if (isVisibleToUser) {
                onFragmentLoad();
            } else {
                onFragmentLoadStop();
            }
        }
    }

    protected void onFragmentLoadStop() {
    }

    protected void onFragmentLoad() {
        
    }

    protected abstract void onBindView(View view);

    protected abstract int getResourceId();

    /**
     * onResume()和onPause()可能会被Activity本身触发，而不是通过ViewPager触发的，那么setUserVisibleHint()就不会被调用，实现不了加载的效果。
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!currentVisibleState && getUserVisibleHint()) {
            dispatchVisibleHint(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        currentVisibleState = false;
    }
}
