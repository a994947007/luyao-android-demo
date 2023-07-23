package com.hc.android_demo.fragment.content.performance.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.hc.recycler.RecyclerUserHelper;
import com.hc.recycler.UserModel;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

import java.util.List;

@ARouter(path = FragmentConstants.PERFORMANCE_REFRESH_RECYCLER_VIEW_ELEMENT,
        group = FragmentConstants.PERFORMANCE)
public class FragmentRefreshRecyclerViewElement extends FragmentRefreshRecyclerViewItem{
    @Override
    protected void updateAndDiff(RecyclerView recyclerView, List<UserModel> list) {
        RecyclerUserHelper.updateAndDiffElement(recyclerView, list);
    }
}
