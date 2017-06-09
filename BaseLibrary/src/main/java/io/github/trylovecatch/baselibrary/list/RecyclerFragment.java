package io.github.trylovecatch.baselibrary.list;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.IToolbarAndFab;
import io.github.trylovecatch.baselibrary.R;
import io.github.trylovecatch.baselibrary.R2;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.list.footer.FooterAdapter;
import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.mvp.MvpFragment;

public abstract class RecyclerFragment<V extends IRecyclerBaseView, T extends BaseRecyclePresenter<V>>
        extends MvpFragment<V, T>
        implements SwipeRefreshLayout.OnRefreshListener, IToolbarAndFab, IRecyclerBaseView {

    public static final int PAGE_SIZE = 20;

    //===============界面变量==============
    @BindView(R2.id.recyler_swp)
    SwipeRefreshLayout mSwp;
    @BindView(R2.id.recyler_rcl)
    RecyclerView mRcl;
    protected BaseAdapter mAdp;
    protected RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.OnScrollListener mScrollListener;
    //===============逻辑变量==============
    protected List<InfoBase> mArrData;
    protected int mPage;

    private boolean mIsAll;//已经没有更多了
    private boolean mIsLoadMore;
    private boolean mIsRefresh;
    private boolean mIsSmooth;

    //===============生命周期==============
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.recycler);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
//        //加载db缓存
//        List<? extends InfoBase> tArr = loadDB();
//        if(isListValid(tArr)) {
//            mArrData.addAll(tArr);
//            mAdp.notifyDataSetChanged();
//        }
//
//        if(isListValid(mArrData)) {
//            setRefreshing(true, 300);
//            mRcl.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    refresh();
//                }
//            }, 1000);
//        }else{
//            showEmptyView(mSwp, TYPE_LOADING);
//            refresh();
//        }

        showLoadingView(mSwp);
        refresh(false);
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

        mLayoutManager = getLayoutManager();
        mRcl.setLayoutManager(mLayoutManager);
//        mAdp = new BaseAdapter(getActivity());
        mAdp = new FooterAdapter(getActivity());
        mAdp.setData(mArrData);
        setHolderView();
        MyItemDecoration tItemDec = new MyItemDecoration();
        tItemDec.setSpace(getItemSpace());
        mRcl.addItemDecoration(tItemDec);
        mRcl.setAdapter(mAdp);
