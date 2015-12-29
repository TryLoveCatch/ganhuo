package io.gank.tlc.framework.event;

/**
 * Event基类，继承此类防止代码混淆产生的反射异常
 * @ClassName EventBase
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:00:44
 */
public class EventBase {
	
	public final static int TOKEN_INVAILD = -2;

	public boolean resultSuc;
	public String resultMsg;
	
	public int status;
	
	public EventBase(){
		resultSuc = true;
	}
	public EventBase(boolean pResultSuc, String pResultMsg){
		this.resultSuc = pResultSuc;
		this.resultMsg = pResultMsg;
	}
}
