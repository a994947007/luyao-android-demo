package com.hc.support.preload.edition1;

import androidx.lifecycle.LifecycleOwner;

public interface PreloadAction extends LifecycleOwner {
    void doLoad();

}
