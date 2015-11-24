package io.gank.tlc.dao;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by lipeng21 on 2015/11/13.
 */
public class DaoManager {
    private final static String DB_NAME = "ganhuo";
    private static DaoManager mInstance;
    public LiteOrm mOrm;
    private DaoManager(){

    }

    public static DaoManager getInstance(){
        synchronized (DaoManager.class){
            if(mInstance==null){
                synchronized (DaoManager.class){
                    mInstance = new DaoManager();
                }
            }
        }
        return mInstance;
    }

    //初始化
    public void init(Context pContext){
        mOrm = LiteOrm.newCascadeInstance(pContext, DB_NAME);
    }
}
