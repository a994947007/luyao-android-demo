package com.hc.support.util;

public final class TextUtils {

    private static final String EMPTY_STRING = "";

    public static boolean isEmpty(final String text) {
        return text == null || EMPTY_STRING.equals(text);
    }

    public static String emptyString() {
        return EMPTY_STRING;
    }

    public static boolean equals(final String str1, final String str2) {
        if (str1 != null) {
            return str1.equals(str2);
        } else {
            return str2 == null;
        }
    }
}
