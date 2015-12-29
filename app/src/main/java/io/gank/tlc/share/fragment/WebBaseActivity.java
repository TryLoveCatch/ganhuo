package io.gank.tlc.share.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.UtilString;

public class WebBaseActivity extends BaseActivity  {

    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
//    @Bind(R.id.fab)
//    FloatingActionButton mFab;

    //===============逻辑变量==============
    private String mUrl;
    //===============生命周期==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.web_base);
    }



    //===============事件接口==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent tIntent = new Intent();
        switch (id){
            case R.id.menu_web:
                tIntent.setAction(Intent.ACTION_VIEW);
                tIntent.setData(Uri.parse(mUrl));
                if(tIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(tIntent);
                }
//                startActivity(Intent.createChooser(tIntent, "请选择浏览器"));
                return true;
            case R.id.menu_web_copy:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
                    android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(mUrl);
                }else{
                    // 得到剪贴板管理器
                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mUrl));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        fillView();
    }

    @Override
    public void initData() {
        Bundle tData = getIntent().getExtras();
        if(tData!=null){
            mUrl = tData.getString(Constant.EXTRA_URL);
        }

        if(UtilString.isBlank(mUrl)){
            finish();
        }
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
        tData.putString(Constant.EXTRA_URL, mUrl);
        setContentFragment(WebBaseFragment.class, tData);
    }

}
