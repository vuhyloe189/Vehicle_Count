package com.example.authentication.Retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    private String email;
    private String password;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    public AuthenticationInterceptor(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .addHeader("Authorization", authToken)
                .method(original.method(),original.body());

        Request request = builder.build();
        return chain.proceed(request);
    }
}