package io.gank.tlc.bin.net;

import io.gank.tlc.bin.detail.InfoDetail;
import io.gank.tlc.bin.meizi.info.InfoMeiziData;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public interface Apis {

    int PAGESIZE = 20;

    String type_meizi = "福利";
    String type_video = "休息视频";
    String type_android = "Android";
    String type_ios = "iOS";
    String type_expand = "拓展资源";
    String type_recommend = "瞎推荐";
    String type_app = "App";

    //根据日期获取当天所有数据
    @GET("day/{year}/{month}/{day}")
    Observable<InfoDetail> getAllDataByDate(@Path("year") String pYear, @Path("month") String pMonth, @Path("day") String pDay);



    //分页获取妹子图片
//    @GET("data/福利/{pageSize}/{page}")
//    Observable<InfoMeiziData> getMeizi(@Path("page") int pPage, @Path("pageSize") int pPageSize);
//    Call<InfoMeizi> getMeizi(@Path("page") int pPage);

    @GET("data/{type}/{pageSize}/{page}")
    Observable<InfoMeiziData> getDataByType(@Path("page") int pPage, @Path("pageSize") int pPageSize, @Path("type") String pType);

}
