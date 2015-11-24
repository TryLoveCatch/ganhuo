package io.gank.tlc.bin.home;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import io.gank.tlc.bin.home.info.InfoMeizi;
import io.gank.tlc.bin.home.view.HomeHView;
import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.share.fragment.RecylerFragment;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HomeFragment extends RecylerFragment{
    
    //===============界面变量==============
    //===============逻辑变量==============
    //===============生命周期==============
    //===============事件接口==============
    @Override
    protected List<? extends InfoBase> loadDB(){
        QueryBuilder tQuery = new QueryBuilder(InfoDaoMeizi.class);
        tQuery.appendOrderDescBy("updatedAt");
        tQuery.limit(0, Apis.PAGESIZE);
        return DaoManager.getInstance().mOrm.query(tQuery);


    }

    @Override
    protected void loadNet() {
        Subscription tSub = ApiManager.getInstance().mApis.getMeizi(mPage, Apis.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .map(new Func1<InfoMeizi, ArrayList<InfoDaoMeizi>>() {
                    @Override
                    public ArrayList<InfoDaoMeizi> call(InfoMeizi infoMeizi) {
                        return infoMeizi.results;
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
                        loadSuc(infoGanks);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        loadFail();
                    }
                });
        addSubscription(tSub);
    }

    @Override
    protected void setHolderView() {
        mAdp.setHolderViews(HomeHView.class);
    }
    //===============对外方法==============
    //===============私有方法==============
//    private void startPictureActivity(InfoGank pInfo, View pView) {
//        Intent tIntent = new Intent(getActivity(), PhotoActivity.class);
//        tIntent.putExtra(Constant.EXTRA_URL, pInfo.url);
//        tIntent.putExtra(Constant.EXTRA_NAME, pInfo.desc);
//
//        ActivityOptionsCompat optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pView,
//                        getString(R.string.transition_share_photo));
//        try {
//            ActivityCompat.startActivity(getActivity(), tIntent, optionsCompat.toBundle());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            startActivity(tIntent);
//        }
//    }
}
