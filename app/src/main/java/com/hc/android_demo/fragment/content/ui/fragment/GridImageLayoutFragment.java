package com.hc.android_demo.fragment.content.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.my_views.ImageGridLayout;

public class GridImageLayoutFragment extends Fragment {

    private int resIds[] = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
    };

    private int resIds2[] = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
    };

    private int resIds3[] = {
            R.drawable.img1
    };

    private int resIds4[] = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid_image_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageGridLayout layout = view.findViewById(R.id.imageLayout);
        layout.setAdapter(new ImageGridAdapter(resIds));

        ImageGridLayout layout2 = view.findViewById(R.id.imageLayout2);
        layout2.setAdapter(new ImageGridAdapter(resIds2));

        ImageGridLayout layout3 = view.findViewById(R.id.imageLayout3);
        layout3.setAdapter(new ImageGridAdapter(resIds3));

        ImageGridLayout layout4 = view.findViewById(R.id.imageLayout4);
        layout4.setAdapter(new ImageGridAdapter(resIds4));
    }

    private static class ImageGridAdapter extends ImageGridLayout.Adapter<ImageGridHolder> {
        private int imageResId[];
        private ImageView imageView;
        @Override
        public ImageGridHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent,false);
            imageView = view.findViewById(R.id.imageView);
            return new ImageGridHolder(view);
        }

        public ImageGridAdapter(int imageResId[]) {
            this.imageResId = imageResId;
        }

        @Override
        public void onBindViewHolder(ImageGridHolder holder, int position) {
            imageView.setImageResource(imageResId[position]);
        }

        @Override
        public int getItemCount() {
            return imageResId.length;
        }
    }

    private static class ImageGridHolder extends ImageGridLayout.ViewHolder {

        public ImageGridHolder(View itemView) {
            super(itemView);
        }
    }
}
