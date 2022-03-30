package com.hc.android_demo.dialog.bottom.sheet;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.bottom.HalfBottomSheetDialogFragment;
import com.hc.my_views.SimpleRecyclerView;
import com.hc.support.mvps.Presenter;
import com.hc.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class HalfBottomSheetDialogFragmentTest extends HalfBottomSheetDialogFragment {

    private Presenter group = new Presenter();

    private final List<Pair<String, Runnable>> items = new ArrayList<>();
    {
        for (int i = 0; i < 12; i++) {
            items.add(new Pair<>("可扩展半屏弹窗" + i, null));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_half_bottom_sheet_dialog_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleRecyclerView homeRecyclerView = view.findViewById(R.id.recyclerView);
        homeRecyclerView.setLayoutRes(R.layout.recycler_item);
        homeRecyclerView.setTitleRes(R.id.recycler_item_text_view);
        homeRecyclerView.bind(items);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindPresenters(getView());
    }

    protected void bindPresenters(View view) {
        group.add(new HalfBottomSheetControlPresenter());
        group.create(view);
        group.bind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        group.destroy();
    }
}
