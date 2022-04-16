package com.hc.android_demo.fragment.content;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.android_demo.constants.Constants;
import com.hc.android_demo.fragment.content.player.LuSurfaceTexturePlayer;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.activity.LuActivity;
import com.hc.util.ActivityUtils;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class StitchingAnimationTestFragment extends Fragment implements ActivityStarter {

    private ImageView imgView;
    private TextureView textureView;
    private LuSurfaceTexturePlayer mPlayer;
    String videoPath = "https://vd3.bdstatic.com/mda-khmgmk56bmk05j5j/sc/mda-khmgmk56bmk05j5j.mp4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stitching_animation_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imgView = view.findViewById(R.id.imgView);
        imgView.setOnClickListener(v -> {
            Bundle params = new Bundle();
            Rect fromPosition = new Rect();
            imgView.getGlobalVisibleRect(fromPosition);
            params.putParcelable(Constants.FROM_RECT_PARAMS_ID, fromPosition);
            ActivityUtils.startStitchingActivity((LuActivity) getActivity(), FragmentConstants.STITCHING_IMAGE_FRAGMENT_ID, params);
        });
        textureView = view.findViewById(R.id.textureView);
        mPlayer = new LuSurfaceTexturePlayer(textureView, videoPath, 0);
        textureView.setSurfaceTextureListener(mPlayer);
        textureView.setOnClickListener(v -> {
            Bundle params = new Bundle();
            Rect fromPosition = new Rect();
            textureView.getGlobalVisibleRect(fromPosition);
            params.putParcelable(Constants.FROM_RECT_PARAMS_ID, fromPosition);
            ActivityUtils.startStitchingActivity((LuActivity) getActivity(), FragmentConstants.STITCHING_TEXTURE_VIDEO_FRAGMENT_ID, params);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayer.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.pause();
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
