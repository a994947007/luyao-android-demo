package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.ui.presenter.SecondFloorOnSlideTipPresenter;
import com.hc.android_demo.fragment.content.ui.presenter.SecondFloorPresenter;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.BaseFragment;
import com.hc.recyclerView.ItemBean;
import com.hc.recyclerView.listView.RecyclerStaggerView;
import com.hc.support.mvps.Presenter;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

import java.util.ArrayList;
import java.util.List;

@ARouter(path = FragmentConstants.SECOND_FLOOR_REFRESH_LAYOUT,
        group = FragmentConstants.CUSTOM_VIEW)
public class SecondFloorRefreshLayoutFragment extends BaseFragment {
    List<ItemBean> itemBeans = new ArrayList<ItemBean>();
    private Presenter mPresenterGroup;

    {
        itemBeans.add(new ItemBean("123", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("124", com.hc.android_view.R.drawable.img2));
        itemBeans.add(new ItemBean("125", com.hc.android_view.R.drawable.img3));
        itemBeans.add(new ItemBean("126", com.hc.android_view.R.drawable.img4));
        itemBeans.add(new ItemBean("127", com.hc.android_view.R.drawable.img5));
        itemBeans.add(new ItemBean("228", com.hc.android_view.R.drawable.img6));
        itemBeans.add(new ItemBean("229", com.hc.android_view.R.drawable.img7));
        itemBeans.add(new ItemBean("230", com.hc.android_view.R.drawable.img8));
        itemBeans.add(new ItemBean("231", com.hc.android_view.R.drawable.img9));
        itemBeans.add(new ItemBean("232", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("233", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("234", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("335", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("336", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("337", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("338", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("339", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("340", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("441", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("442", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("443", com.hc.android_view.R.drawable.img1));
        itemBeans.add(new ItemBean("444", com.hc.android_view.R.drawable.img1));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second_floor_refresh_layout, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterGroup.destroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.content_recyclerView);
        RecyclerStaggerView recyclerStaggerView = new RecyclerStaggerView(getContext(), recyclerView, itemBeans);
        recyclerStaggerView.setAvailable(true);
        recyclerStaggerView.setOrientation(true, false);
        recyclerStaggerView.show();

        mPresenterGroup = new Presenter();
        addPresenters(mPresenterGroup);
        mPresenterGroup.create(view);
        mPresenterGroup.bind();
    }

    protected void addPresenters(Presenter presenterGroup) {
        presenterGroup.add(new SecondFloorPresenter());
        presenterGroup.add(new SecondFloorOnSlideTipPresenter());
    }
}
