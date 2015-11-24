package io.gank.tlc.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 手机相关的工具类
 * 
 * @author Administrator
 * 
 */
public class UtilPhone {
	// ==============常量==================
	private final static String TAG = UtilPhone.class.getSimpleName();
	// ==============界面相关==================
	private Context context;

	// ==============逻辑相关==================

	UtilPhone(Context context) {
		this.context = context;
	}

	/**
	 * 手机IMEI
	 * 
	 * @param context
	 * @return
	 */
	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * 获得本机ip地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得本机mac地址
	 * 
	 * @param context
	 * @return
	 */
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 得到手机的密度 0.75 1.0 1.5
	 * 
	 * @Title: getScreenDensity
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return float
	 * @throws
	 */
	private float density = -1;

	public float getScreenDensity() {
		if (density == -1) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return density;
	}

	/**
	 * dip 转换为px
	 * 
	 * @Title: getPxFromDip
	 * @Description: TODO
	 * @param @param context
	 * @param @param dip
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getPxFromDip(int dip) {
		return (int) (0.5f + getScreenDensity() * dip);
	}

	/**
	 * 手机的高
	 * 
	 * @Title: getAllHeight
	 * @Description: TODO
	 * @param @param context
	 * @param @return 单位px
	 * @return int
	 * @throws
	 */
	public int getAllHeight() {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 屏幕的高
	 * 
	 * @Title: getScreenHeight
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getScreenHeight() {
		return getAllHeight() - getStatusBarHeight();
	}

	// public int getScreenHeight(){
	// WindowManager wm = (WindowManager) context
	// .getSystemService(Context.WINDOW_SERVICE);
	// System.out.println(getAllHeight());
	// System.out.println(wm.getDefaultDisplay().getHeight());
	// return wm.getDefaultDisplay().getHeight();
	// }
	/**
	 * 状态栏的高度
	 * 
	 * @Title: getStatusBarHeight
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getStatusBarHeight() {
		// Rect rect= new Rect();
		//
		// Window window= ((Activity) context).getWindow();
		// window.getDecorView().getWindowVisibleDisplayFrame(rect);
		// System.out.println(rect.top);
		// return rect.top;

		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sbar;
	}

	/**
	 * 手机的宽 即屏幕的宽
	 * 
	 * @Title: getScreenWidth
	 * @Description: TODO
	 * @param @param context
	 * @param @return 单位px
	 * @return int
	 * @throws
	 */
	public int getScreenWidth() {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 得到版本号
	 * 
	 * @Title: getvVersionCode
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getvVersionCode() {
		int versonCode = -1;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versonCode = info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versonCode;
	}

	/**
	 * 得到版本名字
	 * 
	 * @Title: getVersionName
	 * @Description: TODO
	 * @param @param context
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getVersionName() {
		String versionName = null;
		if (versionName == null) {
			try {
				PackageInfo info = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				versionName = info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return versionName;
	}

	/**
	 * 得到android版本 8 为2.2
	 * 
	 * @Title: getSystemVersion
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 读取Manifest文件中定义的meta-data
	 * 
	 * @Title: getMetaData
	 * @Description: TODO
	 * @param @param context
	 * @param @param key
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getMetaData(String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到当前日
	 * 
	 * @Title: getCurrentDay
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到当前月
	 * 
	 * @Title: getCurrentDay
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前年
	 * 
	 * @Title: getCurrentDay
	 * @Description: TODO
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到包路径
	 * 
	 * @return
	 */
	public String getPackage() {
		return this.context.getPackageName();
	}

	/**
	 * Android 2.2
	 * 
	 * @Title hasFroyo
	 * @return boolean
	 * @date 2013-12-6 下午4:23:45
	 */
	public boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Android 2.3
	 * 
	 * @Title hasGingerbread
	 * @return boolean
	 * @date 2013-12-6 下午4:23:50
	 */
	public boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * Android 3.0
	 * 
	 * @Title hasHoneycomb
	 * @return boolean
	 * @date 2013-12-6 下午4:24:18
	 */
	public boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Android 3.1
	 * 
	 * @Title hasHoneycombMR1
	 * @return boolean
	 * @date 2013-12-6 下午4:24:31
	 */
	public boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * Android 4.1
	 * 
	 * @Title hasJellyBean
	 * @return boolean
	 * @date 2013-12-6 下午4:25:07
	 */
	public boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	// 加密
	/**
	 * url编码方式
	 * 
	 * @param str
	 *            指定编码方式，未指定默认为utf-8
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String urlencode(String str) throws UnsupportedEncodingException {
		String rc = URLEncoder.encode(str, "utf-8");
		return rc.replace("*", "%2A");
	}

	/**
	 * 得到MD5 注意 此MD5 都是小写
	 * 
	 * @param str
	 * @return
	 */
	public String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString().toLowerCase();
	}

	/**
	 * 判断程序是否在后台运行
	 * 
	 * @Title isBackground
	 * @return boolean
	 * @date 2014-1-26 下午5:27:51
	 */
	public boolean isBackground() {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					L.i(TAG, String.format("Background App:", appProcess.processName));
					return true;
				} else {
					L.i(TAG, String.format("Foreground App:", appProcess.processName));
					return false;
				}
			}
		}
		return false;
	}
	/**
	 * 
	 * 保留两位有效数字
	 * @Title formatFloat
	 * @Param @param pValue
	 * @Param @return
	 * @Return String
	 */
	public String formatFloat(float pValue){
		 DecimalFormat df = new DecimalFormat("#0.00");
		 return df.format(pValue);
	}
	
	
	public void hideSoft(View pView){
		InputMethodManager imm = (InputMethodManager)pView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(pView.getWindowToken() ,0);
	}
	public void showSoft(View pView){
		InputMethodManager imm = (InputMethodManager)pView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(pView ,0);
	}
	
	/**
     * 转换图片成圆形
     * 
     * @param bitmap
     *            传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
    
    //随即颜色
    public int randomColor(){
    	return (int)Math.round(Math.random() * 255);
    }
    
    /**
     * 获取当前屏幕旋转角度
     * 
     * @param activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public int getScreenRotationOnPhone() {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
        case Surface.ROTATION_0:
            return 0;

//        case Surface.ROTATION_90:
//            return 90;

        case Surface.ROTATION_180:
            return 180;

//        case Surface.ROTATION_270:
//            return -90;
        }
        return 0;
    }
}
