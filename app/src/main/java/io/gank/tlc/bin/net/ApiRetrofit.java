package io.gank.tlc.bin.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class ApiRetrofit {
    //okhttp
    private final static long connectTimeout = 10 * 1000;
    private final static long readTimeout = 10 * 1000;
    private final static long writeTimeout = 10 * 1000;

    private final static String URL_BASE = "http://gank.avosapps.com/api/";


    private Apis apis;

    ApiRetrofit() {
        OkHttpClient tClient = new OkHttpClient();
        tClient.setReadTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        tClient.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
        tClient.setReadTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        Gson tGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .serializeNulls()
                .create();

        Retrofit tRetrofit = new Retrofit.Builder()
                .client(tClient)
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(tGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apis = tRetrofit.create(Apis.class);
    }

    public Apis getApis(){
        return apis;
    }
}
