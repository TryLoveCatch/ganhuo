package io.github.trylovecatch.baselibrary.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import io.github.trylovecatch.baselibrary.application.BaseApplication;

public class UtilScreen {

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
	public static float getScreenDensity() {
		return BaseApplication.mContext.getResources().getDisplayMetrics().density;
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
	public static int getPxFromDip(int dip) {
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
	public static int getAllHeight() {
		return BaseApplication.mContext.getResources().getDisplayMetrics().heightPixels;
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
	public static int getScreenHeight() {
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
	public static int getStatusBarHeight() {
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
			sbar = BaseApplication.mContext.getResources().getDimensionPixelSize(x);
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
	public static int getScreenWidth() {
		return BaseApplication.mContext.getResources().getDisplayMetrics().widthPixels;
	}


    /**
     * 获取当前屏幕旋转角度
     * 
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotationOnPhone() {
        final Display display = ((WindowManager) BaseApplication.mContext
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
