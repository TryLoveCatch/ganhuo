package io.github.trylovecatch.baselibrary.eventbus;

/**
 * Event基类，继承此类防止代码混淆产生的反射异常
 * @ClassName EventBase
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:00:44
 */
public class EventBase {
	
	public boolean isSuc;
	public String errorMsg;

	public EventBase(boolean pIsSuc, String pErrorMsg){
		this.isSuc = pIsSuc;
		this.errorMsg = pErrorMsg;
	}
}
