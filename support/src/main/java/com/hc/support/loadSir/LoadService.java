package com.hc.support.loadSir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.support.R;

import java.util.Map;

public class LoadService {
    private View contentView;
    private Callback.OnReloadListener onReloadListener;
    private ViewGroup loadLayout;
    private ViewGroup loadSirContainer;
    private ViewGroup realLayoutContainer;
    private Callback currentCallback;
    private Map<Class<? extends Callback>, Callback> callbacks;
    public LoadService(Map<Class<? extends Callback>, Callback> callbacks, View contentView, Callback.OnReloadListener onReloadListener) {
        this.callbacks = callbacks;
        this.contentView = contentView;
        this.onReloadListener = onReloadListener;
        this.loadLayout = (ViewGroup) LayoutInflater.from(contentView.getContext()).inflate(R.layout.layout_load_sir, (ViewGroup) contentView.getParent(), false);
        this.loadSirContainer = this.loadLayout.findViewById(R.id.load_sir_container);
        this.realLayoutContainer = this.loadLayout.findViewById(R.id.real_layout_container);
        this.realLayoutContainer.addView(contentView);
    }

    public View getLoadLayout() {
        return this.loadLayout;
    }

    public void showCallback(Class<? extends Callback> clazz) {
        Callback callback = this.callbacks.get(clazz);
        if (callback != null) {
            if (currentCallback != callback) {
                if (currentCallback != null && currentCallback.getState() == Callback.State.ATTACHED) {
                    currentCallback.detach();
                }
                callback.setOnReloadListener(onReloadListener);
                callback.createView(loadSirContainer);
                callback.attach();
            }
            currentCallback = callback;
            loadSirContainer.setVisibility(View.VISIBLE);
            realLayoutContainer.setVisibility(View.GONE);
        }
    }

    public void showSuccess() {
        if (currentCallback != null && currentCallback.getState() == Callback.State.ATTACHED) {
            loadSirContainer.setVisibility(View.GONE);
        }
        realLayoutContainer.setVisibility(View.VISIBLE);
    }
}
