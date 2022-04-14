package com.hc.android_demo.fragment.content;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.constants.Constants;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.activity.LuActivity;
import com.hc.util.ActivityUtils;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class StitchingAnimationTestFragment extends Fragment implements ActivityStarter {

    private ImageView imgView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stitching_animation_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imgView = view.findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                Rect fromPosition = new Rect();
                imgView.getGlobalVisibleRect(fromPosition);
                params.putParcelable(Constants.FROM_RECT_PARAMS_ID, fromPosition);
                ActivityUtils.startStitchingActivity((LuActivity) getActivity(), FragmentConstants.STITCHING_IMAGE_FRAGMENT_ID, params);
            }
        });
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.STITCHING_ANIMATION_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new StitchingAnimationTestFragment();
    }
}
