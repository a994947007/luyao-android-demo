package com.hc.android_demo.fragment.content;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.hc.base.fragment.BaseFragment;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class StitchingImageFragment extends BaseFragment implements ActivityStarter {

    private ImageView imageView;
    private StitchingAnimation stitchingAnimation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stitching_image_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imgView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stitchingAnimation == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                Rect fromPosition = arguments.getParcelable(Constants.FROM_RECT_PARAMS_ID);
                stitchingAnimation = new StitchingAnimation(fromPosition, imageView);
                stitchingAnimation.addCollapseAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getActivity().finish();
                    }
                });
            }
        }
        if (stitchingAnimation != null) {
            stitchingAnimation.open();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (stitchingAnimation != null) {
            stitchingAnimation.close();
        }
        return true;
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.STITCHING_IMAGE_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new StitchingImageFragment();
    }
}
