package io.github.trylovecatch.baselibrary.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.utils.UtilsToast;
import io.github.trylovecatch.baselibrary.utils.UtilActivity;
import io.github.trylovecatch.baselibrary.utils.UtilFile;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
 *  
 */
public class CrashHandler implements UncaughtExceptionHandler {  
      
    public static final String TAG = CrashHandler.class.getSimpleName();  
      
    //系统默认的UncaughtException处理类   
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
    //CrashHandler实例  
    private static CrashHandler INSTANCE = new CrashHandler();  
    //程序的Context对象  
    private Context mContext; 
    //用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();  
  
    //用于格式化日期,作为日志文件名的一部分  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
  
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
    
  
    /** 
     * 初始化 
     *  
     * @param context 
     */  
    public void init(Context context) {  
        mContext = context;  
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            //如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        }else{  
        	if(UtilActivity.getInstance().getTopActivityFromStack()!=null){
        		showDialog();
        	}else{
        		showToast();
        		try {  
        			Thread.sleep(1500);  
        		} catch (InterruptedException e) {  
        			e.printStackTrace();
        		}  
        		//退出程序  
        		android.os.Process.killProcess(android.os.Process.myPid());  
        		System.exit(1); 
        	}
        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
        //打印错误信息 开发的时候 便于查找错误
        ex.printStackTrace();
        
        //收集设备参数信息   
        collectDeviceInfo(mContext);  
        //保存日志文件   
        saveCrashInfo2File(ex);
         
        return true;  
    } 
    
    //使用Toast来显示异常信息  
    private void showToast(){
    	new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                UtilsToast.show(mContext, "哎呀，出了些问题，请尝试重启程序", Toast.LENGTH_LONG);
                Looper.loop();  
            }  
        }.start(); 
    }
    
    private void showDialog(){
    	new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                new AlertDialog.Builder(UtilActivity.getInstance().getTopActivityFromStack())
                .setTitle("提示")
                .setCancelable(false)  
                .setMessage("程序出错, 即将罢工")
                .setNeutralButton("关闭", new OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
//                    	UtilManager.getInstance().mUtilActivity.clearActivtyStack();
//                    	//退出程序  
//                        android.os.Process.killProcess(android.os.Process.myPid());  
//                        System.exit(1); 
                    	((Application)mContext).onTerminate();
                    }  
                })
                .setPositiveButton("重启", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
                        UtilActivity.getInstance().clearActivtyStack();
						
						// 以下用来捕获程序崩溃异常
//				        Intent intent = new Intent();
//				        // 参数1：包名，参数2：程序入口的activity
//                        intent.setPackage(mContext.getPackageName());
//                        Logger.i(mContext.getPackageName());
//                        intent.setAction(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        Intent intent = mContext.getPackageManager()
                                .getLaunchIntentForPackage(mContext.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0,  
				                intent, PendingIntent.FLAG_ONE_SHOT);
				        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);  
			            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1200,
			                    restartIntent); // 2秒钟后重启应用
			            //退出程序  
                        android.os.Process.killProcess(android.os.Process.myPid());  
                        System.exit(1);
					}
				})
                .create().show();  
                Looper.loop();  
            }  
        }.start(); 
    }
      
    /** 
     * 收集设备参数信息 
     * @param ctx 
     */  
    public void collectDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "an error occured when collect package info", e);  
        }  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                Log.e(TAG, "an error occured when collect crash info", e);  
            }  
        }  
    }  
  
    /** 
     * 保存错误信息到文件中 
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private String saveCrashInfo2File(Throwable ex) {  
          
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
          
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
        String result = writer.toString();  
        sb.append(result);  
        FileOutputStream fos = null;
        try {  
            long timestamp = System.currentTimeMillis();  
            String time = formatter.format(new Date());  
            String fileName = "crash-" + time + "-" + timestamp + ".log";  
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                File tFile  = UtilFile.getInstance().getDirCrash();
                if(tFile==null){
                	return null;
                }
                fos = new FileOutputStream(new File(tFile, fileName));
                fos.write(sb.toString().getBytes());  
            } 
            return fileName;  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while writing file...", e);  
        }finally{
        	try {
        		if(fos!=null){
        			fos.close();
        		}
			} catch (IOException e) {} 
        }
        return null;  
    }  
} 
