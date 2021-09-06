package com.hc.android_demo;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.hc.nested_recycler_fragment.ViewFragment;
import com.hc.my_views.GradientTextView;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        // TextView
        GradientTextView textView = findViewById(R.id.textView);
        ObjectAnimator.ofFloat(textView, "rightPercent", 0, 1).setDuration(5000).start();
        textView.setShadowColor(Color.BLUE);

        // ViewPager
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), 0) {
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