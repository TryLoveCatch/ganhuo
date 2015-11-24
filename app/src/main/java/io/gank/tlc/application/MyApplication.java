package io.gank.tlc.application;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import de.greenrobot.event.EventBus;
import io.gank.tlc.dao.DaoManager;
import io.gank.tlc.util.L;
import io.gank.tlc.util.UtilManager;

public class MyApplication extends Application{
	//==============常量==================
	private final static String TAG = MyApplication.class.getSimpleName();
	public static final int NOTIFY_ID = 0x000;
	public static final int NOTIFY_ID_MSG = 0x001;
	//==============界面相关==================
	public static Context mContext;
	//==============逻辑相关==================
	private static boolean mIsInited = false;
	private NetCheckReceiver mNetCheck;
	
	//================生命周期相关=====================
	@Override
	public void onCreate() {
		super.onCreate();
		initVar();
		initBug();
		initEventBus();
		initPreload();
		initFresco();
		initDB();
		
		L.i(TAG, "app start ...");
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
	
	//================私有方法相关=====================
	private void onFinish(){
		//清空数据
		//取消网络监听
		mNetCheck.unRegister();
		//清空栈
		UtilManager.getInstance().mUtilActivity.clearActivtyStack();
		//退出程序
		android.os.Process.killProcess(android.os.Process.myPid());  
		System.exit(0); 
	}
	/**
	 * 一些初始化
	 * 全局唯一的
	 */
	private void initVar(){
		if (MyApplication.mIsInited)
            return;
		MyApplication.mIsInited = true;
		MyApplication.mContext = getApplicationContext();
		//初始化
		UtilManager.getInstance().init(this);
		//数据库
		initDB();

		//网络监听
		mNetCheck = new NetCheckReceiver(this);
		mNetCheck.register();
		
//		if(UtilManager.getInstance().mUtilPhone.getSystemVersion() >= 14){
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//			.detectDiskReads()
//			.detectDiskWrites()
//			.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
//			.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
//			.build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//			.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
//			.penaltyLog() //打印logcat
//			.penaltyDeath()
//			.build()); 
//		}
	}
	/**
	 * bug收集
	 */
	private void initBug(){
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(this); 
	}
	
	private void initDB(){
		DaoManager.getInstance().init(this);
	}

	private void initFresco(){
		ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
				.setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
				.build();
		Fresco.initialize(this, imagePipelineConfig);
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
