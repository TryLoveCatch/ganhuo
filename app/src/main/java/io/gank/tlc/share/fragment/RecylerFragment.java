package io.gank.tlc.share.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.framework.BaseFragment;
import io.gank.tlc.framework.IToolbarAndFab;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.framework.view.BaseAdapter;
import io.gank.tlc.util.UtilManager;

public abstract class RecylerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IToolbarAndFab {

    //===============界面变量==============
    @Bind(R.id.recyler_swp)
    SwipeRefreshLayout mSwp;
    @Bind(R.id.recyler_rcl)
    RecyclerView mRcl;
    protected BaseAdapter mAdp;
    private RecyclerView.OnScrollListener mScrollListener;

    @Bind(R.id.recyler_lin_ad)
    LinearLayout mLinAds;
    @Bind(R.id.recyler_adView_google)
    AdView mGoogleAdView;

    com.facebook.ads.AdView mFbAdView;
    //===============逻辑变量==============
    protected List<InfoBase> mArrData;
    protected int mPage;

    private boolean mIsResumed;
    private boolean mIsAll;//已经没有更多了
    private boolean mIsLoadMore;
    private boolean mIsRefresh;
    private boolean mIsSmooth;
    //===============生命周期==============
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.recyler);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mIsResumed) {
            mIsResumed = true;
            //加载db缓存
            List<? extends InfoBase> tArr = loadDB();
            if(isListValid(tArr)) {
                mArrData.addAll(tArr);
                mAdp.notifyDataSetChanged();
            }

            if(isListValid(mArrData)) {
                setRefreshing(true, 300);
                mRcl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }, 1000);
            }else{
                showEmptyView(mSwp, TYPE_LOADING);
                refresh();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        clear();
        super.onDestroy();
    }

    //===============事件接口==============
    @Override
    public void initViewProperty() {
        mSwp.setOnRefreshListener(this);
        mSwp.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        StaggeredGridLayoutManager tLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        tLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRcl.setLayoutManager(tLayoutManager);
        mAdp = new BaseAdapter(getActivity());
        mAdp.setData(mArrData);
        setHolderView();
        mRcl.setAdapter(mAdp);
//        mRcl.setItemAnimator(new DefaultItemAnimator());
        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] tArr = ((StaggeredGridLayoutManager)recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPositions(null);
                int tLastVisibleItem = findMax(tArr);
                int tItemCount = mAdp.getItemCount();
//                L.e("tLastVisibleItem," + tLastVisibleItem + " tItemCount," + tItemCount + " mPage," + mPage + " mIsAll," + mIsAll);
                if(tLastVisibleItem >= tItemCount - 1){

                    if(mIsAll){
                        showToast("没有更多内容了");
                        return;
                    }

                    if(!mIsLoadMore && !mIsRefresh && !mSwp.isRefreshing()) {
                        mIsLoadMore = true;
                        mPage++;
                        loadData();
                    }
                }

                //滚动
                //解决smoothScrollToPosition 不能滚到头的问题
                if(mIsSmooth){
                    int[] tArrSmooth = ((StaggeredGridLayoutManager)recyclerView.getLayoutManager())
                            .findFirstCompletelyVisibleItemPositions(null);
                    if(findMin(tArrSmooth) == 0){
                        mIsSmooth = false;
                        mAdp.notifyDataSetChanged();
                    }else{
                        mRcl.smoothScrollToPosition(0);
                    }
                }
            }
        };
        mRcl.addOnScrollListener(mScrollListener);

        mEmptyView.setOnRefreshListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage = 1;
                loadData();
            }
        });


        /*//google 广告
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("D76F87DF8E4064B61C82039722CDD8EA")
                .build();
        mGoogleAdView.loadAd(adRequest);

        //Facebook广告 需要翻墙
        AdSettings.addTestDevice("93e430c13ec2a5a3e2e3929bef1f8761");
        mFbAdView = new com.facebook.ads.AdView(getActivity(), getString(R.string.banner_ad_facebook_id), AdSize.BANNER_HEIGHT_50);
        mFbAdView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                L.e("" + adError.getErrorCode());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                L.e("facebook ad loaded" );
            }

            @Override
            public void onAdClicked(Ad ad) {
                L.e("facebook ad clicked" );
            }
        });
        mFbAdView.loadAd();
        mLinAds.addView(mFbAdView);*/
    }

    @Override
    public void initData() {
        mArrData = new ArrayList<>();
        mPage = 1;
        mIsAll = false;
        mIsLoadMore = false;
        mIsSmooth = false;
        mIsRefresh = false;
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onToolbarClicked(Toolbar pToolbar) {
        if(mIsLoadMore || mSwp.isRefreshing()) return;
        mIsSmooth = true;
        mRcl.smoothScrollToPosition(0);
    }

    @Override
    public void onFabClicked(FloatingActionButton pFab) {
        mRcl.scrollToPosition(0);
        setRefreshing(true, 10);
        refresh();
    }
    //===============对外方法==============

    protected abstract List<? extends InfoBase> loadDB();
    protected abstract void loadNet();
    // mAdp.setHolderViews(HomeHView.class);
    protected abstract void setHolderView();

    protected void loadSuc(ArrayList<? extends InfoBase> pArrInfos){
        if (mArrData.size() == 0 && pArrInfos.size() == 0) {
            showEmptyView(mSwp, TYPE_NO_DATA);
            return;
        }
        fillView(pArrInfos);
    }

    protected void loadFail(){
        if (mArrData.size() > 0) {
            showToast(R.string.msg_top_fail);
        } else {
            showEmptyView(mSwp, TYPE_ERROR);
        }
    }

    //===============私有方法==============
    private void loadData(){

        if(!UtilManager.getInstance().mUtilNet.isNetAvailable()){
            if(isListValid(mArrData)){
                showToast(R.string.msg_top_nonet);
            }else {
                showEmptyView(mSwp, TYPE_NO_NET);
            }
            setRefreshing(false, 1000);
            return;
        }

        loadNet();
    }

    private void fillView(ArrayList<? extends InfoBase> pArrInfos){
        if(mEmptyView.getVisibility()==View.VISIBLE) {
            hideEmptyView(mSwp);
        }

        if (pArrInfos.size() < Apis.PAGESIZE) {
            mIsAll = true;
        }

        if(mIsRefresh){
            mArrData.clear();
        }

        mArrData.addAll(pArrInfos);
        mAdp.notifyDataSetChanged();

        mIsLoadMore = false;
        mIsRefresh = false;

        // 防止刷新消失太快
        setRefreshing(false, 1000);
    }

    private void refresh(){
        if(!mIsLoadMore){
            mIsRefresh = true;
            mPage = 1;
            loadData();
        }
    }

    private void clear(){
        mRcl.removeOnScrollListener(mScrollListener);
    }

    private int findMax(int[] pArr){
        int tResult = pArr[pArr.length - 1];
        for(int i : pArr){
            if(tResult < i){
                tResult = i;
            }
        }
        return tResult;
    }

    private int findMin(int[] pArr){
        int tResult = pArr[0];
        for(int i : pArr){
            if(tResult > i){
                tResult = i;
            }
        }
        return tResult;
    }

    //解决SwipeRefreshLayout setRefreshing()不管用的问题
    private void setRefreshing(final boolean pRefreshing, final long pDelayMillis){
        mSwp.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwp.setRefreshing(pRefreshing);
            }
        }, pDelayMillis);
    }
}
