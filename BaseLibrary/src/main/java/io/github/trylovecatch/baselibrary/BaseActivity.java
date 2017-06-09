package io.github.trylovecatch.baselibrary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import butterknife.ButterKnife;
import io.github.trylovecatch.baselibrary.application.BaseApplication;
import io.github.trylovecatch.baselibrary.application.OnNetChangeListener;
import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.utils.UtilsToast;
import io.github.trylovecatch.baselibrary.utils.UtilActivity;

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
	// ================逻辑相关=====================
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
        UtilActivity.getInstance().pushActivityToStack(this);
        ((BaseApplication)getApplication()).getNetCheckReceiver().getFilter().addListener(this);
        
        ButterKnife.bind(this);
        initData();
        initViewProperty();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        /** 从栈中移除该窗口引用 */
        UtilActivity.getInstance().removeActivityFromStack(this);
		((BaseApplication)getApplication()).getNetCheckReceiver().getFilter().removeListener(this);
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
        BaseFragment outer = (BaseFragment)fm.findFragmentById(R.id.content_frame);
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

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass) {
        Bundle arguments = null;
        if (getIntent() != null) {
            arguments = getIntent().getExtras();
        }
        setContentFragment(fragmentClass, arguments);
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }

    protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments, int contentId) {
        Logger.d("set content fragment. class={}", fragmentClass.getName());

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(contentId, fragment);
        t.commit();
    }

    protected void setContentFragment(String fragmentClassName, Bundle arguments) {
        Logger.d("set content fragment. class={}", fragmentClassName);

        Fragment fragment = Fragment.instantiate(this, fragmentClassName, arguments);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment);
        t.commit();
    }

    public void showToast(String text) {
        UtilsToast.showShort(this, text);
    }

    public void showToast(int resId) {
        UtilsToast.showShort(this, resId);
    }
}
