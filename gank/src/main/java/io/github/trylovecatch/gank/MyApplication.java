package io.github.trylovecatch.gank;

import io.github.trylovecatch.baselibrary.application.BaseApplication;
import io.github.trylovecatch.baselibrary.retrofit.ApiRetrofit;

/**
 * Created by lipeng21 on 2017/6/9.
 */

public class MyApplication extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initRetrofit() {
        ApiRetrofit.getInstance().init("http://gank.io/api/");
    }
}
