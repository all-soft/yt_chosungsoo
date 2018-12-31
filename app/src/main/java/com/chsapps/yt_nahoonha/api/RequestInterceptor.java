package com.chsapps.yt_nahoonha.api;

import com.chsapps.yt_nahoonha.utils.SecureUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body());
        builder.header(SecureUtils.getAppKey(),SecureUtils.getAppValue());
        return chain.proceed(builder.build());
    }
}
