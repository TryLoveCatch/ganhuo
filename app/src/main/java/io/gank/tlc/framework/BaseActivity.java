package io.gank.tlc.framework;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.tlc.R;
import io.gank.tlc.application.MyApplication;
import io.gank.tlc.application.OnNetChangeListener;
import io.gank.tlc.share.view.EmptyView;
import io.gank.tlc.util.L;
import io.gank.tlc.util.T;
import io.gank.tlc.util.UtilManager;
import rx.Subscription;

/**
 * Activity、FragmentActivity基类<br>
 * @Package com.jiaoyang.video.framework
 * @ClassName BaseActivity
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:00:58
 */
public abstract class BaseActivity extends AppCompatActivity implements IUI, OnNetChangeListener {
	// ================一些常量=====================
	// ================界面相关=====================
    @Nullable
    @Bind(R.id.emptyView)
    protected EmptyView mEmptyView;
	// ================逻辑相关=====================
	/**
	 * 销毁时通知DataTask cancel的观察者
	 */
    private ArrayList<Subscription> mSubscriptions = new ArrayList<>();
	private boolean forbidStartActivityAnimation = false;
	private boolean forbidFinishActivityGesture = false;//默认不要左划finish
	// ================生命周期相关=====================
    /**
     * 做了4件事:<br>
     * 1、setContentView<br>
     * 2、初始化Views<br>
     * 3、调用initViewProperty<br>
     * 4、调用initData
     * @Title: onCreate
     * @param savedInstanceState
     * @param layoutResId
     * @return void
     * @date Apr 18, 2014 11:23:00 AM
     */
    protected void onCreate(Bundle savedInstanceState, int layoutResId) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setContentView(layoutResId);
        UtilManager.getInstance().mUtilActivity.pushActivityToStack(this);
        ((MyApplication)getApplication()).getNetCheckReceiver().getFilter().addListener(this);
        
        ButterKnife.bind(this);
        initData();
        initViewProperty();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        for(Subscription tSub : mSubscriptions){
            tSub.unsubscribe();
        }
        /** 从栈中移除该窗口引用 */
		UtilManager.getInstance().mUtilActivity.removeActivityFromStack(this);
		((MyApplication)getApplication()).getNetCheckReceiver().getFilter().removeListener(this);
		super.onDestroy();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    // ================事件接口================
    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        if(!this.forbidStartActivityAnimation){
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }
    }
    
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        if(!this.forbidStartActivityAnimation){
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }
    }
    
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        if(!this.forbidStartActivityAnimation){
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
    
    private int startX = 0;
    private int startY = 0;
    /* (non-Javadoc)
     * 手势finish
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        
        if(this.forbidFinishActivityGesture){
            return super.dispatchTouchEvent(event);
        }
        
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startX = (int)event.getX();
            startY = (int)event.getY();
        }
        
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(event.getX() - startX > 100 && Math.abs(event.getY() - startY) < 200){
                finish();
                return true;
            }
        }
        
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        io.gank.tlc.framework.BaseFragment outer = (io.gank.tlc.framework.BaseFragment)fm.findFragmentById(R.id.content_frame);
        if(outer != null){
            if(outer.onBackPressed()){
                return;
            }
        }
        super.onBackPressed();
    }
    
    @Override
	public void onNetChanged(boolean isAvailable) {
	}
    
    // ================对外方法=====================
    /**
     * 设置禁止启动Activity动画
     * @Title: setForbidStartActivityAnimation
     * @param forbidStartActivityAnimation
     * @return void
     * @date May 14, 2014 11:14:18 AM
     */
    public void setForbidStartActivityAnimation(boolean forbidStartActivityAnimation) {
        this.forbidStartActivityAnimation = forbidStartActivityAnimation;
    }
    
    /**
     * 设置禁止finish activity手势，用于存在viewpager等手势冲突的activity
     * @Title: setForbidFinishActivityGesture
     * @param forbidFinishActivityGesture
     * @return void
     * @date May 14, 2014 11:48:27 AM
     */
    public void setForbidFinishActivityGesture(boolean forbidFinishActivityGesture) {
        this.forbidFinishActivityGesture = forbidFinishActivityGesture;
    }

    protected void addSubscription(Subscription pSub) {
        mSubscriptions.add(pSub);
    }
    
    protected void setContentFragment(Class<? extends io.gank.tlc.framework.BaseFragment> fragmentClass) {
        Bundle arguments = null;
        if (getIntent() != null) {
            arguments = getIntent().getExtras();
        }
        setContentFragment(fragmentClass, arguments);
    }

    protected void setContentFragment(Class<? extends io.gank.tlc.framework.BaseFragment> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }

    protected void setContentFragment(Class<? extends io.gank.tlc.framework.BaseFragment> fragmentClass, Bundle arguments, int contentId) {
        L.d("set content fragment. class={}", fragmentClass.getName());

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(contentId, fragment);
        t.commit();
    }

    protected void setContentFragment(String fragmentClassName, Bundle arguments) {
        L.d("set content fragment. class={}", fragmentClassName);

        Fragment fragment = Fragment.instantiate(this, fragmentClassName, arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }


    //自己增加
    protected final static int TYPE_NO_NET = 1;
    protected final static int TYPE_NO_DATA = 2;
    protected final static int TYPE_ERROR = 3;
    protected final static int TYPE_LOADING = 4;

    protected void hideEmptyView(View pView){
        mEmptyView.setVisibility(View.GONE);
        pView.setVisibility(View.VISIBLE);
    }

    protected void showEmptyView(View pView, int pType){
        if(mEmptyView==null) return;
        switch(pType){
            case TYPE_NO_NET:
                mEmptyView.showNoNet();
                break;
            case TYPE_NO_DATA:
                mEmptyView.showNoData();
                break;
            case TYPE_ERROR:
                mEmptyView.showOnFail();
                break;
            case TYPE_LOADING:
                mEmptyView.showLoading();
                break;
        }
        pView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }
    
    protected void showToast(String text) {
        T.showShort(this, text);
    }

    protected void showToast(int resId) {
        T.showShort(this, resId);
    }
    
    public boolean isListValid(List<?> plist){
    	return plist!=null && plist.size() > 0;
    }
}
