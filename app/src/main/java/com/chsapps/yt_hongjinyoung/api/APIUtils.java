package com.chsapps.yt_hongjinyoung.api;

import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {
    private static final String TAG = APIUtils.class.getSimpleName();

    private static final APIUtils instanse = new APIUtils();
    private RequestService apiService;
    private RequestService analysisService;

    public static APIUtils getInstanse() {
        return instanse;
    }

    public APIUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.API_SERVER_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(RequestService.class);
    }

    public RequestService getApiService() {
        return apiService;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true)
                .readTimeout(APIConstants.SERVER_CONNTECT_TIME, TimeUnit.MILLISECONDS)
                .writeTimeout(APIConstants.SERVER_CONNTECT_TIME, TimeUnit.MILLISECONDS)
                .addInterceptor(new RequestInterceptor());

        if (true) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.v(TAG, "message : " + message);
                }
            });

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }
}
