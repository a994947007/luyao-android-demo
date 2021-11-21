package com.hc.support.loadSir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class Callback {
    private View loadView;
    enum State {
        INIT, ATTACHED, DETACHED;
    }
    private State state = State.INIT;
    private ViewGroup mParent;
    protected OnReloadListener reloadListener;

    public View createView(ViewGroup parent) {
        this.mParent = parent;
        state = State.INIT;
        if (loadView == null) {
            this.loadView = LayoutInflater.from(parent.getContext()).inflate(onCreateView(), parent, false);
        }
        return this.loadView;
    }

    public void attach() {
        if (state == State.INIT) {
            mParent.removeAllViews();
            FrameLayout parent = (FrameLayout) this.loadView.getParent();
            if (parent != null) {
                parent.removeView(this.loadView);
            }
            mParent.addView(this.loadView);
            onAttach(loadView.getContext(), loadView);
            state = State.ATTACHED;
        }
    }

    public void detach() {
        if (state == State.ATTACHED) {
            this.mParent.removeAllViews();
            onDetach();
            state = State.DETACHED;
        }
    }

    public State getState() {
        return state;
    }

    protected abstract int onCreateView();

    protected void onAttach(Context context, View loadView) {}

    protected void onDetach() {}

    public void setOnReloadListener(OnReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    public interface OnReloadListener {
        void onReload(View view);
    }
}
