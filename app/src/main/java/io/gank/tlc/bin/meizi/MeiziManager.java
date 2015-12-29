package io.gank.tlc.bin.meizi;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import io.gank.tlc.bin.meizi.info.InfoMeiziData;
import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventMeizi;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class MeiziManager {

    private static MeiziManager mInstatnce;
    private MeiziManager(){}

    public static MeiziManager getInstatnce(){
        if(mInstatnce==null){
            synchronized (MeiziManager.class){
                if(mInstatnce==null){
                    mInstatnce = new MeiziManager();
                }
            }
        }
        return mInstatnce;
    }


    protected List<InfoDaoMeizi> loadDB(String pType){
        QueryBuilder tQuery = new QueryBuilder(InfoDaoMeizi.class);
        tQuery.where("type like ?", new String[]{pType + "%"});
        tQuery.appendOrderDescBy("updatedAt");
        tQuery.limit(0, Apis.PAGESIZE);
        return DaoManager.getInstance().mOrm.query(tQuery);


    }

    public Subscription loadHomeData(int pPage){
        return loadHomeData(pPage, Apis.PAGESIZE);
    }

    public Subscription loadHomeData(int pPage, int pPageSize){
        return ApiManager.getInstance().mApis.getDataByType(pPage, pPageSize, Apis.type_meizi)
                .subscribeOn(Schedulers.io())
                .map(new Func1<InfoMeiziData, ArrayList<InfoDaoMeizi>>() {
                    @Override
                    public ArrayList<InfoDaoMeizi> call(InfoMeiziData infoMeiziData) {
                        return infoMeiziData.results;
                    }
                })
                .doOnNext(new Action1<ArrayList<InfoDaoMeizi>>() {
                    @Override
                    public void call(ArrayList<InfoDaoMeizi> infoDaoMeizis) {
                        for (InfoDaoMeizi tInfo : infoDaoMeizis) {
                            if (DaoManager.getInstance().mOrm.queryById(tInfo.objectId, InfoDaoMeizi.class) == null) {
                                DaoManager.getInstance().mOrm.insert(tInfo, ConflictAlgorithm.Replace);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<InfoDaoMeizi>>() {
                    @Override
                    public void call(ArrayList<InfoDaoMeizi> infoGanks) {
                        if(infoGanks==null || infoGanks.size() <= 0){
                            EventBus.post(new EventMeizi(false, "数据为空"));
                        }else {
                            EventBus.post(new EventMeizi(infoGanks));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventBus.post(new EventMeizi(false, ""));
                    }
                });
    }
}
