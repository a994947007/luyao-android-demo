package com.hc.android_demo.fragment.content.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.constants.Constants;
import com.hc.android_demo.fragment.content.player.LuSurfaceTexturePlayer;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.fragment.BaseFragment;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.jny.common.fragment.FragmentConstants;

@ARouter(path = FragmentConstants.STITCHING_TEXTURE_VIDEO_FRAGMENT_ID,
        group = FragmentConstants.CUSTOM_VIEW)
public class StitchingTextureViewVideoFragment extends BaseFragment {

    private TextureView textureView;
    private StitchingAnimation stitchingAnimation;
    String videoPath = "https://vd3.bdstatic.com/mda-khmgmk56bmk05j5j/sc/mda-khmgmk56bmk05j5j.mp4";
    private LuSurfaceTexturePlayer mPlayer;
    private boolean isStarted = false;
    private View backgroundView;
    private View videoContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stitching_texture_video_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textureView = view.findViewById(R.id.textureView);
        mPlayer = new LuSurfaceTexturePlayer(textureView, videoPath, 1);
        textureView.setSurfaceTextureListener(mPlayer);
        backgroundView = view.findViewById(R.id.background);
        videoContentView = view.findViewById(R.id.video_content);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isStarted) {
            if (stitchingAnimation == null) {
                Bundle arguments = getArguments();
                if (arguments != null) {
                    Rect fromPosition = arguments.getParcelable(Constants.FROM_RECT_PARAMS_ID);
                    stitchingAnimation = new StitchingAnimation(fromPosition, videoContentView);
                    stitchingAnimation.addExpandAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mPlayer.play();
                        }
                    });
                    stitchingAnimation.addCollapseAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);
                        }
                    });
                    stitchingAnimation.addCollapseUpdateListener(animation -> {
                        float alpha = (float)animation.getAnimatedValue();
                        backgroundView.setAlpha(alpha);
                    });
                    stitchingAnimation.addExpandUpdateListener(animation -> {
                        float alpha = (float)animation.getAnimatedValue();
                        backgroundView.setAlpha(alpha);
                    });
                }
            }
            if (stitchingAnimation != null) {
                stitchingAnimation.open();
            }
            isStarted = true;
        } else {
            mPlayer.play();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.pause();
    }


    @Override
    public boolean onBackPressed() {
        if (stitchingAnimation != null) {
            stitchingAnimation.close();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayer.stop();
    }
}