//        mRcl.setItemAnimator(new DefaultItemAnimator());
        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int tLastVisibleItem = 0;

                if(mLayoutManager instanceof  StaggeredGridLayoutManager){
                    int[] tArr = ((StaggeredGridLayoutManager)mLayoutManager)
                            .findLastCompletelyVisibleItemPositions(null);
                    tLastVisibleItem = findMax(tArr);
                }else if(mLayoutManager instanceof LinearLayoutManager){
                    tLastVisibleItem = ((LinearLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
                }

                int tItemCount = mAdp.getItemCount();
//                L.e("tLastVisibleItem," + tLastVisibleItem + " tItemCount," + tItemCount + " mPage," + mPage + " mIsAll," + mIsAll);
                if(tLastVisibleItem >= tItemCount - 1){

                    if(mIsAll){
                        showToast(R.string.no_more_data);
                        return;
                    }
                    loadMore();
                }

                //滚动
                //解决smoothScrollToPosition 不能滚到头的问题
                if(mIsSmooth){
                    int tFirstVisibleItem = 0;
                    if(mLayoutManager instanceof  StaggeredGridLayoutManager){
                        int[] tArr = ((StaggeredGridLayoutManager)mLayoutManager)
                                .findFirstCompletelyVisibleItemPositions(null);
                        tFirstVisibleItem = findMin(tArr);
                    }else if(mLayoutManager instanceof LinearLayoutManager){
                        tFirstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstCompletelyVisibleItemPosition();
                    }

                    if(tFirstVisibleItem == 0){
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
                refresh(true);
            }
        });
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
        refresh(true);
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
        refresh(true);
    }

    //=================MVC-V==================
    @Override
    public void fillView(EventRecycle pEvent) {

        if(mIsLoadMore) {
            if (pEvent == null || !pEvent.isSuc || !isListValid(pEvent.infos)) {

                if(pEvent==null || TextUtils.isEmpty(pEvent.errorMsg)) {
                    showToast(R.string.msg_top_fail);
                }else{
                    showToast(pEvent.errorMsg);
                }
                return;
            }

            if(pEvent.infos.size() < PAGE_SIZE){
                if(mAdp!=null && mAdp instanceof FooterAdapter){
                    ((FooterAdapter)mAdp).setIsHasMore(false);
                }
                mIsAll = true;
            }

            mArrData.addAll(pEvent.infos);
            mAdp.notifyDataSetChanged();

            mIsLoadMore = false;

        }else if(mIsRefresh){

            if (pEvent == null || !pEvent.isSuc || !isListValid(pEvent.infos)) {


                if (mArrData.size() > 0) {
                    if(pEvent==null || TextUtils.isEmpty(pEvent.errorMsg)) {
                        showToast(R.string.msg_top_fail);
                    }else{
                        showToast(pEvent.errorMsg);
                    }
                }else{
                    if(pEvent==null || TextUtils.isEmpty(pEvent.errorMsg)) {
                        showErrorView(mSwp, R.string.msg_top_fail);
                    }else{
                        showErrorView(mSwp, pEvent.errorMsg);
                    }
                }

                // 防止刷新消失太快
                setRefreshing(false, 1000);
                mIsRefresh = false;

                return;
            }

            hideEmptyView(mSwp);

            if(pEvent.infos.size() < PAGE_SIZE){
                if(mAdp!=null && mAdp instanceof FooterAdapter){
                    ((FooterAdapter)mAdp).setIsHasMore(false);
                }
                mIsAll = true;
            }

            mArrData.clear();
            mArrData.addAll(pEvent.infos);
            mAdp.notifyDataSetChanged();

            if(!pEvent.isFromCache) {
                // 防止刷新消失太快
                setRefreshing(false, 1000);
                mIsRefresh = false;
            }

        }else{
            Logger.e("没有刷新，没有加载更多，这个是什么鬼");
        }
    }

    //===============对外方法==============

    protected abstract void setHolderView();
    protected abstract int getItemSpace();
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected void clear(){
        mRcl.removeOnScrollListener(mScrollListener);
    }

    //===============私有方法==============
//    private void loadData(){
//
//        if(!UtilNet.getInstance().isNetAvailable(getContext())){
//            if(isListValid(mArrData)){
//                showToast(R.string.msg_top_nonet);
//            }else {
//                showEmptyView(mSwp, TYPE_NO_NET);
//            }
//            setRefreshing(false, 1000);
//            return;
//        }
//
//        loadNet();
//    }


    private void refresh(boolean pIsForceRefresh){
        if(!mIsLoadMore && !mIsRefresh) {
            mIsRefresh = true;
            mIsAll = false;
            mPage = 1;
            mPresenter.loadData(pIsForceRefresh, mPage, PAGE_SIZE);
        }
    }

    private void loadMore(){
        if(!mIsLoadMore && !mIsRefresh && !mSwp.isRefreshing()) {
            mIsLoadMore = true;
            mPage++;
            mPresenter.loadData(true, mPage, PAGE_SIZE);
        }
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


    public class MyItemDecoration extends RecyclerView.ItemDecoration{

        private int space;
        public void setSpace(int space){
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(space > 0 ){
                if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                    if(parent.getChildLayoutPosition(view) != 0) {
                        outRect.top = space;
                    }
                } else {
                    outRect.left = space;
                    if(parent.getChildLayoutPosition(view) != 0) {
                        outRect.top = space;
                    }
                }
            }
        }
    }
}
