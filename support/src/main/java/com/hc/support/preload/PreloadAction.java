package com.hc.support.preload;

import androidx.lifecycle.LifecycleOwner;

public interface PreloadAction extends LifecycleOwner {
    void doLoad();

}
