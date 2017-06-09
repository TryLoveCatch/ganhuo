package io.github.trylovecatch.baselibrary.application;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.utils.UtilNet;

public class NetChangeFilter {
    private NetworkInfo mOldNet;
    private Set<OnNetChangeListener> mNetListeners;
    
    NetChangeFilter(){
        this.mOldNet = UtilNet.getInstance().getNetWorkInfo(BaseApplication.mContext);
        this.mNetListeners = new HashSet<>();
    }
    
    public void netChange(){
        NetworkInfo tNet = UtilNet.getInstance().getNetWorkInfo(BaseApplication.mContext);
        
        if(mOldNet==null){
            if(tNet==null){
                Logger.i("null ————> null");
                return;
            }
            if(tNet.getType()==ConnectivityManager.TYPE_MOBILE){
                Logger.i("null ————> mobile");
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }else if(tNet.getType() == ConnectivityManager.TYPE_WIFI){
                Logger.i("null ————> wifi");
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }else{
                Logger.i("null ————> " + tNet.getTypeName());
                for(OnNetChangeListener tListener : mNetListeners){
                	tListener.onNetChanged(true);
				}
            }
        }else {
            if(tNet==null){
                if(mOldNet.getType()==ConnectivityManager.TYPE_MOBILE){
                    Logger.i("mobile ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }else if(mOldNet.getType() == ConnectivityManager.TYPE_WIFI){
                    Logger.i("wifi ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }else{
                    Logger.i(mOldNet.getTypeName() + " ————> null");
                    for(OnNetChangeListener tListener : mNetListeners){
                    	tListener.onNetChanged(false);
    				}
                }
            }else{
                int tOld = mOldNet.getType();
                int tNew = tNet.getType();
                
                if(tOld!=tNew){
                    if(tOld==ConnectivityManager.TYPE_MOBILE && tNew==ConnectivityManager.TYPE_WIFI){
                        Logger.i("mobile ————> wifi");
                    }else if(tOld==ConnectivityManager.TYPE_WIFI && tNew==ConnectivityManager.TYPE_MOBILE){
                        Logger.i("wifi ————> mobile");
                    }else{
                        Logger.i(tOld + " ————> " + tNew);
                    }
                }else{
                    Logger.i("same===============" + mOldNet.getTypeName());
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

