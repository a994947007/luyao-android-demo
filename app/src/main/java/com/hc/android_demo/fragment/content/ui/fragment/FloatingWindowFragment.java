package com.hc.android_demo.fragment.content.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.service.FloatingWindowService;
import com.hc.base.fragment.LuFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.FLOAT_WINDOW_FRAGMENT,
        group = FragmentConstants.CUSTOM_VIEW)
public class FloatingWindowFragment extends LuFragment {
    private Button mButton;
    String videoPath = "https://vd3.bdstatic.com/mda-khmgmk56bmk05j5j/sc/mda-khmgmk56bmk05j5j.mp4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floating_window_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButton = view.findViewById(R.id.floatingWindowBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getContext())) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName())), 0);
                    } else {
                        getActivity().startService(new Intent(getContext(), FloatingWindowService.class).putExtra(FloatingWindowService.VIDEO_PATH_KEY, videoPath));
                    }
                }

            }
        });
    }
}
