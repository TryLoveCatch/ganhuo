package io.gank.tlc.bin.video;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.gank.tlc.bin.meizi.info.InfoMeiziData;
import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.dao.InfoDaoVideo;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventVideo;
import io.gank.tlc.util.L;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class VideoManager {

    private static VideoManager mInstatnce;
    private VideoManager(){}

    public static VideoManager getInstatnce(){
        if(mInstatnce==null){
            synchronized (VideoManager.class){
                if(mInstatnce==null){
                    mInstatnce = new VideoManager();
                }
            }
        }
        return mInstatnce;
    }


    public List<InfoDaoVideo> loadDB(){
        QueryBuilder tQuery = new QueryBuilder(InfoDaoVideo.class);
        tQuery.appendOrderDescBy("createTime");
        tQuery.limit(0, Apis.PAGESIZE);
        return DaoManager.getInstance().mOrm.query(tQuery);


    }

    public Subscription loadHomeData(int pPage, String pType){
        return loadHomeData(pPage, Apis.PAGESIZE, pType);
    }

    public Subscription loadHomeData(int pPage, int pPageSize, final String pType){
        final OkHttpClient tClient = new OkHttpClient();
        return ApiManager.getInstance().mApis.getDataByType(pPage, pPageSize, pType)
                .subscribeOn(Schedulers.io())
                .map(new Func1<InfoMeiziData, ArrayList<InfoDaoMeizi>>() {
                    @Override
                    public ArrayList<InfoDaoMeizi> call(InfoMeiziData infoMeiziData) {
                        return infoMeiziData.results;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<ArrayList<InfoDaoMeizi>, ArrayList<InfoDaoVideo>>() {
                    @Override
                    public ArrayList<InfoDaoVideo> call(ArrayList<InfoDaoMeizi> infoDaoMeizis) {
                        ArrayList<InfoDaoVideo> tArrData = new ArrayList<InfoDaoVideo>();
                        for (InfoDaoMeizi tInfo : infoDaoMeizis) {
                            InfoDaoVideo tInfoVideo = new InfoDaoVideo();
                            tInfoVideo.name = tInfo.who;
                            tInfoVideo.createTime = tInfo.publishedAt;
                            tInfoVideo.desc = tInfo.desc;
                            tInfoVideo.objectId = tInfo.objectId;
                            tInfoVideo.videoUrl = tInfo.url;
                            try {

                                if (tInfo.url.contains("v.youku.com")) {
                                    Response tResponse = tClient.newCall(new Request.Builder().url(tInfo.url).build()).execute();
                                    String tHtml = tResponse.body().string();
                                    tInfoVideo.picUrl = getYoukuPic(tHtml);
                                } else if (tInfo.url.contains("miaopai.com")) {
                                    Response tResponse = tClient.newCall(new Request.Builder().url(tInfo.url).build()).execute();
                                    String tHtml = tResponse.body().string();
                                    tInfoVideo.picUrl = getMiaopaiPic(tHtml);
                                } else if (tInfo.url.contains("video.weibo.com")) {
                                    Response tResponse = tClient.newCall(new Request.Builder().url(tInfo.url).build()).execute();
                                    String tHtml = tResponse.body().string();
                                    tInfoVideo.picUrl = getWeiboPic(tHtml);
                                } else if (tInfo.url.contains("v.qq.com")) {
                                    String tWapUrl = tInfo.url.replace("http://", "http://m.");
                                    Response tResponse = tClient.newCall(new Request.Builder().url(tWapUrl).build()).execute();
                                    String tHtml = tResponse.body().string();
                                    tInfoVideo.picUrl = getQQPic(tHtml);
                                } else if (tInfo.url.contains("bilibili.com")) {
                                    Response tResponse = tClient.newCall(new Request.Builder().url(tInfo.url).build()).execute();
                                    String tHtml = tResponse.body().string();
                                    tInfoVideo.picUrl = getBilibiliPic(tHtml);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            tArrData.add(tInfoVideo);
                        }

                        return tArrData;
                    }
                })
                .doOnNext(new Action1<ArrayList<InfoDaoVideo>>() {
                    @Override
                    public void call(ArrayList<InfoDaoVideo> infoDaoVideos) {
                        for (InfoDaoVideo tInfo : infoDaoVideos) {
                            if (DaoManager.getInstance().mOrm.queryById(tInfo.objectId, InfoDaoVideo.class) == null) {
                                DaoManager.getInstance().mOrm.insert(tInfo, ConflictAlgorithm.Replace);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<InfoDaoVideo>>() {
                    @Override
                    public void call(ArrayList<InfoDaoVideo> infoGanks) {
                        if (infoGanks == null || infoGanks.size() <= 0) {
                            EventBus.post(new EventVideo(false, "数据为空"));
                        } else {
                            EventBus.post(new EventVideo(infoGanks));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventBus.post(new EventVideo(false, ""));
                    }
                });
    }


    private String getYoukuPic(String pHtml){
        String tPicUrl = "";

        int s0 = pHtml.indexOf("id=\"s_qq_haoyou1\"");

        if(s0!=-1){
            s0 = pHtml.indexOf("pics", s0);
            if(s0!=-1){
                tPicUrl = pHtml.substring(s0 + "pics=".length(), pHtml.indexOf("&", s0));
            }
        }
        return tPicUrl;
    }
    private String getMiaopaiPic(String pHtml){
        String tPicUrl = "";

        int s0 = pHtml.indexOf("<div class=\"video_img\">");

        if(s0!=-1){
            s0 = pHtml.indexOf("src", s0);
            if(s0!=-1){
                s0 += "src=\"".length();
                tPicUrl = pHtml.substring(s0 , pHtml.indexOf("\">", s0));
            }
        }
        return tPicUrl;
    }
    private String getWeiboPic(String pHtml){
        String tPicUrl = "";

        int s0 = pHtml.indexOf("<img src = \"");

        if(s0!=-1){
            s0 += "<img src = \"".length();
            tPicUrl = pHtml.substring(s0, pHtml.indexOf("\"", s0));
        }
        return tPicUrl;
    }

    private String getQQPic(String pHtml){
        String tPicUrl = "";

        int s0 = pHtml.indexOf("pic_640_360");

        if(s0!=-1){
            s0 += "pic_640_360\":\"".length();
            tPicUrl = pHtml.substring(s0, pHtml.indexOf("\"", s0));
        }
        return tPicUrl;
    }

    private String getBilibiliPic(String pHtml){
        String tPicUrl = "";

        int s0 = pHtml.indexOf("media:thumbnail");

        if(s0!=-1){
            s0 = pHtml.indexOf("content", s0);
            if(s0!=-1){
                s0 += "content=\"".length();
                tPicUrl = pHtml.substring(s0, pHtml.indexOf("\"", s0));
            }
        }
        L.e(tPicUrl);
        return tPicUrl;
    }
}
