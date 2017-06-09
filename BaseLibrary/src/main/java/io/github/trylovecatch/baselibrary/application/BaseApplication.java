package io.github.trylovecatch.baselibrary.application;

import org.greenrobot.eventbus.EventBus;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import io.github.trylovecatch.baselibrary.BuildConfig;
import io.github.trylovecatch.baselibrary.log.LogBuilder;
import io.github.trylovecatch.baselibrary.log.LogPrintStyle;
import io.github.trylovecatch.baselibrary.log.LogPrintStyle2;
import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.log.XLogStyle;
import io.github.trylovecatch.baselibrary.utils.UtilActivity;

public abstract class BaseApplication extends Application{
	//==============常量==================
	//==============界面相关==================
	public static Context mContext;
	//==============逻辑相关==================
	private static boolean mIsInited = false;
	private NetCheckReceiver mNetCheck;
	
	//================生命周期相关=====================
	@Override
	public void onCreate() {
		super.onCreate();
		initLog();
		initVar();
		initNet();
		initBug();
		initDB();
		initEventBus();
		initRetrofit();
		initPreload();

		Logger.i("app start ...");
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		onFinish();
	}
	//================对外方法相关=====================
	public NetCheckReceiver getNetCheckReceiver(){
		return mNetCheck;
	}

	public abstract void initRetrofit();
	
	//================私有方法相关=====================
	private void onFinish(){
		Logger.i("app finishing ...");
		//清空数据
		//取消网络监听
		mNetCheck.unRegister();
		//清空栈
		UtilActivity.getInstance().clearActivtyStack();
		//退出程序
		android.os.Process.killProcess(android.os.Process.myPid());  
		System.exit(0); 
	}
	/**
	 * 一些初始化
	 * 全局唯一的
	 */
	private void initVar(){
		if (BaseApplication.mIsInited)
            return;
		BaseApplication.mIsInited = true;
		BaseApplication.mContext = getApplicationContext();


	}
	private void initLog(){

		Logger.initialize(new LogBuilder()
//				.logPrintStyle(new LogPrintStyle2())
				.logPrintStyle(new XLogStyle())
				.showMethodLink(true)
				.showThreadInfo(true)
				.tagPrefix("TryLoveCatch")
//				.globalTag("TryLoveCatch")
				.methodOffset(0)
				.logPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
				.build()
		);
	}
	private void initNet(){
		//网络监听
		mNetCheck = new NetCheckReceiver(this);
		mNetCheck.register();
	}
	/**
	 * bug收集
	 */
	private void initBug(){
		CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this); 
	}
	
	private void initDB(){
	}


	private void initEventBus(){
		EventBus.builder().throwSubscriberException(true).build();
	}

	/**
	 * 
	 * 进行首页数据的预加载
	 * @Title initPreload
	 * @Param 
	 * @Return void
	 */
	private void initPreload(){
		
	}


}
