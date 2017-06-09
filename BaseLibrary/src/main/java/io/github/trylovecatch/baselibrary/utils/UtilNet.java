package io.github.trylovecatch.baselibrary.utils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import io.github.trylovecatch.baselibrary.log.Logger;

public class UtilNet {
	//==============常量==================
	private final String TAG = UtilNet.class.getSimpleName();
	public final int NETWORKINFO_INVALID = -1;//当前无网络
	public final int NETWORKINFO_MOBILE_WAP = 1;//wap网络状态
	public final int NETWORKINFO_MOBILE_NET = 2;//net网络
	public final int NETWORKINFO_WIFI = 3;//wifi网络
	//==============界面相关==================
	//==============逻辑相关==================
	private static volatile UtilNet mInstance;
	private UtilNet(){
	}
	public static UtilNet getInstance(){
		if(mInstance==null){
			synchronized (UtilNet.class){
				if(mInstance==null){
					mInstance = new UtilNet();
				}
			}
		}
		return mInstance;
	}

	
	/**
	 * 得到ConnectivityManager 网络管理对象
	 * @param context
	 * @return
	 */
	private ConnectivityManager getConnectManager(Context context){
		ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectMgr;
	}
	
	/**
	 * 得到NetworkInfo类
	 * @param context
	 * @return
	 */
	public NetworkInfo getNetWorkInfo(Context context){
		ConnectivityManager connectMgr = getConnectManager(context);
		if(connectMgr == null){
			return null;
		}else{
			return connectMgr.getActiveNetworkInfo();
		}
	}
	
	/**
	 * 验证当前是否有网络
	 * @param context
	 * @return
	 */
	public boolean isNetAvailable(Context context){
		if(context != null){
			NetworkInfo nwInfo = getNetWorkInfo(context);
			if(nwInfo != null && nwInfo.isAvailable()){
				return true;
			}
		}
		return false;
	}
	/**
	 * 启动设置网络连接界面
	 * @param context
	 * @return
	 */
	public boolean goNetSetting(Context context){
		if(isNetAvailable(context)){
			return true;
		}else{//假如没有可用网络
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return false;
		}
	}
	
	/**
	 * 得到网络类型，可以判断是wifi，还是mobile连接
	 * @param context
	 * @return
	 */
	private int getNetWorkType(Context context){
		NetworkInfo nw = getNetWorkInfo(context);
		if(nw!=null){
			return nw.getType();
		}else{
			return -1;
		}
	}
	/**
	 * 判断当前网络是否是wifi连接网络
	 * @param context
	 * @return
	 */
	public boolean isWifiNetWork(Context context){
		if(getNetWorkType(context) == ConnectivityManager.TYPE_WIFI){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断当前网络是否是非wifi连接网络
	 * 可能是wap或者net
	 * @param context
	 * @return
	 */
	public boolean isMobileNetWrok(Context context){
		if(getNetWorkType(context) == ConnectivityManager.TYPE_MOBILE){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 得到当前网络的APN
	 * 
	 * 到时候 应该修改 
	 * 不用强制转换为net utilhttp修改一下 就ok了
	 * 
	 * @param context
	 * @return
	 */
	public int getCurrentApn(Context context)
	{
		NetworkInfo netWorkInfo = getNetWorkInfo(context);
		if(netWorkInfo != null){
			if(netWorkInfo.getState() == NetworkInfo.State.CONNECTED
					&& netWorkInfo.getExtraInfo() != null
					&& netWorkInfo.getExtraInfo().toLowerCase().endsWith("wap")){
				Logger.i(TAG, netWorkInfo.getExtraInfo().toLowerCase());
//				String apn = netWorkInfo.getExtraInfo().toLowerCase();
//				String apnPre = apn.substring(0, apn.indexOf("wap"));
//				/**
//				 * 得到当前可用的所有apn
//				 */
//				List<Apn> apns = getCurrentApns(context);
//				Apn defaultApn = null;
//				/**
//				 * 得到符合条件的net网络
//				 * 如 原来是cmwap 就找到cmnet
//				 */
//				for(Apn bean : apns){
//					if(bean.apn.contains(apnPre) && !bean.apn.equals(apn) && bean.apn.contains("net")){//移动竟然还有cmmail，所以加上net的判断
//						defaultApn = bean;
//						break;
//					}
//				}
//				/**
//				 * 如果在当前可用没有找到
//				 * 就得到根据apn名字 得到apn
//				 */
//				if(defaultApn==null){
//					apns = getAllApn(context,apnPre+"net");
//					for(Apn bean : apns){
//						if(bean.apn.contains(apnPre) && !bean.apn.equals(apn)){
//							defaultApn = bean;
//							break;
//						}
//					}
//				}
//				/**
//				 * 下面是强制切换到net网络
//				 */
//				if(defaultApn==null){
//					Logger.i(TAG, "无法切换到net网络");
//				}else{
//					if(setDefaultAPN(context,defaultApn.id)){
//						Logger.i(TAG, "切换到net网络");
//						return NETWORKINFO_MOBILE_NET;
//					}else{
//						Logger.i(TAG, "无法切换到net网络");
//					}
//				}
				return NETWORKINFO_MOBILE_WAP;
			}else{
				return NETWORKINFO_MOBILE_NET;
			}
		}else{
			return NETWORKINFO_INVALID;
		}
	}
	
	
	/**判断是否是wap网络*/
	public boolean IsWapNetWork(Context context)
	{
		if(getCurrentApn(context) == NETWORKINFO_MOBILE_WAP)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**判断是否是Net网络*/
	public boolean IsNetNetwork(Context context)
	{
		if(getCurrentApn(context)== NETWORKINFO_MOBILE_NET)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

}
