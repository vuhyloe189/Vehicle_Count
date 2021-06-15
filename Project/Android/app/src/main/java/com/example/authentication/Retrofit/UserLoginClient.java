package com.example.authentication.Retrofit;

import android.text.TextUtils;
import android.util.Base64;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Call;
//import okhttp3.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import com.example.authentication.LoginActivity;
import com.example.authentication.Models.*;

public class UserLoginClient {
    public static final String API_BASE_URL = "http://192.168.0.120:9000/";

    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String credentials = username + ":" + password;
            String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, basic);
        }

        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
//        Input auth right
//                Log.v( TAG, "The information is " + authToken);

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    public interface LoginService {
        @POST("myCode/authenticate")
        Call<UserComponent> basicLogin();
    }
}

