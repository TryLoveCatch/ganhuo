package io.github.trylovecatch.baselibrary.web;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.BaseActivity;
import io.github.trylovecatch.baselibrary.R;
import io.github.trylovecatch.baselibrary.R2;
import io.github.trylovecatch.baselibrary.utils.UtilsToast;

public class WebBaseActivity extends BaseActivity {
    public static final String EXTRA_URL = "url";

    //===============界面变量==============
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

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
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent tIntent = new Intent();
        if (id == R.id.menu_web) {
            tIntent.setAction(Intent.ACTION_VIEW);
            tIntent.setData(Uri.parse(mUrl));
            if (tIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(tIntent, "请选择浏览器"));
            }else{
                UtilsToast.showShort(this, "没有匹配的程序");
            }
            return true;
        } else if (id == R.id.menu_web_copy) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboardManager =
                        (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(mUrl);
            } else {
                // 得到剪贴板管理器
                android.content.ClipboardManager clipboardManager =
                        (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mUrl));
            }
            UtilsToast.showShort(this, "已复制到剪贴板");
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

        fillView();
    }

    @Override
    public void initData() {
        Bundle tData = getIntent().getExtras();
        if(tData!=null){
            mUrl = tData.getString(EXTRA_URL);
        }

        if(TextUtils.isEmpty(mUrl)){
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
        tData.putString(EXTRA_URL, mUrl);
        setContentFragment(WebBaseFragment.class, tData);
    }

}
