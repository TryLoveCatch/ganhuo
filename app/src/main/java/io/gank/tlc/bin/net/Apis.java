package io.gank.tlc.bin.net;

import io.gank.tlc.bin.detail.InfoDetail;
import io.gank.tlc.bin.home.info.InfoMeizi;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public interface Apis {

    int PAGESIZE = 20;

    //根据日期获取当天所有数据
    @GET("day/{year}/{month}/{day}")
    Observable<InfoDetail> getAllDataByDate(@Path("year") int pYear, @Path("month") int pMonth, @Path("day") int pDay);
    //分页获取妹子图片
//    @GET("data/福利/" + PAGESIZE + "/{page}")
    @GET("data/福利/{pageSize}/{page}")
    Observable<InfoMeizi> getMeizi(@Path("page") int pPage, @Path("pageSize") int pPageSize);
//    Call<InfoMeizi> getMeizi(@Path("page") int pPage);

}
