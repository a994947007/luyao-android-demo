package com.hc.android_demo.dialog.bottom.sheet;

import android.view.View;
import com.hc.android_demo.R;
import com.hc.my_views.bottomsheet.HalfBottomSheetView;
import com.hc.support.mvps.Presenter;

public class HalfBottomSheetControlPresenter extends Presenter {
    private View headerView;
    private HalfBottomSheetDialogFragmentTest dialogFragmentTest;
    private HalfBottomSheetView mContainer;

    @Override
    protected void doInject() {
        super.doInject();
        dialogFragmentTest = inject(HalfBottomSheetDialogFragmentTest.class);
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        headerView = rootView.findViewById(R.id.header);
        mContainer = rootView.findViewById(R.id.container);
    }

    @Override
    protected void onBind() {
        super.onBind();
        headerView.setOnClickListener(v -> {
            if (!mContainer.isFullExpended) {
                mContainer.halfToFull();
            } else {
                mContainer.fullToHalf();
            }
        });
    }
}
