package com.example.authentication.Retrofit;

import java.util.concurrent.TimeUnit;
import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit instance = null;

    public static final String API_BASE_URL = "http://0.0.0.127:9000/";

    public static Retrofit getInstance(){
        if(instance == null){

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300,TimeUnit.SECONDS)
                    .readTimeout(300,TimeUnit.SECONDS)
                    .build();

            instance = new Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return  instance;

    }
}
