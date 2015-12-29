package io.gank.tlc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import io.gank.tlc.bin.home.HomeFragment;
import io.gank.tlc.bin.meizi.MeiziFragment;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.bin.text.TextFragment;
import io.gank.tlc.bin.video.VideoFragment;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.framework.IToolbarAndFab;
import io.gank.tlc.util.Constant;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static int INDEX_HOME = 0;
    private final static int INDEX_MEIZI = 1;
    private final static int INDEX_ANDROID = 2;
    private final static int INDEX_IOS = 3;
    private final static int INDEX_RECOMMEND = 4;
    private final static int INDEX_EXPAND = 5;
    private final static int INDEX_VIDEO = 6;
    private final static int INDEX_APP = 7;

    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    //===============逻辑变量==============
    private int mIndex;
    //===============生命周期==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
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
//        else (id == android.R.id.home){
//            if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
//                mDrawerLayout.closeDrawers();
//            }else{
//                mDrawerLayout.setdraw
//            }
//        }
        switch (id){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawers();
                }else{
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
        }else {
            showExitDialog();
        }
    }

    @Override
    public void initViewProperty() {
        setForbidFinishActivityGesture(true);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tFrg = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (tFrg instanceof IToolbarAndFab) {
                    ((IToolbarAndFab) tFrg).onToolbarClicked(mToolbar);
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment tFrg = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (tFrg instanceof IToolbarAndFab) {
                    ((IToolbarAndFab) tFrg).onFabClicked(mFab);
                }
            }
        });


        ActionBarDrawerToggle tDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        tDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(tDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        fillView();
    }

    @Override
    public void initData() {
        mIndex = INDEX_HOME;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                mIndex = INDEX_HOME;
                break;
            case R.id.item_meizi:
                mIndex = INDEX_MEIZI;
                break;
            case R.id.item_android:
                mIndex = INDEX_ANDROID;
                break;
            case R.id.item_ios:
                mIndex = INDEX_IOS;
                break;
            case R.id.item_expand:
                mIndex = INDEX_EXPAND;
                break;
            case R.id.item_recommend:
                mIndex = INDEX_RECOMMEND;
                break;
            case R.id.item_video:
                mIndex = INDEX_VIDEO;
                break;
            case R.id.item_app:
                mIndex = INDEX_APP;
                break;
        }
        switchFragment();
        item.setCheckable(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){

    }

    private void fillView(){
        switchFragment();
    }

    private void switchFragment(){
        Bundle tData = new Bundle();
        switch (mIndex){
            case INDEX_HOME:
                setContentFragment(HomeFragment.class);
                break;
            case INDEX_MEIZI:
                setContentFragment(MeiziFragment.class);
                break;
            case INDEX_ANDROID:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_android);
                setContentFragment(TextFragment.class, tData);
                break;
            case INDEX_IOS:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_ios);
                setContentFragment(TextFragment.class, tData);
                break;
            case INDEX_EXPAND:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_expand);
                setContentFragment(TextFragment.class, tData);
                break;
            case INDEX_RECOMMEND:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_recommend);
                setContentFragment(TextFragment.class, tData);
                break;
            case INDEX_APP:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_app);
                setContentFragment(TextFragment.class, tData);
                break;
            case INDEX_VIDEO:
                tData.putString(Constant.EXTRA_TYPE, Apis.type_video);
                setContentFragment(VideoFragment.class, tData);
                break;

        }
    }

    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_exit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getApplication().onTerminate();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setMessage(R.string.msg_exit);
        AlertDialog tDialog = builder.create();
        tDialog.show();
    }

}
