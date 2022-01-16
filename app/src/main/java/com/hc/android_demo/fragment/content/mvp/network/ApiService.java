package com.hc.android_demo.fragment.content.mvp.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hc.android_demo.fragment.content.mvp.model.UserListResponse;
import com.hc.android_demo.fragment.content.mvp.model.UserModel;
import com.hc.support.rxJava.observable.Observable;
import com.hc.support.rxJava.observer.Subscriber;
import com.hc.support.rxJava.schedule.Schedules;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {

/*    public static Observable<UserListResponse> requestUserListResponse() {
        String url = "https://reqres.in/api/users";
        OkHttpClient okHttpClient = new OkHttpClient();
        return Observable.create(subscriber -> {
            final Request request = new Request.Builder()
                    .url(url)
                    .get()//默认就是GET请求，可以不写
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, IOException e) {
                    Log.d("UserListResponse", "onFailure: ");
                    subscriber.onError(e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("UserListResponse", response.body().string());
                    try {
                        Gson gson = new Gson();
                        UserListResponse userListResponse = gson.fromJson(response.body().string(), UserListResponse.class);
                        subscriber.onNext(userListResponse);
                        subscriber.onComplete();
                    } catch (Throwable r) {
                        Log.d("UserListResponse", "error" + r.getMessage());
                        subscriber.onError(r);
                    }
                }
            });
        });
    }*/

    public static final String RESPONSE  = "{\"page\":1,\"per_page\":6,\"total\":12,\"total_pages\":2,\"data\":[{\"id\":1,\"email\":\"george.bluth@reqres.in\",\"first_name\":\"George\",\"last_name\":\"Bluth\",\"avatar\":\"https://reqres.in/img/faces/1-image.jpg\"},{\"id\":2,\"email\":\"janet.weaver@reqres.in\",\"first_name\":\"Janet\",\"last_name\":\"Weaver\",\"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"},{\"id\":3,\"email\":\"emma.wong@reqres.in\",\"first_name\":\"Emma\",\"last_name\":\"Wong\",\"avatar\":\"https://reqres.in/img/faces/3-image.jpg\"},{\"id\":4,\"email\":\"eve.holt@reqres.in\",\"first_name\":\"Eve\",\"last_name\":\"Holt\",\"avatar\":\"https://reqres.in/img/faces/4-image.jpg\"},{\"id\":5,\"email\":\"charles.morris@reqres.in\",\"first_name\":\"Charles\",\"last_name\":\"Morris\",\"avatar\":\"https://reqres.in/img/faces/5-image.jpg\"},{\"id\":6,\"email\":\"tracey.ramos@reqres.in\",\"first_name\":\"Tracey\",\"last_name\":\"Ramos\",\"avatar\":\"https://reqres.in/img/faces/6-image.jpg\"}],\"support\":{\"url\":\"https://reqres.in/#support-heading\",\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"}}";

    public static Observable<UserListResponse> requestUserListResponse() {
        return Observable.create((Observable.OnSubscriber<UserListResponse>) subscriber -> {
            Gson gson = new Gson();
            UserListResponse userListResponse = gson.fromJson(RESPONSE, UserListResponse.class);
            subscriber.onNext(userListResponse);
            subscriber.onComplete();
            try {
                subscriber.onNext(userListResponse);
                subscriber.onComplete();
            } catch (Throwable r) {
                subscriber.onError(r);
            }
        }).subscribeOn(Schedules.NEW_THREAD);
    }

    public static void addUser(UserModel userModel) {
        // 模拟发送请求
    }
}
