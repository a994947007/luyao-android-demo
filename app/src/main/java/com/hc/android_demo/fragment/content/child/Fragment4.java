package com.hc.android_demo.fragment.content.child;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.support.preload.edition2.PreloadManager2;
import com.hc.support.preload.edition3.PreloadAction;

public class Fragment4 extends Fragment implements PreloadAction {
    TextView tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hello, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = view.findViewById(R.id.textView);
        tv.setText("Fragment4");
    }

    @Override
    public void doPreload() {
        Log.d("PreloadManager", "Fragment4 doLoad");
        tv.setText("Fragment4 loaded");
    }
}
