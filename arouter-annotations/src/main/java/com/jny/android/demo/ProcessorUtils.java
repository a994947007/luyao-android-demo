package com.jny.android.demo;

public class ProcessorUtils {

    public static String getFullPath(String simplePath) {
        return "/" + ProcessorConfig.DEFAULT_GROUP + "/" + simplePath;
    }

    public static String upperCaseFirstChat(String str) {
        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
    }
}
