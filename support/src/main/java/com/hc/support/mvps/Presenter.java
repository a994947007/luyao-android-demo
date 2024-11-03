package com.hc.support.mvps;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.android.demo.rxandroid.disposable.Disposable;

import java.util.ArrayList;
import java.util.List;

public class Presenter implements ViewBinder{
    private final List<Presenter> children = new ArrayList<>();
    private final ContextWrapper contextWrapper = new ContextWrapper();
    private final List<Disposable> disposables = new ArrayList<>();
    private View rootView;

    private State currentState = State.INIT;

    public void add(Presenter child) {
        children.add(child);
    }

    public void create(View rootView) {
        currentState = currentState.next();
        this.rootView = rootView;
        onCreate();
        doBindView(rootView);
        for (Presenter child : children) {
            child.create(rootView);
        }
    }

    public Context getContext() {
        return rootView.getContext();
    }

    public Activity getActivity() {
        for (Context context = getContext(); context instanceof android.content.ContextWrapper;
             context = ((android.content.ContextWrapper)context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }

    protected void onCreate() { }

    public void bind(Object... callerContext) {
        provide(callerContext);
        injectInternal();
        bindInternal();
    }

    public void bindInternal() {
        currentState = currentState.next();
        if (currentState == State.BIND) {
            onBind();
            for (Presenter child : children) {
                child.bindInternal();
            }
        } else {
            throw new RuntimeException("current state not is bind");
        }
    }

    private void provide(Object [] callerContext) {
        provideInternal(callerContext);
        for (Presenter child : children) {
            child.provide(callerContext);
        }
    }

    private void provideInternal(Object [] callerContext) {
        if (callerContext != null && callerContext.length > 0) {
            for (Object context : callerContext) {
                if (context instanceof NameParam) {
                    String key = ((NameParam) context).key;
                    Object bean = ((NameParam) context).bean;
                    if (key == null || "".equals(key)) {
                        throw new IllegalArgumentException("key not be empty");
                    }
                    if (bean == null) {
                        throw new IllegalArgumentException("provider bean not be empty");
                    }
                    contextWrapper.add(key, bean);
                } else {
                    Class<?> clazz = context.getClass();
                    contextWrapper.add(clazz, context);
                }
            }
        }
    }

    private void injectInternal() {
        doInject();
        for (Presenter child : children) {
            child.doInject();
            child.injectInternal();
        }
    }

    protected void onBind() {

    }

    public void unBind() {
        currentState = currentState.next();
        if (currentState == State.UNBIND) {
            onUnBind();
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
            for (Presenter child : children) {
                child.unBind();
            }
        } else {
            throw new RuntimeException("current state not is unBind");
        }
    }

    protected void onUnBind() { }

    public void destroy() {
        if (currentState == State.BIND) {
            unBind();
        }
        destroyInternal();
    }

    private void destroyInternal() {
        currentState = State.DESTROY;
        onDestroy();
        rootView = null;
        for (Presenter child : children) {
            child.destroy();
        }
    }

    protected void onDestroy() { }

    protected void doInject() {

    }

    protected <T> T inject(Class<?> clazz) {
        T res = (T) contextWrapper.get(clazz);
        if (res == null) {
            throw new IllegalArgumentException("inject bean is null");
        }
        return res;
    }

    protected <T> Reference<T> injectRef(Class<?> clazz) {
        T res = (T) contextWrapper.get(clazz);
        if (res == null) {
            throw new IllegalArgumentException("inject bean is null");
        }
        return new Reference<T>() {
            @Override
            public T get() {
                return res;
            }
        };
    }

    protected <T> Reference<T> injectRef(String key) {
        T res = (T) contextWrapper.get(key);
        if (res == null) {
            throw new IllegalArgumentException("inject bean is null");
        }
        return new Reference<T>() {
            @Override
            public T get() {
                return res;
            }
        };
    }

    protected <T> T inject(String key) {
        T res = (T) contextWrapper.get(key);
        if (res == null) {
            throw new IllegalArgumentException("inject bean is null");
        }
        return res;
    }

    protected <T> T injectOptional(Class<?> clazz) {
        return (T) contextWrapper.get(clazz);
    }

    protected <T> T injectOptional(String key) {
        return (T) contextWrapper.get(key);
    }

    protected void addToAutoDispose(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void doBindView(View rootView) {

    }

    public View getRootView() {
        return rootView;
    }
}
