package com.jny.common.load;

import com.hc.support.loadSir.Callback;
import com.jny.common.R;

public class LoadingCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }
}
