package io.gank.tlc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.gank.tlc.framework.BaseFragment;
import io.gank.tlc.framework.event.EventBus;

public class AAFragment extends BaseFragment{
    
    //===============界面变量==============
    //===============逻辑变量==============
    //===============生命周期==============
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.activity_main);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        try{
            EventBus.register(this);
        }catch(Exception e){}
    }
    
    @Override
    public void onPause() {
        super.onPause();
        try{
            EventBus.unregister(this);
        }catch(Exception e){}
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    //===============事件接口==============
    @Override
    public void initViewProperty() {
        
    }

    @Override
    public void initData() {
        loadData();
    }
    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        
    }
    
    private void fillView(){
        
    }
}
