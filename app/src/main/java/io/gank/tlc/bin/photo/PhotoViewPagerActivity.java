package io.gank.tlc.bin.photo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.meizi.MeiziManager;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.bin.photo.adapter.PhotoPagerAdapter;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventMeizi;
import io.gank.tlc.share.view.pinchImageView.PinchImageViewPager;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.L;
import io.gank.tlc.util.UtilString;

public class PhotoViewPagerActivity extends BaseActivity {

    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.photo_viewpager_rlt_root)
    RelativeLayout mRltRoot;
    @Bind(R.id.photo_viewpager_fab)
    FloatingActionButton mFab;
    @Bind(R.id.photo_viewpager_pager)
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
        super.onCreate(savedInstanceState, R.layout.photo_viewpager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            EventBus.register(this);
        }catch(Exception e){}
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            EventBus.unregister(this);
        }catch(Exception e){}
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

        int tCount = 0;
        Bundle tData = getIntent().getExtras();
        if(tData!=null){
            mPosition = tData.getInt(Constant.EXTRA_POSITION, 0);
            tCount = tData.getInt(Constant.EXTRA_COUNT, 0);
        }
        mPage = 0;
        mPageSize = Apis.PAGESIZE;

        if(tCount % mPageSize == 0){
            mNextPage = tCount / mPageSize;
        }else{
            mIsAll = true;
            mNextPage = tCount / mPageSize + 1;
        }

        mPageSize = tCount;
    }

    public void onEvent(EventMeizi pData){
        if(pData.data!=null && isListValid(pData.data)){

            fillView(pData.data);

            mPageSize = Apis.PAGESIZE;
            mPage = mArrData.size() % mPageSize == 0 ? mArrData.size() / mPageSize : mArrData.size() / mPageSize + 1;
        }else{
            if(UtilString.isNotBlank(pData.resultMsg)){
                if (mArrData.size() > 0) {
                    showToast(pData.resultMsg);
                } else {
                    showEmptyView(mRltRoot, TYPE_NO_DATA);
                }
            }else {
                if (mArrData.size() > 0) {
                    showToast(R.string.msg_top_fail);
                } else {
                    showEmptyView(mRltRoot, TYPE_ERROR);
                }
            }
        }
    }
    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        addSubscription(MeiziManager.getInstatnce().loadHomeData(mPage, mPageSize));
    }

    private void fillView(List<InfoDaoMeizi> pArrInfos){

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
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(mPosition);
                    mPosition = -1;
                }
            }, 100);
        }

        mIsLoadMore = false;
    }
}
