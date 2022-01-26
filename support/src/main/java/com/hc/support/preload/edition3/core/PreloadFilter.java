package com.hc.support.preload.edition3.core;

public interface PreloadFilter {
    boolean filter(int direction, float offsetPercent, int offsetPixes);
}
