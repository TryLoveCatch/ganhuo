package io.github.trylovecatch.baselibrary.utils;

import java.util.Stack;

import android.app.Activity;
import io.github.trylovecatch.baselibrary.log.Logger;


/** 窗口管理类 */
public class UtilActivity {
	//==============常量==================
	private static final String TAG = UtilActivity.class.getSimpleName();
	//==============界面相关==================
	//==============逻辑相关==================
	private static volatile UtilActivity mInstance;
	private UtilActivity(){
		
	}
	public static UtilActivity getInstance(){
		if(mInstance==null){
			synchronized (UtilActivity.class){
				if(mInstance==null){
					mInstance = new UtilActivity();
				}
			}
		}
		return mInstance;
	}

	/** 自定义窗口管理栈 */
	private Stack<Activity> activityStack = new Stack<Activity>();

	/** 把窗口引用压入堆栈顶部 */
	public void pushActivityToStack(Activity activity) {
		activityStack.push(activity);
//		Logger.i(TAG,"当前窗口增加：" + activity.getClass().getName());
	}

	/** 从栈中移除窗口引用 */
	public void removeActivityFromStack(Activity activity) {
		activityStack.remove(activity);
//		Logger.i(TAG,"当前窗口移除：" + activity.getClass().getName());
	}

	/** 获取当前栈顶窗口类名 */
	public String getStackTopActivityClsName() {
		return activityStack.peek().getClass().getName();
	}

	/** 查找栈顶窗口引用,即当前处于活动的窗口 */
	public Activity getTopActivityFromStack() {
		if(activityStack.size()==0){
			return null;
		}
		return activityStack.peek();
	}

	/** 清空栈 */
	public void clearActivtyStack() {
		Logger.i(TAG,"关闭窗口：" + activityStack.size());
		for(int i=activityStack.size()-1;i>=0;i--){
			Logger.i(TAG, "准备关闭"+i);
			Activity activity = activityStack.get(i);
			if(activity!=null){
				activity.finish();
			}
			Logger.i(TAG,"关闭窗口：" + activity.getClass().getName());
		}
		activityStack.clear();
		
//		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
