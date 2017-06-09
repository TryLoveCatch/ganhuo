package io.github.trylovecatch.baselibrary.utils;

import java.io.File;
import java.io.IOException;

import com.bumptech.glide.util.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import io.github.trylovecatch.baselibrary.application.BaseApplication;
import io.github.trylovecatch.baselibrary.log.Logger;

/**
 * 文件相关的工具类
 * @author Administrator
 *
 */
public class UtilFile {
	//==============常量==================
	private final String TAG = UtilFile.class.getSimpleName();
	private final static String DIR_CRASH = "app_crash";//错误日志
	private final static String DIR_PICTURES = "app_pic";//图片
	private final static String DIR_CACHE = "app_cache";//缓存
	private final static String DIR_DOWNLOAD = "app_download";//缓存
	public final static String FILE_PHOTO = "photo.png";//头像
	public final static String FILE_PHOTO_CAMERA = "photo_camera.png";//头像
	//==============界面相关==================
	//==============逻辑相关==================
	private static volatile UtilFile mInstance;
	private Context mContext;
	
	private UtilFile(){
		mContext = BaseApplication.mContext;
	}

	public static UtilFile getInstance(){
		if(mInstance==null){
			synchronized (UtilFile.class){
				if(mInstance==null){
					mInstance = new UtilFile();
				}
			}
		}
		return mInstance;
	}
	
	//===========================对外方法==================================
	/**
     * 得到pic路径
     * 1、有sdcard返回Android/data/com..../Pictures目录
     * 2、无sdcard返回data/data/com.../Pictures目录
     * 
     * @Title: getDirPic
     * @return
     * @return File
     * @date 2014-4-10 下午4:20:05
     */
    public File getDirPic(){
        if(isSDCardWritable()){
            return mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }else{
            return getDirInner(DIR_PICTURES);
        }
    }
    
    public File getDirCache(){
        if(isSDCardWritable()){
            return mContext.getExternalFilesDir(DIR_CACHE);
        }else{
            return getDirInner(DIR_CACHE);
        }
    }
    
    public File getDirCrash(){
        if(isSDCardWritable()){
            return mContext.getExternalFilesDir(DIR_CRASH);
        }else{
            return getDirInner(DIR_CRASH);
        }
    }
    public File getDirDownload(){
    	if(isSDCardWritable()){
    		return mContext.getExternalFilesDir(DIR_DOWNLOAD);
//    		return new File(Environment.getExternalStorageDirectory() , DIR_DOWNLOAD);
    	}else{
    		return getDirInner(DIR_DOWNLOAD);
    	}
    }
	
	
	/**
	 * 判断 该路径的文件或者文件夹是否存在
	 * 
	 * @Title isFileExist
	 * @param pFilePath
	 * @return boolean
	 * @date 2013-12-6 下午4:41:06
	 */
	public boolean isFileExist(String pFilePath){
		try{
			File tFolder = new File(pFilePath);
			return isFileExist(tFolder);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 判断 该路径的文件或者文件夹是否存在
	 * 
	 * @Title isFileExist
	 * @param pFile
	 * @return boolean
	 * @date 2013-12-6 下午4:41:29
	 */
	public boolean isFileExist(File pFile){
		try{
			if(pFile.exists()){
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除一个文件
	 * @param filePath
	 * @return
	 */
	public boolean deleteFile(String filePath){
		boolean result = false;
		File file = new File(filePath);
		if(file.exists()){
			if(file.isFile()){
				result = file.delete();
			}else{
			    return true;
			}
		}else{
		    return true;
		}
		return result;
	}
	
	/**
	 * 清除缓存
	 * @return
	 */
	public void clearCache(){
	    //删除内部cache
	    deleteDirInner(DIR_CACHE);
	    //删除sdcard上的cache
	    deleteDirCache();
	}
	
	/**
	 * 得到sdcard上可用的大小
	 * @return
	 */
	public long getAvailableSize(){
		File path =Environment.getExternalStorageDirectory();
        //取得sdcard文件路径
        StatFs statfs=new StatFs(path.getPath());
        //获取block的SIZE
        long blocSize = statfs.getBlockSize();
        //获取BLOCK数量
//        long totalBlocks = statfs.getBlockCount();
        //可用的Block的数量
        long availaBlock = statfs.getAvailableBlocks();
        return availaBlock*blocSize;
	}
	
	
	
	
	
	//===========================私有方法==================================
	
	/**
	 * sdcard是否被移除
	 * 
	 * @Title isExternalStorageRemovable
	 * @return boolean
	 * @date 2013-12-6 下午4:33:00
	 */
	@TargetApi(9)
    private boolean isExternalStorageRemovable() {
        if (hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
	
	/**
	 * 判断sd卡读写能力
	 * @return
	 */
	public boolean isSDCardWritable(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !isExternalStorageRemovable()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据dir名字 得到文件
	 * 存在 返回
	 * 不存在 创建
	 * 
	 * 
	 * @Title getDir
	 * @param pDir
	 * @return File
	 * @date 2013-12-6 下午4:52:02
	 */
	private File getDirInner(String pDir){
		File tFile = null;
		String tRoot = mContext.getFilesDir().getParent();
		if(!TextUtils.isEmpty(tRoot)){
		    String tDir = tRoot + File.separator + pDir;
			if(createDir(tDir)){
				tFile = new File(tDir);
			}else{
				Logger.i(TAG, "getDir is null");
			}
		}else{
			Logger.i(TAG, "getDir is null");
		}
		return tFile;
	}
	/**
	 * 根据文件夹名字 创建文件夹
	 * @param pDir
	 * @return
	 */
	private boolean createDir(String pDir) {
		boolean result = false;
		if(!TextUtils.isEmpty(pDir)){
			File folder = new File(pDir);
			if (!folder.exists()) {
				if (folder.mkdirs()) {
					createNoMedia(pDir);
					result = true;
				} else {
					result = false;
				}
			}else{
				createNoMedia(pDir);
				result = true;
			}
		}
		return result;
	}
	//创建一个.nomedia的文件 不被图库扫描
	private void createNoMedia(String dir){
		File f = new File(dir, ".nomedia");
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 删除文件夹
     * @param pDir
     * @return
     */
    private void deleteDirInner(String pDir){
        String tRoot = mContext.getFilesDir().getParent() + File.separator + pDir;
        deleteDir(new File(tRoot));
    }
    private void deleteDirCache(){
        if(isSDCardWritable()){
            File tDir = mContext.getExternalFilesDir(DIR_CACHE);
            deleteDir(tDir);
        }
    }
    private void deleteDir(File pDir){
        if(pDir.exists()){
            String[] files = pDir.list();
            if(files!=null && files.length>0){
                File file;
                for(int i=0; i<files.length; i++){
                    file = new File(pDir, files[i]);
                    if(file.exists()){
                        file.delete();
                    }
                }
            }
        }
    }

	private boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}
}
	
