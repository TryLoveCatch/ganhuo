package io.gank.tlc.bin.detail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.detail.view.DetailHView;
import io.gank.tlc.bin.detail.view.DetailTitleHView;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.share.event.EventDetail;
import io.gank.tlc.share.event.EventDetailClicked;
import io.gank.tlc.share.fragment.SharedFragmentActivity;
import io.gank.tlc.share.fragment.WebBaseActivity;
import io.gank.tlc.share.fragment.WebPlayFragment;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.UtilManager;
import io.gank.tlc.util.UtilString;

public class DetailActivity extends BaseActivity {

    //===============界面变量==============
    @Bind(R.id.detail_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.detail_img)
    ImageView mImg;
    @Bind(R.id.detail_collapsing)
    CollapsingToolbarLayout mCollapsing;


    @Bind(R.id.recyler_rcl)
    RecyclerView mRcl;
    protected DetailAdapter mAdp;

    //===============逻辑变量==============
    private boolean mIsResumed;

    private String mUrlPic;
    private String mTitle;
    private Date mDate;
    protected List<InfoBase> mArrData;
    //===============生命周期==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.home_detail);
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            EventBus.register(this);
        }catch (Exception e){}

        if(!mIsResumed) {
            mIsResumed = true;
            loadData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            EventBus.unregister(this);
        }catch (Exception e){}
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
        switch (id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initViewProperty() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mCollapsing.setTitle(mTitle);
        mCollapsing.setExpandedTitleColor(Color.GRAY);
        mCollapsing.setCollapsedTitleTextColor(Color.WHITE);

        Glide.with(this)
                .load(mUrlPic)
                .into(mImg);


        mRcl.setLayoutManager(new LinearLayoutManager(this));
        mAdp = new DetailAdapter(this);
        mAdp.setData(mArrData);
        mAdp.setHolderViews(DetailTitleHView.class, DetailHView.class);
        mRcl.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = getResources().getDimensionPixelSize(R.dimen.dp_12);
            }
        });
        mRcl.setAdapter(mAdp);

    }

    @Override
    public void initData() {
        mArrData = new ArrayList<>();
        Bundle tData = getIntent().getExtras();
        if(tData!=null){
            mUrlPic = tData.getString(Constant.EXTRA_URL);
            mTitle = tData.getString(Constant.EXTRA_NAME);
            mDate = (Date)tData.getSerializable(Constant.EXTRA_INFO);
        }

        if(UtilString.isBlank(mUrlPic)){
            finish();
        }
    }

    public void onEvent(EventDetail pData){
        if(pData.data!=null){
            loadSuc(pData.data);
        }else{
            loadFail();
        }
    }

    public void onEvent(EventDetailClicked pData){
        startDetailActivity(pData.url, pData.isVideo);
    }
    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        if(!UtilManager.getInstance().mUtilNet.isNetAvailable()){
            showEmptyView(mRcl, TYPE_NO_NET);
            return;
        }

        showEmptyView(mRcl, TYPE_LOADING);
        DetailManager.getInstatnce().loadDetailData(mDate);
    }

    private void fillView(){
        if(mEmptyView.getVisibility()==View.VISIBLE) {
            hideEmptyView(mRcl);
        }

    }

    private void loadSuc(InfoDetail pInfo){
        if(pInfo==null || pInfo.results==null){
            showEmptyView(mRcl, TYPE_NO_DATA);
            return;
        }
        mArrData.clear();
        InfoDetailTitle tInfoTitle = null;
        if(isListValid(pInfo.results.androidList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "Android";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.androidList);
        }
        if(isListValid(pInfo.results.iosList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "iOS";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.iosList);
        }
        if(isListValid(pInfo.results.recommendList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "瞎推荐";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.recommendList);
        }
        if(isListValid(pInfo.results.expandList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "拓展资源";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.expandList);
        }
        if(isListValid(pInfo.results.videoList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "休息视频";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.videoList);
        }
        if(isListValid(pInfo.results.appList)){
            tInfoTitle = new InfoDetailTitle();
            tInfoTitle.title = "App";
            mArrData.add(tInfoTitle);
            mArrData.addAll(pInfo.results.appList);
        }
        fillView();
    }

    private void loadFail(){
        if (mArrData.size() > 0) {
            showToast(R.string.msg_top_fail);
        } else {
            showEmptyView(mRcl, TYPE_ERROR);
        }
    }

    private void startDetailActivity(String pUrl, boolean pIsVideo) {
        Bundle tData = new Bundle();
        tData.putString(Constant.EXTRA_URL, pUrl);

        if(pIsVideo){
            SharedFragmentActivity.startFragmentActivity(this, WebPlayFragment.class, tData);
        }else{
            Intent tIntent = new Intent(this, WebBaseActivity.class);
            tIntent.putExtras(tData);
            this.startActivity(tIntent);
        }
    }

}
