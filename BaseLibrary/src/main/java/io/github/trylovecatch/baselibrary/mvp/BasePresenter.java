package io.github.trylovecatch.baselibrary.mvp;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Created by lipeng21 on 2017/6/9.
 *
 *
 * 我的理解Presenter和View应该都是一一对应的
 * 但是Model可以有多个
 * 也就是一个Presenter里面可以调用多个Model
 * 而Model应该是被细分为一个一个的功能
 * Presenter可以来组合这些Modle，生成一个新的Model供View使用
 *
 */

public abstract class BasePresenter<T extends IView> implements IPresenter {

    protected Reference<T> mViewRef;

    protected abstract void init();

    protected T getView(){
        if(mViewRef!=null){
            return mViewRef.get();
        }
        return null;
    }

    protected void attachView(T pView){
        mViewRef = new SoftReference<>(pView);
    }

    protected void detachView(){
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

}
