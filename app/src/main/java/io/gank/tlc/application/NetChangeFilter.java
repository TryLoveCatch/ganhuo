package io.gank.tlc.application;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

import io.gank.tlc.util.L;
import io.gank.tlc.util.UtilManager;

public class NetChangeFilter {
	private final static String TAG = NetChangeFilter.class.getSimpleName();

    private NetworkInfo mOldNet;
    private Set<OnNetChangeListener> mNetListeners;
    
    NetChangeFilter(){
        this.mOldNet = UtilManager.getInstance().mUtilNet.getNetWorkInfo();
        this.mNetListeners = new HashSet<OnNetChangeListener>();
    }
    
    public void netChange(){
        NetworkInfo tNet = UtilManager.getInstance().mUtilNet.getNetWorkInfo();
        
        if(mOldNet==null){
            if(tNet==null){
                L.i(TAG, "null ————> null");
                return;
            }
            if(tNet.getType()==ConnectivityManager.TYPE_MOBILE){
                L.i(TAG, "null ————> mobile");
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }else if(tNet.getType() == ConnectivityManager.TYPE_WIFI){
                L.i(TAG, "null ————> wifi");
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }else{
                L.i(TAG, "null ————> " + tNet.getTypeName());
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }
        }else {
            if(tNet==null){
                if(mOldNet.getType()==ConnectivityManager.TYPE_MOBILE){
                    L.i(TAG, "mobile ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }else if(mOldNet.getType() == ConnectivityManager.TYPE_WIFI){
                    L.i(TAG, "wifi ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }else{
                    L.i(TAG, mOldNet.getTypeName() + " ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }
            }else{
                int tOld = mOldNet.getType();
                int tNew = tNet.getType();
                
                if(tOld!=tNew){
                    if(tOld==ConnectivityManager.TYPE_MOBILE && tNew==ConnectivityManager.TYPE_WIFI){
                        L.i(TAG, "mobile ————> wifi");
                    }else if(tOld==ConnectivityManager.TYPE_WIFI && tNew==ConnectivityManager.TYPE_MOBILE){
                        L.i(TAG, "wifi ————> mobile");
                    }else{
                        L.i(TAG, tOld + " ————> " + tNew);
                    }
                }else{
                    L.i(TAG, "same===============" + mOldNet.getTypeName());
                }
            }
        }
        mOldNet = tNet;
    }
    
    
    public void addListener(OnNetChangeListener pOnNetChangeListener){
    	this.mNetListeners.add(pOnNetChangeListener);
    }
    public void removeListener(OnNetChangeListener pOnNetChangeListener){
    	this.mNetListeners.remove(pOnNetChangeListener);
    }
}

