package io.gank.tlc.bin.home;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.gank.tlc.bin.meizi.info.InfoMeiziData;
import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.dao.InfoDaoHome;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventHome;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class HomeManager {

    private static HomeManager mInstatnce;
    private HomeManager(){}

    public static HomeManager getInstatnce(){
        if(mInstatnce==null){
            synchronized (HomeManager.class){
                if(mInstatnce==null){
                    mInstatnce = new HomeManager();
                }
            }
        }
        return mInstatnce;
    }


    public List<InfoDaoMeizi> loadDB(){
        QueryBuilder tQuery = new QueryBuilder(InfoDaoHome.class);
        tQuery.appendOrderDescBy("createTime");
        tQuery.limit(0, Apis.PAGESIZE);
        return DaoManager.getInstance().mOrm.query(tQuery);


    }

    public Subscription loadHomeData(int pPage){
        return loadHomeData(pPage, Apis.PAGESIZE);
    }

    public Subscription loadHomeData(int pPage, int pPageSize){
        return Observable.zip(ApiManager.getInstance().mApis.getDataByType(pPage, pPageSize, Apis.type_meizi),
                ApiManager.getInstance().mApis.getDataByType(pPage, pPageSize, Apis.type_video), new Func2<InfoMeiziData, InfoMeiziData, List<InfoDaoHome>>() {
                    @Override
                    public List<InfoDaoHome> call(InfoMeiziData o, InfoMeiziData o2) {
                        List<InfoDaoHome> tArr = new ArrayList<>();
                        for(int i=0;i<o.results.size();i++){
                            InfoDaoHome tInfoHome = new InfoDaoHome();
                            InfoDaoMeizi tInfoMeizi = o.results.get(i);
                            tInfoHome.objectId = tInfoMeizi.objectId;
                            tInfoHome.createTime = tInfoMeizi.publishedAt;
                            tInfoHome.picUrl = tInfoMeizi.url;
                            tInfoHome.name = tInfoMeizi.who;
                            for(int j=0;j<o2.results.size();j++){
                                InfoDaoMeizi tInfoVideo = o2.results.get(j);
                                if(isTheSameDay(tInfoMeizi.publishedAt, tInfoVideo.publishedAt)){
                                    tInfoHome.desc = tInfoVideo.desc;
                                    break;
                                }
                            }
                            tArr.add(tInfoHome);
                        }
                        return tArr;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<InfoDaoHome>>() {
                    @Override
                    public void call(List<InfoDaoHome> infoDaoHomes) {
                        for (InfoDaoHome tInfo : infoDaoHomes) {
                            if (DaoManager.getInstance().mOrm.queryById(tInfo.objectId, InfoDaoHome.class) == null) {
                                DaoManager.getInstance().mOrm.insert(tInfo, ConflictAlgorithm.Replace);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InfoDaoHome>>() {
                    @Override
                    public void call(List<InfoDaoHome> infoDaoHomes) {
                        if (infoDaoHomes == null || infoDaoHomes.size() <= 0) {
                            EventBus.post(new EventHome(false, "数据为空"));
                        } else {
                            EventBus.post(new EventHome(infoDaoHomes));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventBus.post(new EventHome(false, ""));
                    }
                });
    }


    private boolean isTheSameDay(Date one, Date another) {
        Calendar _one = Calendar.getInstance();
        _one.setTime(one);
        Calendar _another = Calendar.getInstance();
        _another.setTime(another);
        int oneDay = _one.get(Calendar.DAY_OF_YEAR);
        int anotherDay = _another.get(Calendar.DAY_OF_YEAR);

        return oneDay == anotherDay;
    }
}
