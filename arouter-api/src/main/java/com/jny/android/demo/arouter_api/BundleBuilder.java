package com.jny.android.demo.arouter_api;

import android.os.Bundle;

public class BundleBuilder {

    private final Bundle bundle = new Bundle();

    public BundleBuilder withString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleBuilder withInt(String key, Integer value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder withBoolean(String key, Boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public Bundle build() {
        return bundle;
    }
}
