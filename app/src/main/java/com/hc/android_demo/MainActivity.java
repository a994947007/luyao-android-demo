package com.hc.android_demo;

import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.dialog.LandscapeInputFragment;
import com.hc.nested_recycler_fragment.RecyclerNestedScrollActivity;
import com.hc.dialog.BottomSheetDialogFragmentV1;
import com.hc.dialog.BottomSheetDialogFragmentV2;
import com.hc.dialog.BottomSheetDialogV1;
import com.hc.dialog.BottomSheetDialogV2;
import com.hc.recyclerView.RecyclerActivity;
import com.hc.util.InflaterUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Pair<String, View.OnClickListener>> items = new ArrayList<>();

    {
        items.add(new Pair<>("BSD简单版", this::onClickDialogV1));
        items.add(new Pair<>("只显示dialog一半", this::onClickDialogV2));
        items.add(new Pair<>("BSD Fragment", this::onClickDialogV3));
        items.add(new Pair<>("BSD Fragment,home回切无动画", this::onClickDialogV4));
        items.add(new Pair<>("九宫格", this::onClickGridImageView));
        items.add(new Pair<>("滑动冲突+吸顶", this::onClickNestedScrollViewBtn));
        items.add(new Pair<>("自定义TextView,属性动画渐变", this::onClickMyTextView));
        items.add(new Pair<>("圆形进度条,仿快手", this::onClickProgressView));
        items.add(new Pair<>("评分条", this::onClickRatingBarBtn));
        items.add(new Pair<>("微信右边的字母列表", this::onClickLetterBar));
        items.add(new Pair<>("RecyclerView的各种用法", this::onClickRecyclerActivity));
        items.add(new Pair<>("仿QQ、酷狗侧滑栏", this::OnClickSlideMenu));
        items.add(new Pair<>("垂直抽屉View+RecyclerView", this::OnClickVerticalDrawRecyclerView));
        items.add(new Pair<>("灵动的鲤鱼", this::onClickFishActivity));
        items.add(new Pair<>("ViewPager+Fragment懒加载", this::onClickLazyFragment));
        items.add(new Pair<>("自定义LayoutManager", this::onClickCustomLayoutManager));
        items.add(new Pair<>("自定义CoordinateLayout", this::onClickCoordinateLayout));
        items.add(new Pair<>("MD复杂折叠布局(AppBarLayout)", this::onClickMaterialDesign));
        items.add(new Pair<>("浮Button、SnackBar、Material输入框", this::onClickMaterialActivityButton));
        items.add(new Pair<>("DrawerLayout，网易云音乐侧边栏", this::onClickDrawerLayoutActivity));
        items.add(new Pair<>("NestedScrollView嵌套滑动", this::onClickNestedScrollViewActivity));
        items.add(new Pair<>("自定义NestedScrollView", this::onClickCustomNestedScrollViewActivity));
        items.add(new Pair<>("全屏Dialog和全屏DialogFragment", this::onClickFullScreenDialogActivity));
        items.add(new Pair<>("输入框Dialog", this::onClickLandscapeInputFragment));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);     // 由于设置了启动加载bg，因此需要将主题设置回去
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView homeRecyclerView = findViewById(R.id.home_recycler_view);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        homeRecyclerView.setLayoutManager(mLinearLayoutManager);
        homeRecyclerView.setAdapter(new HomeRecyclerAdapter(items));
    }

    private static class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
        private final List<Pair<String, View.OnClickListener>> items = new ArrayList<>();

        public HomeRecyclerAdapter(List<Pair<String, View.OnClickListener>> items) {
            this.items.addAll(items);
        }


        @NonNull
        @Override
        public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = InflaterUtils.inflate(parent.getContext(), parent, R.layout.recycler_item);
            return new HomeRecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
            TextView itemTextView = holder.itemView.findViewById(R.id.recycler_item_text_view);
            itemTextView.setText(items.get(position).first);
            holder.itemView.setOnClickListener(items.get(position).second);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

        public HomeRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    private void onClickLetterBar(View view) {
        startActivity(new Intent(MainActivity.this, LetterActivity.class));
    }

    private void onClickRatingBarBtn(View view) {
        startActivity(new Intent(MainActivity.this, RatingBarActivity.class));
    }

    private void onClickProgressView(View view) {
        startActivity(new Intent(MainActivity.this, ProgressActivity.class));
    }

    private void onClickMyTextView(View view) {
        startActivity(new Intent(MainActivity.this, TextActivity.class));
    }

    private void onClickDialogV4(View view) {
        BottomSheetDialogFragmentV2 dialogFragmentV2 = new BottomSheetDialogFragmentV2(this);
        dialogFragmentV2.show(getSupportFragmentManager(), "bsdFragment");
    }

    private void onClickNestedScrollViewBtn(View view) {
        startActivity(new Intent(MainActivity.this, RecyclerNestedScrollActivity.class));
    }

    private void onClickGridImageView(View view) {
        startActivity(new Intent(MainActivity.this, GridImageLayoutActivity.class));
    }

    private void onClickDialogV3(View view) {
        BottomSheetDialogFragmentV1 dialogFragmentV1 = new BottomSheetDialogFragmentV1(this);
        dialogFragmentV1.show(getSupportFragmentManager(), "bsdFragment");
    }

    private void onClickDialogV2(View view) {
        BottomSheetDialogV2 bsdV2 = new BottomSheetDialogV2(this);
        bsdV2.show();
    }

    private void onClickDialogV1(View listener) {
        BottomSheetDialogV1 bsdV1 = new BottomSheetDialogV1(this);
        bsdV1.show();
    }

    private void onClickRecyclerActivity(View view) {
        startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
    }

    private void OnClickSlideMenu(View view) {
        startActivity(new Intent(MainActivity.this, SlideMenuActivity.class));
    }

    private void OnClickVerticalDrawRecyclerView(View view) {
        startActivity(new Intent(MainActivity.this, VerticalDrawerRecyclerView.class));
    }

    private void onClickFishActivity(View view) {
        startActivity(new Intent(MainActivity.this, FishActivity.class));
    }

    private void onClickLazyFragment(View view) {
        startActivity(new Intent(MainActivity.this, LazyActivity.class));
    }

    private void onClickCustomLayoutManager(View view) {
        startActivity(new Intent(MainActivity.this, CustomLayoutActivity.class));
    }

    private void onClickCoordinateLayout(View view) {
        startActivity(new Intent(MainActivity.this, CoordinateLayoutActivity.class));
    }

    private void onClickMaterialDesign(View view) {
        startActivity(new Intent(MainActivity.this, MaterialDesignActivity.class));
    }

    private void onClickMaterialActivityButton(View view) {
        startActivity(new Intent(MainActivity.this, MaterialViewActivity.class));
    }

    private void onClickDrawerLayoutActivity(View view) {
        startActivity(new Intent(MainActivity.this, DrawerLayoutActivity.class));
    }

    private void onClickNestedScrollViewActivity(View view) {
        startActivity(new Intent(MainActivity.this, NestedScrollViewActivity.class));
    }

    private void onClickFullScreenDialogActivity(View view) {
        startActivity(new Intent(MainActivity.this, FullScreenDialogActivity.class));
    }

    private void onClickCustomNestedScrollViewActivity(View view) {
        startActivity(new Intent(MainActivity.this, CustomNestedScrollViewActivity.class));
    }


    private void onClickLandscapeInputFragment(View view) {
        LandscapeInputFragment landscapeInputFragment = new LandscapeInputFragment(this);
        landscapeInputFragment.show(getSupportFragmentManager(), "LandscapeInputFragment");
    }
}