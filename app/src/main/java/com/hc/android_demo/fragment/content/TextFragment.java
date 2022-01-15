package com.hc.android_demo.fragment.content;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hc.android_demo.R;
import com.hc.my_views.GradientTextView;
import com.hc.nested_recycler_fragment.ViewFragment;

public class TextFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TextView
        GradientTextView textView = view.findViewById(R.id.textView);
        ObjectAnimator.ofFloat(textView, "rightPercent", 0, 1).setDuration(5000).start();
        textView.setShadowColor(Color.BLUE);

        // ViewPager
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager(), 0) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new ViewFragment(position);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }
}
