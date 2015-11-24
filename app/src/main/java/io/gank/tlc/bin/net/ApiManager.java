package io.gank.tlc.bin.net;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class ApiManager {

    public Apis mApis;
    private static ApiManager mManager;

    private ApiManager(){
        mApis = new ApiRetrofit().getApis();
    }

    public static ApiManager getInstance(){
        if(mManager==null) {
            synchronized (ApiManager.class) {
                if (mManager == null) {
                    mManager = new ApiManager();
                }
            }
        }
        return mManager;
    }
}
