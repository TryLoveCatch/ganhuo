package io.github.trylovecatch.baselibrary.retrofit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.trylovecatch.baselibrary.BuildConfig;
import io.github.trylovecatch.baselibrary.log.Logger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class ApiRetrofit {
    //okhttp
    private final static long connectTimeout = 10 * 1000;
    private final static long readTimeout = 10 * 1000;
    private final static long writeTimeout = 10 * 1000;

    private static ApiRetrofit mInstance;
    private ApiRetrofit(){
        mMapApis = new HashMap<>();
    }
    public static ApiRetrofit getInstance(){
        if(mInstance==null){
            synchronized (ApiRetrofit.class){
                if(mInstance==null){
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return mInstance;
    }

    private Retrofit mRetrofit;
    private Map<Class, Object> mMapApis;

    public void init(String pUrlBase){
        Logger.i(pUrlBase);
        if(mRetrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            // NONE BASIC HEADERS BODY
            if(BuildConfig.DEBUG) {
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            }else {
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            OkHttpClient tClient = new OkHttpClient.Builder()
                    .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(logging)
                    .build();



            Gson tGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .serializeNulls()
                    .create();

            mRetrofit = new Retrofit.Builder()
                    .client(tClient)
                    .baseUrl(pUrlBase)
                    .addConverterFactory(GsonConverterFactory.create(tGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public <T> T get(Class<T> tClass){
        Object tObj = mMapApis.get(tClass);
        if(tObj==null) {
            T t = mRetrofit.create(tClass);
            mMapApis.put(tClass, t);
            return t;
        }else{
            return (T)tObj;
        }
    }

}
