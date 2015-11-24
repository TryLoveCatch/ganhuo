package io.gank.tlc.util;

import android.content.Context;

/**
 * UtilManager的所有成员变量的类的构造函数
 * 都不是 public的 这样其他包就不能直接访问 
 * 只能通过utilManager来访问
 * 起到解耦和的作用
 * 
 * @Package com.lp.im.util
 * @ClassName UtilManager
 * @author TryLoveCatch
 * @date 2013-12-6 下午3:50:14
 */
public class UtilManager {

	public UtilPhone mUtilPhone;
	public UtilSharedP mUtilSharedP;
	public UtilActivity mUtilActivity;
	public UtilFile mUtilFile;
	public UtilNet mUtilNet;
	
	
	private UtilManager(){}
	private static UtilManager mUtilManager = new UtilManager();
	public static UtilManager getInstance(){
		return mUtilManager;
	}
	
	
	public void init(Context pContext){
		this.mUtilPhone = new UtilPhone(pContext);
		this.mUtilSharedP = new UtilSharedP(pContext);
		this.mUtilActivity = new UtilActivity();
		this.mUtilFile = new UtilFile(mUtilManager, pContext);
		this.mUtilNet = new UtilNet(pContext);
	}
}
