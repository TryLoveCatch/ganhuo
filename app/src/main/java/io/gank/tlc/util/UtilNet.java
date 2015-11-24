package io.gank.tlc.util;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class UtilNet {
	//==============常量==================
	private final String TAG = UtilNet.class.getSimpleName();
	public final int NETWORKINFO_INVALID = -1;//当前无网络
	public final int NETWORKINFO_MOBILE_WAP = 1;//wap网络状态
	public final int NETWORKINFO_MOBILE_NET = 2;//net网络
	public final int NETWORKINFO_WIFI = 3;//wifi网络
	//==============界面相关==================
	private Context context;
	//==============逻辑相关==================
	
	UtilNet(Context context){
		this.context = context;
	}
	
	/**
	 * 得到ConnectivityManager 网络管理对象
	 * @param context
	 * @return
	 */
	private ConnectivityManager getConnectManager(){
		ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectMgr;
	}
	
	/**
	 * 得到NetworkInfo类
	 * @param context
	 * @return
	 */
	public NetworkInfo getNetWorkInfo(){
		ConnectivityManager connectMgr = getConnectManager();
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
	public boolean isNetAvailable(){
		if(context != null){
			NetworkInfo nwInfo = getNetWorkInfo();
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
	public boolean goNetSetting(){
		if(isNetAvailable()){
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
	private int getNetWorkType(){
		NetworkInfo nw = getNetWorkInfo();
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
	public boolean isWifiNetWork(){
		if(getNetWorkType() == ConnectivityManager.TYPE_WIFI){
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
	public boolean isMobileNetWrok(){
		if(getNetWorkType() == ConnectivityManager.TYPE_MOBILE){
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
	public int getCurrentApn()
	{
		NetworkInfo netWorkInfo = getNetWorkInfo();
		if(netWorkInfo != null){
			if(netWorkInfo.getState() == NetworkInfo.State.CONNECTED
					&& netWorkInfo.getExtraInfo() != null
					&& netWorkInfo.getExtraInfo().toLowerCase().endsWith("wap")){
				L.i(TAG, netWorkInfo.getExtraInfo().toLowerCase());
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
//					L.i(TAG, "无法切换到net网络");
//				}else{
//					if(setDefaultAPN(context,defaultApn.id)){
//						L.i(TAG, "切换到net网络");
//						return NETWORKINFO_MOBILE_NET;
//					}else{
//						L.i(TAG, "无法切换到net网络");
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
	public boolean IsWapNetWork()
	{
		if(getCurrentApn() == NETWORKINFO_MOBILE_WAP)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**判断是否是Net网络*/
	public boolean IsNetNetwork()
	{
		if(getCurrentApn()== NETWORKINFO_MOBILE_NET)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public final Uri APN_TABLE_URI = Uri.parse("content://telephony/carriers"); 
	public final Uri PREFERRED_APN_URI =Uri.parse("content://telephony/carriers/preferapn"); 
	public final Uri CURRENT_APN_URI =Uri.parse("content://telephony/carriers/current");
	
	/**
	 * 得到当前可以使用的apn列表
	 * @param context
	 * @return
	 */
	private List<Apn> getCurrentApns() { 
		Cursor cr = context.getContentResolver().query(CURRENT_APN_URI, null, null, null, null); 
		List<Apn> apns = new ArrayList<Apn>();
		while(cr!=null && cr.moveToNext()){ 
			Apn bean = new Apn();
			bean.id = cr.getInt(cr.getColumnIndex("_id"));
			bean.apn = cr.getString(cr.getColumnIndex("apn"));
			apns.add(bean);
		}
		return apns;
	}
	/**
	 * 根据apn名字 得到apn所有信息
	 * 主要是得到id
	 * @param context
	 * @param apnName
	 * @return
	 */
	private List<Apn> getAllApn(String apnName) { 
		Cursor cr = context.getContentResolver().query(APN_TABLE_URI, null,  "apn = '" + apnName + "' ", null, null); 
		List<Apn> apns = new ArrayList<Apn>();
		while(cr!=null && cr.moveToNext()){ 
			Apn bean = new Apn();
			bean.id = cr.getInt(cr.getColumnIndex("_id"));
			bean.apn = cr.getString(cr.getColumnIndex("apn"));
			apns.add(bean);
		}
		return apns;
	}
	/**
	 * 强制设置当前的apn
	 * @param context
	 * @param id
	 * @return
	 */
	private boolean setDefaultAPN(int id) {
		boolean res = false;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("apn_id", id);
		try {
			resolver.update(PREFERRED_APN_URI, values, null, null);
			Cursor c = resolver.query(
                    PREFERRED_APN_URI, 
                    new String[]{"name","apn"}, 
                    "_id="+id, 
                    null, 
                    null);
            if(c != null)
            {
                res = true;
                c.close();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 判断是不是中国电信的wap网络
	 * 因为中国电信和移动等用的不是同一个代理ip地址
	 * @param context
	 * @return
	 */
	public boolean isCtwap()
	{
		boolean ret = false;
		if(context != null)
		{
			NetworkInfo nwInfo = getNetWorkInfo();
			if(nwInfo != null)
			{
				if((nwInfo.getExtraInfo()==null || nwInfo.getExtraInfo().equals("") ) && nwInfo.getExtraInfo().toLowerCase().contains("ctwap"))
				{
					ret = true;
				}
			}
		}
		return ret;
	}
	
    
    /**
     * 判断3g和2g
     * 在中国,联通3g为UMTS或者HSDPA,电信的3g为EVDO,移动和联通的2g为GPRS或者EGDE,电信的2G为CMDA
     * @param context
     * @return 3g
     */
    public boolean isConnectionFast(){
    	TelephonyManager telmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telmanager.getNetworkType();
//		getCurrentApn(context);
        switch(type){
        case TelephonyManager.NETWORK_TYPE_1xRTT:
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
        	L.i(TAG, "电信2g NETWORK_TYPE_CDMA");
            return false; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
        	L.i(TAG, "移动联通2g NETWORK_TYPE_EDGE");
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
        	L.i(TAG, "电信3g NETWORK_TYPE_EVDO_0");
            return true; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
        	L.i(TAG, "电信3g NETWORK_TYPE_EVDO_A");
            return true; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
        	L.i(TAG, "移动联通2g NETWORK_TYPE_GPRS");
            return false; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
        	L.i(TAG, "联通3g NETWORK_TYPE_HSDPA");
            return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
            return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
            return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
        	L.i(TAG, "联通3g NETWORK_TYPE_UMTS");
            return true; // ~ 400-7000 kbps
        // Unknown
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            return false; 
        default:
            return false;
        }
    }
}
class Apn{
	int id;
	String apn;
}
