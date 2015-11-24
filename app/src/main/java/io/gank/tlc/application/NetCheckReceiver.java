package io.gank.tlc.application;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class NetCheckReceiver{
    BaseBroadcast mReceiver;
    Context mContext;
    NetChangeFilter mFilter;
    
    NetCheckReceiver(Context context){
    	mContext = context;
        this.mFilter = new NetChangeFilter();
    }
    public void register(){
        mReceiver = new BaseBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mReceiver, filter);
    }
    public void unRegister(){
        mContext.unregisterReceiver(mReceiver);
    }
    
    public NetChangeFilter getFilter(){
    	return mFilter;
    }
    
    private class BaseBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {// 网络连接
                mFilter.netChange();
            }
        }
    }
}
