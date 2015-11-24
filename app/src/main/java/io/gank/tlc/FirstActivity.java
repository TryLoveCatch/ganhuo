package io.gank.tlc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.gank.tlc.framework.BaseActivity;

public class FirstActivity extends BaseActivity{
    
    //===============界面变量==============
    private Handler mHandler;
    //===============逻辑变量==============
    //===============生命周期==============
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setForbidStartActivityAnimation(true);
        super.onCreate(savedInstanceState, R.layout.first);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    
    //===============事件接口==============
    @Override
    public void initViewProperty() {
        
    }

    @Override
    public void initData() {

        mHandler = new Handler();
    }
    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        mHandler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                Intent tIntent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(tIntent);
                finish();
            }
        }, 3000);
    }
}
