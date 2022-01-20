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
import com.hc.support.preload.PreloadAction;

public class Fragment3 extends Fragment implements PreloadAction {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hello3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = view.findViewById(R.id.textView);
        tv.setText("Fragment3");
    }

    @Override
    public void doLoad() {
        Log.d("PreloadManager", "Fragment3 doLoad");
    }
}
