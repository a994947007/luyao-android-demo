package com.hc.android_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.hc.my_views.ImageGridLayout;

public class GridImageLayoutActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image_layout);

        ImageGridLayout layout = findViewById(R.id.imageLayout);
        layout.setAdapter(new ImageGridAdapter(resIds));

        ImageGridLayout layout2 = findViewById(R.id.imageLayout2);
        layout2.setAdapter(new ImageGridAdapter(resIds2));

        ImageGridLayout layout3 = findViewById(R.id.imageLayout3);
        layout3.setAdapter(new ImageGridAdapter(resIds3));

        ImageGridLayout layout4 = findViewById(R.id.imageLayout4);
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