package io.gank.tlc.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类
 * 
 * @author Administrator
 * 
 */
public class UtilSharedP {
	// ==============常量==================
	private final String TAG = UtilSharedP.class.getSimpleName();
	private final static String FILE_NAME = "pharos";
	// ==============界面相关==================
	// ==============逻辑相关==================
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	UtilSharedP(Context context) {
		sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public SharedPreferences getSp() {
		return sp;
	}

	// uid
	public void setUid(int pUid) {
		editor.putInt("uid", pUid);
		editor.commit();
	}
	
	public int getUid() {
		return sp.getInt("uid", -1);
	}
	// 用户名
	public void setUserName(String pUserName) {
		editor.putString("userName", pUserName);
		editor.commit();
	}

	public String getUserName() {
	    return sp.getString("userName", "");
	}
	// 用户名
	public void setNickName(String pNickName) {
		editor.putString("nickName", pNickName);
		editor.commit();
	}
	
	public String getNickName() {
		return sp.getString("nickName", "");
	}
	
	// 密码
	public void setUserPassword(String pUserPassword) {
	    editor.putString("userPassword", pUserPassword);
	    editor.commit();
	}
	
	public String getUserPassword() {
	    return sp.getString("userPassword", "");
	}
	// 第三方登录
	public void setUserId(String pUserId) {
	    editor.putString("userId", pUserId);
	    editor.commit();
	}
	
	public String getUserId() {
	    return sp.getString("userId", "");
	}
	
	
	public void clearUser(){
		setUserId("");
		setUserName("");
		setUserPassword("");
	}
}
