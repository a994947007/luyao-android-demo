package com.jny.android.demo.util;

import com.jny.android.demo.ProcessorConfig;

public class ProcessorUtils {

    public static String getFullPath(String simplePath) {
        return "/" + ProcessorConfig.DEFAULT_GROUP + "/" + simplePath;
    }
}
