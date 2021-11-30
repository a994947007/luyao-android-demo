package com.hc.android_demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hc.android_demo.activity.CoordinateLayoutActivity;
import com.hc.android_demo.activity.CustomLayoutActivity;
import com.hc.android_demo.activity.CustomNestedScrollViewActivity;
import com.hc.android_demo.activity.DrawerLayoutActivity;
import com.hc.android_demo.activity.FishActivity;
import com.hc.android_demo.activity.FullScreenDialogActivity;
import com.hc.android_demo.activity.GridImageLayoutActivity;
import com.hc.android_demo.activity.LazyActivity;
import com.hc.android_demo.activity.LetterActivity;
import com.hc.android_demo.activity.LongViewImageActivity;
import com.hc.android_demo.activity.MaterialDesignActivity;
import com.hc.android_demo.activity.MaterialViewActivity;
import com.hc.android_demo.activity.NestedScrollViewActivity;
import com.hc.android_demo.activity.PhotoViewActivity;
import com.hc.android_demo.activity.ProgressActivity;
import com.hc.android_demo.R;
import com.hc.android_demo.activity.RatingBarActivity;
import com.hc.android_demo.activity.SlideMenuActivity;
import com.hc.android_demo.activity.TextActivity;
import com.hc.android_demo.activity.VerticalDrawerRecyclerViewActivity;
import com.hc.base.autoservice.AutoServiceManager;
import com.hc.dialog.BottomSheetDialogFragmentV1;
import com.hc.dialog.BottomSheetDialogFragmentV2;
import com.hc.dialog.BottomSheetDialogV1;
import com.hc.dialog.BottomSheetDialogV2;
import com.hc.dialog.LandscapeInputFragment;
import com.hc.nested_recycler_fragment.RecyclerNestedScrollActivity;
import com.hc.recyclerView.RecyclerActivity;
import com.hc.util.InflaterUtils;
import com.hc.util.ViewUtils;
import com.jny.common.webview.WebViewService;
import com.jny.webview.webviewProgress.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class CustomViewFragment extends Fragment {

    private ViewGroup mContentView;

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
        items.add(new Pair<>("组件化(AutoService)+DataBinding+WebVIew", this::onClickWebViewActivity));
        items.add(new Pair<>("PhotoView，支持图片双击放大双击缩小", this::onClickPhotoViewActivity));
        items.add(new Pair<>("长图+落坑动画", this::onClickLongImageViewActivity));
    }

    public static CustomViewFragment newInstance() {
        CustomViewFragment fragment = new CustomViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity)getContext()).getWindow().setNavigationBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
            ((Activity)getContext()).getWindow().setStatusBarColor(ViewUtils.getColor(R.color.bottom_nav_color));
            ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = (ViewGroup) inflater.inflate(R.layout.fragment_custom_view, container, false);
        RecyclerView homeRecyclerView = mContentView.findViewById(R.id.home_recycler_view);
        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        homeRecyclerView.setLayoutManager(mLinearLayoutManager);
        homeRecyclerView.setAdapter(new HomeRecyclerAdapter(items));
        return mContentView;
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
        startActivity(new Intent(getContext(), LetterActivity.class));
    }

    private void onClickRatingBarBtn(View view) {
        startActivity(new Intent(getContext(), RatingBarActivity.class));
    }

    private void onClickProgressView(View view) {
        startActivity(new Intent(getContext(), ProgressActivity.class));
    }

    private void onClickMyTextView(View view) {
        startActivity(new Intent(getContext(), TextActivity.class));
    }

    private void onClickDialogV4(View view) {
        BottomSheetDialogFragmentV2 dialogFragmentV2 = new BottomSheetDialogFragmentV2((AppCompatActivity) getContext());
        dialogFragmentV2.show(getFragmentManager(), "bsdFragment");
    }

    private void onClickNestedScrollViewBtn(View view) {
        startActivity(new Intent(getContext(), RecyclerNestedScrollActivity.class));
    }

    private void onClickGridImageView(View view) {
        startActivity(new Intent(getContext(), GridImageLayoutActivity.class));
    }

    private void onClickDialogV3(View view) {
        BottomSheetDialogFragmentV1 dialogFragmentV1 = new BottomSheetDialogFragmentV1((Activity) getContext());
        dialogFragmentV1.show(getFragmentManager(), "bsdFragment");
    }

    private void onClickDialogV2(View view) {
        BottomSheetDialogV2 bsdV2 = new BottomSheetDialogV2(getContext());
        bsdV2.show();
    }

    private void onClickDialogV1(View listener) {
        BottomSheetDialogV1 bsdV1 = new BottomSheetDialogV1(getContext());
        bsdV1.show();
    }

    private void onClickRecyclerActivity(View view) {
        startActivity(new Intent(getContext(), RecyclerActivity.class));
    }

    private void OnClickSlideMenu(View view) {
        startActivity(new Intent(getContext(), SlideMenuActivity.class));
    }

    private void OnClickVerticalDrawRecyclerView(View view) {
        startActivity(new Intent(getContext(), VerticalDrawerRecyclerViewActivity.class));
    }

    private void onClickFishActivity(View view) {
        startActivity(new Intent(getContext(), FishActivity.class));
    }

    private void onClickLazyFragment(View view) {
        startActivity(new Intent(getContext(), LazyActivity.class));
    }

    private void onClickCustomLayoutManager(View view) {
        startActivity(new Intent(getContext(), CustomLayoutActivity.class));
    }

    private void onClickCoordinateLayout(View view) {
        startActivity(new Intent(getContext(), CoordinateLayoutActivity.class));
    }

    private void onClickMaterialDesign(View view) {
        startActivity(new Intent(getContext(), MaterialDesignActivity.class));
    }

    private void onClickMaterialActivityButton(View view) {
        startActivity(new Intent(getContext(), MaterialViewActivity.class));
    }

    private void onClickDrawerLayoutActivity(View view) {
        startActivity(new Intent(getContext(), DrawerLayoutActivity.class));
    }

    private void onClickNestedScrollViewActivity(View view) {
        startActivity(new Intent(getContext(), NestedScrollViewActivity.class));
    }

    private void onClickFullScreenDialogActivity(View view) {
        startActivity(new Intent(getContext(), FullScreenDialogActivity.class));
    }

    private void onClickCustomNestedScrollViewActivity(View view) {
        startActivity(new Intent(getContext(), CustomNestedScrollViewActivity.class));
    }

    private void onClickLandscapeInputFragment(View view) {
        LandscapeInputFragment landscapeInputFragment = new LandscapeInputFragment((Activity) getContext());
        landscapeInputFragment.show(getFragmentManager(), "LandscapeInputFragment");
    }

    private void onClickWebViewActivity(View view) {
        // 这里只依赖了common模块，并未依赖webview模块（IOC，解耦）
        WebViewService webViewService = AutoServiceManager.load(WebViewService.class);
        webViewService.openWebViewActivity(getContext(), Constants.WEB_URL + "demo.html", true, "demo");
    }

    private void onClickPhotoViewActivity(View view) {
        startActivity(new Intent(getContext(), PhotoViewActivity.class));
    }

    private void onClickLongImageViewActivity(View view) {
        startActivity(new Intent(getContext(), LongViewImageActivity.class));
    }
}