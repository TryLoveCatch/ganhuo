package io.github.trylovecatch.gank.text;

import java.util.List;

import android.text.TextUtils;
import io.github.trylovecatch.baselibrary.list.BaseRecyclePresenter;
import io.github.trylovecatch.baselibrary.list.EventRecycle;
import io.github.trylovecatch.baselibrary.list.IRecyclerBaseView;
import io.github.trylovecatch.baselibrary.retrofit.ApiRetrofit;
import io.github.trylovecatch.baselibrary.rxjava.RxErrorObserver;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.LifecycleEvent;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.RxLifecycle;
import io.github.trylovecatch.gank.info.InfoGank;
import io.github.trylovecatch.gank.service.ApiServeces;
import io.github.trylovecatch.gank.service.RxjavaServices;

/**
 * Created by lipeng21 on 2017/6/14.
 */

public class TextPresenter extends BaseRecyclePresenter<IRecyclerBaseView> {

    private String type;

    @Override
    public void loadData(boolean pIsForceRefresh, int pPage, int pPageSize) {
        IRecyclerBaseView tView = getView();
        if(tView==null){
            return;
        }
        if(TextUtils.isEmpty(type)){
            return;
        }
        ApiRetrofit.getInstance().get(ApiServeces.class).getDataByType(pPage, pPageSize, type)
                .compose(RxjavaServices.<List<InfoGank>>handleResult())
                .compose(RxLifecycle.bind(tView).toObservable(LifecycleEvent.DESTORY_VIEW))
                .subscribe(new RxErrorObserver<List<InfoGank>>() {
                    @Override
                    protected void _onNext(List<InfoGank> infoGanks) {
                        EventRecycle tEvent = new EventRecycle(true, null, false);
                        tEvent.infos = infoGanks;
                        IRecyclerBaseView tView = getView();
                        if(tView!=null) {
                            tView.fillView(tEvent);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        EventRecycle tEvent = new EventRecycle(false, message, false);
                        IRecyclerBaseView tView = getView();
                        if(tView!=null) {
                            tView.fillView(tEvent);
                        }
                    }
                });
    }

    @Override
    protected void init() {

    }

    public void setType(String pType){
        type = pType;
    }
}
