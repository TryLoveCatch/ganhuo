package io.github.trylovecatch.gank.service;

import java.util.List;

import io.github.trylovecatch.gank.info.InfoGank;
import io.github.trylovecatch.gank.info.InfoNetBase;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public interface ApiServeces {

    String TYPE_ALL = "all";
    String TYPE_ANDROID = "Android";
    String TYPE_IOS = "iOS";
    String TYPE_MEIZI = "福利";
    String TYPE_VIDEO = "休息视频";
    String TYPE_EXPAND = "拓展资源";
    String TYPE_FRONT = "前端";
    String TYPE_RECOMMEND = "瞎推荐";
    String TYPE_APP = "App";

    @GET("data/{type}/{pageSize}/{page}")
    Observable<InfoNetBase<List<InfoGank>>> getDataByType(@Path("page") int pPage,
                                                          @Path("pageSize") int pPageSize, @Path("type") String pType);

}
