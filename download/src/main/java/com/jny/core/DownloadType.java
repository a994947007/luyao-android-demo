package com.jny.core;

import androidx.annotation.IntDef;

@IntDef({
        DownloadType.UNKNOWN,
        DownloadType.VIDEO,
        DownloadType.AUDIO,
        DownloadType.IMAGE,
        DownloadType.DOC,
        DownloadType.ZIP,
        DownloadType.RAR
})
public @interface DownloadType {
    int UNKNOWN = 0;
    int VIDEO = 1;
    int AUDIO = 2;
    int IMAGE = 3;
    int DOC = 4;
    int ZIP = 5;
    int RAR =  6;
}
