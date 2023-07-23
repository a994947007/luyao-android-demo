package com.hc.android_demo.fragment.content.framework.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hc.android_demo.R;
import com.hc.base.autoservice.AutoServiceManager;
import com.jny.android.demo.arouter_annotations.ARouter;
import com.android.demo.rxandroid.observable.Observable;
import com.android.demo.rxandroid.schedule.Schedules;
import com.jny.common.fragment.FragmentConstants;
import com.luyao.android.demo.download.download.DownloadService;
import com.luyao.android.demo.download.download.LuDownload;

import java.net.MalformedURLException;
import java.net.URL;

@ARouter(path = FragmentConstants.DOWNLOAD_BITMAP_TEST_FRAGMENT_ID,
        group = FragmentConstants.FRAMEWORK)
public class DownloadBitmapFragment extends Fragment {

    private static final String IMAGE_URL1 = "https://img1.baidu.com/it/u=3709586903,1286591012&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
    private static final String IMAGE_URL2 = "https://img2.baidu.com/it/u=3681172266,4264167375&fm=253&fmt=auto&app=138&f=JPEG?w=501&h=500";

    private ImageView imageView1;
    private ImageView imageView2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download_image_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView1 = view.findViewById(R.id.image1);
        imageView2 = view.findViewById(R.id.image2);

        DownloadService downloadService = LuDownload.getInstance().getDownloadService();
        try {

            Observable.just(new URL(IMAGE_URL1))
                    .map(downloadService::downloadImage)
                    .subscribeOn(Schedules.IO)
                    .observeOn(Schedules.MAIN)
                    .subscribe(bitmap -> imageView1.setImageDrawable(new BitmapDrawable(getResources(), bitmap)));

            downloadService.downloadImageObservable(new URL(IMAGE_URL2))
                    .observeOn(Schedules.MAIN)
                    .subscribe(bitmap -> imageView2.setImageDrawable(new BitmapDrawable(getResources(), bitmap)));

/*            downloadService.observeDownloadFile(getLifecycle(), IMAGE_URL1)
                    .observeOn(Schedules.MAIN)
                    .subscribe(resultPath -> {
                        Uri uri = Uri.fromFile(new File(resultPath));
                        imageView1.setImageURI(uri);
                    });*/
/*            downloadService.downloadFile(getLifecycle(), IMAGE_URL1, new DownloadCallback() {
                @Override
                public void onStart() {
                    Utils.runOnUITread(() -> ToastUtils.show("开始下载"));
                }

                @Override
                public void onDownload(float progress) {

                }

                @Override
                public void onSuccess(String resultPath) {
                    Utils.runOnUITread(() -> {
                        Uri uri = Uri.fromFile(new File(resultPath));
                        imageView1.setImageURI(uri);
                        ToastUtils.show("下载成功");
                    });
                }

                @Override
                public void onError(Throwable r) {

                }
            });
        */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
