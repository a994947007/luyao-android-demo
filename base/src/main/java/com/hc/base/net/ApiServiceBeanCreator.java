package com.hc.base.net;

import com.hc.support.singleton.BeanCreator;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceBeanCreator implements BeanCreator {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    public Object create(Class<?> clazz) {
        return retrofit.create(clazz);
    }
}
