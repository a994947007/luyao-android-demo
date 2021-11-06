package com.hc.coordinateLayout;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class DependOnBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        child.scrollTo((int)dependency.getX(), (int)dependency.getY());
        return true;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
        return dependency.getTag().equals("dependency");
    }
}
