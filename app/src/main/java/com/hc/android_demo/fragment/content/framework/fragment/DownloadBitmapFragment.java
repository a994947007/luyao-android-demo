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

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.base.autoservice.AutoServiceManager;
import com.jny.android.demo.rxandroid.observable.Observable;
import com.jny.android.demo.rxandroid.observer.Consumer;
import com.jny.android.demo.rxandroid.schedule.Schedules;
import com.jny.common.download.DownloadService;
import com.jny.common.fragment.FragmentConstants;

import java.net.MalformedURLException;
import java.net.URL;

@AutoService({ActivityStarter.class})
public class DownloadBitmapFragment extends Fragment implements ActivityStarter {

    private static final String IMAGE_URL1 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1114%2F113020142315%2F201130142315-1-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1656329678&t=bab46c96599ea92d8cd98749427b211a";
    private static final String IMAGE_URL2 = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2Ftp09%2F210F2130512J47-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1656329729&t=26094907d8d51720e002fcff675cb817";

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

        DownloadService downloadService = AutoServiceManager.load(DownloadService.class);
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

    @Override
    public String getStarterId() {
        return FragmentConstants.DOWNLOAD_BITMAP_TEST_FRAGMENT_ID;
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new DownloadBitmapFragment();
    }
}
