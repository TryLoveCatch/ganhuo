package io.gank.tlc.bin.photo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.home.info.InfoMeizi;
import io.gank.tlc.bin.net.ApiManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.bin.photo.adapter.PhotoPagerAdapter;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.share.view.pinchImageView.PinchImageViewPager;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.L;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PhotoActivity extends BaseActivity {

    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.photo_rlt_root)
    RelativeLayout mRltRoot;
    @Bind(R.id.photo_fab)
    FloatingActionButton mFab;
    @Bind(R.id.photo_pager)
    PinchImageViewPager mViewPager;
    private PhotoPagerAdapter mAdp;

    //===============逻辑变量==============
    private List<InfoDaoMeizi> mArrData;
    private int mPosition;
    private int mPage;
    private int mNextPage;
    private int mPageSize;
    private boolean mIsLoadMore;
    private boolean mIsAll;
    //===============生命周期==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.photo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    //===============事件接口==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewProperty() {
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAdp = new PhotoPagerAdapter(this, mViewPager);
        mAdp.setData(mArrData);
        mViewPager.setAdapter(mAdp);
        mViewPager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        mArrData = new ArrayList<>();
        mIsLoadMore = false;

        Bundle tData = getIntent().getExtras();
        if(tData!=null){
            mPosition = tData.getInt(Constant.EXTRA_POSITION, 0);
        }
        mPage = 0;
        if(mPosition > 0){
            mNextPage = mPosition % (Apis.PAGESIZE - 1) == 0 ?
                    mPosition / (Apis.PAGESIZE - 1) + 1
                    :
                    mPosition / (Apis.PAGESIZE - 1);
            mPageSize = mNextPage * Apis.PAGESIZE;
        }else{
            mPageSize = Apis.PAGESIZE;
            mNextPage = 0;
        }
    }

    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        Subscription tSub = ApiManager.getInstance().mApis.getMeizi(mPage, mPageSize)
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
                    public void call(ArrayList<InfoDaoMeizi> pArrInfos) {
                        if (mArrData.size() == 0 && pArrInfos.size() == 0) {
                            showEmptyView(mRltRoot, TYPE_NO_DATA);
                            return;
                        }
                        if(mPageSize!=Apis.PAGESIZE) {
                            mPageSize = Apis.PAGESIZE;
                            mPage = mNextPage;
                        }
                        fillView(pArrInfos);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (mArrData.size() > 0) {
                            showToast(R.string.msg_top_fail);
                        } else {
                            showEmptyView(mRltRoot, TYPE_ERROR);
                        }
                    }
                });
        addSubscription(tSub);
    }

    private void fillView(ArrayList<InfoDaoMeizi> pArrInfos){
        if(mEmptyView.getVisibility()==View.VISIBLE) {
            hideEmptyView(mRltRoot);
        }

        if (pArrInfos.size() < Apis.PAGESIZE) {
            mIsAll = true;
        }

        mArrData.addAll(pArrInfos);
        mAdp.notifyDataSetChanged();
        L.e("position: " + mPosition);
        if(mPosition!=-1) {
            mViewPager.setCurrentItem(mPosition);
            mPosition = -1;
        }

        mIsLoadMore = false;
    }
}
