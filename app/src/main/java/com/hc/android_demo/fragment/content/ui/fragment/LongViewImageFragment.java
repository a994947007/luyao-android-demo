package com.hc.android_demo.fragment.content.ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.my_views.PhotoView;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.LONG_VIEW_IMAGE_TEST_FRAGMENT_ID,
        group = FragmentConstants.CUSTOM_VIEW)
public class LongViewImageFragment extends Fragment {

    private PhotoView draweeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_long_view_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        draweeView = view.findViewById(R.id.imgView);
        //   draweeView.setImageURI(UriUtils.getUri(R.drawable.long_image));
        //   draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_X);
    }

}
