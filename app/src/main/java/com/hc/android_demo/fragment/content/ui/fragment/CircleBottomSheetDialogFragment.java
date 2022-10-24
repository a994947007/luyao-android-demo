package com.hc.android_demo.fragment.content.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hc.android_demo.R;
import com.hc.base.autoservice.AutoServiceManager;
import com.hc.design.drawable.CircleBitmapDrawable;
import com.android.demo.rxandroid.schedule.Schedules;
import com.jny.download.DownloadService;

import java.net.MalformedURLException;
import java.net.URL;

public class CircleBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String IMAGE_URL2 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2Ftp09%2F210F2130512J47-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1656329729&t=26094907d8d51720e002fcff675cb817";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, com.hc.dialog.R.style.BottomSheetDialogBg);      // 用于设置圆角背景透明且无蒙层
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DownloadService downloadService = AutoServiceManager.load(DownloadService.class);
        try {
            downloadService.downloadImageObservable(new URL(IMAGE_URL2))
                    .observeOn(Schedules.MAIN)
                    .subscribe(bitmap -> {
                        //requireView().setBackground(new BitmapDrawable(getResources(), bitmap.copy(bitmap.getConfig(),true)));
                        //requireView().setBackground(new CircleBitmapDrawable(bitmap, 50, true, true, false, false));
                        requireView().setBackground(new CircleBitmapDrawable(Color.WHITE, 50, true, true, false, false));
                    });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_circle_bottom_sheet_layout, container, false);
    }
}
