package io.gank.tlc.bin.detail;

import java.util.Calendar;
import java.util.Date;

import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventDetail;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class DetailManager {

    private static DetailManager mInstatnce;
    private DetailManager(){}

    public static DetailManager getInstatnce(){
        if(mInstatnce==null){
            synchronized (DetailManager.class){
                if(mInstatnce==null){
                    mInstatnce = new DetailManager();
                }
            }
        }
        return mInstatnce;
    }
    public Subscription loadDetailData(Date pDate){
        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(pDate);
        int tYear = tCalendar.get(Calendar.YEAR);
        int tMonth = tCalendar.get(Calendar.MONTH) + 1;
        int tDay = tCalendar.get(Calendar.DAY_OF_MONTH);
        return ApiManager.getInstance().mApis.getAllDataByDate(tYear + "", tMonth + "", tDay + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InfoDetail>() {
                    @Override
                    public void call(InfoDetail infoDetail) {
                        if(infoDetail==null || infoDetail.results == null){
                            EventBus.post(new EventDetail(false, "数据为空"));
                        }else {
                            EventBus.post(new EventDetail(infoDetail));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        EventBus.post(new EventDetail(false, ""));
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
